package com.bank.atm.dao;

import java.util.List;

import com.bank.atm.model.Denomination;
import com.bank.atm.response.ATMRequestResult;

public interface AccountRepositoryDao {

	ATMRequestResult getBalance(long accountNumber, int pin);

	boolean withdraw(long accountNumber, int amount);

	boolean updateAccountTrnsaction(long accountNumber, int amount);

    boolean verifyAccountWithPin(long accountN, int pin);

	double fetchATMBalance();

	boolean updateAtmBalance(double i);
	
	public List<Denomination> dispenseMoney(int amount);
	
	boolean deposit(long accountNumber, int pin, int amount, int overdraft);


}
