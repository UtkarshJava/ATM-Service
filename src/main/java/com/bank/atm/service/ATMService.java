package com.bank.atm.service;

import com.bank.atm.response.ATMRequestResult;

public interface ATMService {

	ATMRequestResult checkBalance(long accountNumber, int pin);

	ATMRequestResult withdraw(long accountNumber, int pin, int amount);

	void validateCustomerAccount(String accountNumber, int amount);

}
