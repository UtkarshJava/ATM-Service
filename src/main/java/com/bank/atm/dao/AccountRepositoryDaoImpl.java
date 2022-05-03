package com.bank.atm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bank.atm.model.ApplicationData;
import com.bank.atm.model.Denomination;
import com.bank.atm.response.ATMRequestResult;
import com.bank.atm.response.AccountResult;
import com.bank.atm.util.DBUtil;
import org.springframework.jdbc.datasource.*;


@Repository
public class AccountRepositoryDaoImpl implements AccountRepositoryDao {

    private JdbcTemplate template;
    
    public AccountRepositoryDaoImpl() {
        this.template = DBUtil.getJdbcTemplate();

    }
    
private ApplicationData applicationData;
	
	public ApplicationData getApplicationData() {
		return applicationData;
	}

	@Autowired
	public void setApplicationData(ApplicationData applicationData) {
		this.applicationData = applicationData;
	}

    public ATMRequestResult getBalance(long accountNumber, int pin) {
    	String sql = "select ACCOUNT_NUMBER, OPENING_BALANCE, OVERDRAFT from BANK.ACCOUNT where ACCOUNT_NUMBER=? AND PIN=?";
    	ATMRequestResult result = this.template.queryForObject(sql, new Object[] { accountNumber, pin }, new RowMapper<ATMRequestResult>() {

            public ATMRequestResult mapRow(ResultSet rowSet, int arg1) throws SQLException {
            	ATMRequestResult requestResult = new ATMRequestResult();
            	AccountResult accResult = new AccountResult();
            	accResult.setAccountNumber(rowSet.getString("ACCOUNT_NUMBER"));
            	accResult.setBalance(rowSet.getInt("OPENING_BALANCE"));
            	accResult.setOverdraft(rowSet.getInt("OVERDRAFT"));
        		requestResult.setCustomerResult(accResult);
                return requestResult;
            }
        });
		return result;

    }

    @Override
    public boolean withdraw(long accountNumber, int amount) {
    	String sql = "update BANK.ACCOUNT set OPENING_BALANCE=OPENING_BALANCE-? where ACCOUNT_NUMBER=?";
    	return template.update(sql,new Object[] { amount, accountNumber }) == 1;
		
    }


    @Override
    public boolean updateAccountTrnsaction(long accountNumber, int amount) {
        return template.update("insert into BANK.TRANSACTION_DETAILS(ACCOUNT_NUMBER,TRANSACTION_TIME,TRANSACTION_AMOUNT) values(?,?,?)", new Object[] { accountNumber, new Date(), amount }) == 1;
    }

    @Override
    public boolean verifyAccountWithPin(long accountNumber, int pin) {
    	return template.queryForObject("select count(*) as ACCOUNT_COUNT from BANK.ACCOUNT where ACCOUNT_NUMBER=? AND  PIN=?" , new Object[] { accountNumber, pin}, new RowMapper<Integer>() {

            public Integer mapRow(ResultSet rowSet, int arg1) throws SQLException {

                return rowSet.getInt("ACCOUNT_COUNT");
            }
        }) == 1;
    }

    @Override
    public double fetchATMBalance() {
		return template.queryForObject("SELECT ATM_AMOUNT FROM BANK.ATM_BALANCE", Double.class);
	}

	@Override
	public boolean updateAtmBalance(double amount) {
		String sql = "update BANK.ATM_BALANCE set ATM_AMOUNT=ATM_AMOUNT-?";
    	return template.update(sql,new Object[] { amount}) == 1;
	}
	

	@Override
	public synchronized List<Denomination> dispenseMoney(int amount) {
		
		List<Denomination> denominationResult = new ArrayList<>();
		return calculateWithAllDenomination(amount, denominationResult);
	}

	
	private List<Denomination> calculateWithAllDenomination(int amount, List<Denomination> denominationResult) {
		
		int remainingAmt = 0;
		
		TreeSet<Denomination> denominationAll = getApplicationData().getAtmMachine().getDenominationAll();
		Iterator<Denomination> iterator = denominationAll.iterator();
		
		Denomination updatedDenomination = null;	// here new quantity will be updated, after calculation
		
		while (iterator.hasNext()) {
			Denomination currentDenomination = iterator.next();
			updatedDenomination = currentDenomination;
			if (amount >= currentDenomination.getNoteValue() && currentDenomination.getNoteQuantity() > 0) {
				remainingAmt = calculateWithDenomination(amount, currentDenomination, updatedDenomination, denominationResult);
				break;
			}
		}
		
		// Update repository
		denominationAll.add(updatedDenomination);
		getApplicationData().getAtmMachine().setDenominationAll(denominationAll);
		
		// recursion
		if (remainingAmt > 0) {
			calculateWithAllDenomination(remainingAmt, denominationResult);	
		}
		
		return denominationResult;
	}
	
	private int calculateWithDenomination(int amount, Denomination currentDenomination, Denomination updatedDenomination, List<Denomination> denominationResult) {
		
		Denomination denominationToBeGiven = null;
		
		int remainingAmt = amount % currentDenomination.getNoteValue();
		int noteQuantityRequired = amount / currentDenomination.getNoteValue();
		int noteQuantityToBeGiven = 0;
		
		if (noteQuantityRequired > currentDenomination.getNoteQuantity()) {
			noteQuantityToBeGiven = currentDenomination.getNoteQuantity();
			remainingAmt = amount - (currentDenomination.getNoteQuantity() * currentDenomination.getNoteValue());
		} else {
			noteQuantityToBeGiven = noteQuantityRequired;
		}
		
		// Preparation for repository update
		updatedDenomination.setNoteQuantity(currentDenomination.getNoteQuantity() - noteQuantityToBeGiven);
		
		// Update Result
		denominationToBeGiven = new Denomination(currentDenomination.getNoteValue(), noteQuantityToBeGiven);
		denominationResult.add(denominationToBeGiven);
		
		return remainingAmt;
	}

	@Override
	public boolean deposit(long accountNumber, int pin, int amount, int overdraft) {
		return template.update("insert into BANK.ACCOUNT(ACCOUNT_NUMBER, PIN, OPENING_BALANCE, OVERDRAFT) values(?,?,?,?)", new Object[] { accountNumber,pin ,amount, overdraft }) == 1;
	}

	// Datasource setup for Test class
//	@Autowired
	public void setDataSource(DataSource dataSource) {
		template = new JdbcTemplate(dataSource);
//		this.dataSource = dataSource;
    }

    public int getCountOfAccounts() {
        return template.queryForObject("SELECT COUNT(*) FROM BANK.ACCOUNT", Integer.class);
    }
    
//    @PostConstruct
//    public void postConstruct() {
//    	template = new JdbcTemplate(dataSource);
//    }

}
