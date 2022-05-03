package com.bank.atm.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.bank.atm.dao.AccountRepositoryDao;
import com.bank.atm.dao.AccountRepositoryDaoImpl;
import com.bank.atm.response.ATMRequestResult;

public class ATMServiceTest {
	
	@Autowired
	private ATMService aTMService;
	
	private AccountRepositoryDaoImpl accountRepository;
	
	@Test
	public void test_checkBalance_validAccountNumber() {
		initDatasource();
		ATMRequestResult result = accountRepository.getBalance(111111111, 1441);
		long balance = result.getCustomerResult().getBalance();
		long overdraft = result.getCustomerResult().getOverdraft();
		
		assertEquals(500,balance);
		assertEquals(50,overdraft);
		
	}
	
	@Test
	public void test_verifyAccountWithPin() {
		initDatasource();
		boolean result = accountRepository.verifyAccountWithPin(111111111, 1231);
				
		assertFalse(result);
		
	}
	
	@Test
	public void test_withdraw() {
		initDatasource();
		boolean result = accountRepository.withdraw(111111111, 100);
				
		assertFalse(result);
		
	}
	
	private AccountRepositoryDaoImpl initDatasource() {
		DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
			      .addScript("classpath:scripts/create/account_test.sql")
			      .addScript("classpath:scripts/create/test-data.sql")
			      .build();
		
		accountRepository = new AccountRepositoryDaoImpl();
		accountRepository.setDataSource(dataSource);
		return accountRepository;
	}

}
