package com.bank.atm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.atm.constant.ATMConstants;
import com.bank.atm.constant.ATMErrorKeys;
import com.bank.atm.dao.AccountRepositoryDao;
import com.bank.atm.exception.ATMException;
import com.bank.atm.exception.AccountException;
import com.bank.atm.exception.ValidationException;
import com.bank.atm.model.Denomination;
import com.bank.atm.response.ATMRequestResult;
import com.bank.atm.response.AccountResult;

@Service
public class ATMServiceImpl implements ATMService {

	@Autowired
	private AccountRepositoryDao accountRepository;

	public ATMRequestResult checkBalance(long accountNumber, int pin) {
		return accountRepository.getBalance(accountNumber, pin);
	}

	
	/**
	 * Below steps will be executed, in order to withdraw money from ATM/Account.
	 * 
	 * Is ATM working?
	 * Validate amount - in terms of ATM note value
	 * Get customer balance
	 * Check withdrawal amount is in multiple of 5
	 * Check withdrawal amount against ATM machine amount
	 * Check overdraft balance, if needed
	 * Deduct money from user account
	 * Update account transaction details for audits
	 * Update ATM machine balance
	 * Dispense money, with notes count
	 * 
	 */
	@Transactional
	public ATMRequestResult withdraw(long accountNumber, int pin, int amount) {
		ATMRequestResult atmResult = null;
		double atmBalance = accountRepository.fetchATMBalance();
		System.out.println("ATM Machine current balance is: " + atmBalance);
		atmResult = accountRepository.getBalance(accountNumber, pin);
		long balance = atmResult.getCustomerResult().getBalance();
		if (amount % 5 == 0) {
			if (atmBalance >= amount) {
				if (balance >= amount) {
					Boolean result = accountRepository.withdraw(accountNumber, amount);
					if (result) {
						result = accountRepository.updateAccountTrnsaction(accountNumber, -amount);
						result = accountRepository.updateAtmBalance(amount);
						System.out.println("New ATM Machine balance after trx is: " + accountRepository.fetchATMBalance());
						List<Denomination> denominationResult = accountRepository.dispenseMoney(amount);
						atmResult = accountRepository.getBalance(accountNumber, pin);
						AccountResult accountResult = new AccountResult();
						accountResult.setAccountNumber(String.valueOf(accountNumber));
						accountResult.setBalance(atmResult.getCustomerResult().getBalance());
						accountResult.setOverdraft(atmResult.getCustomerResult().getOverdraft()); // whether to show it or not, in output
						accountResult.setWithdrawnAmount(String.valueOf(amount));
						accountResult.setDenominationResult(denominationResult);
						atmResult.setCustomerResult(accountResult);
						return atmResult;

					} else {
						throw new ATMException(ATMErrorKeys.ATM_OUT_OF_SERVICE.getErrorKey());
					}

				} else {
					throw new AccountException(ATMErrorKeys.ACCOUNT_INSUFFICIENT_BALANCE.getErrorKey());
				}
			} else {
				throw new ATMException(ATMErrorKeys.ATM_INSUFFICIENT_BALANCE.getErrorKey());
			}
		} else {
			throw new ATMException(ATMErrorKeys.BAD_REQUEST_INVALID_AMOUNT.getErrorKey());
		}
	}
	 

	@Override
	public void validateCustomerAccount(String accountNumber, int pin) {
		if (validateAccountNumber(accountNumber) && validatePin(pin)) {
			boolean isPinValid = false;
			try {
				isPinValid = accountRepository.verifyAccountWithPin(Long.parseLong(accountNumber), pin);
			} catch (Exception e) {
				throw new ValidationException(ATMErrorKeys.BAD_REQUEST.getErrorKey(), e);
			}

			if (!isPinValid) {
				throw new ValidationException(ATMErrorKeys.AUTHENTICATION_ERROR.getErrorKey());
			}
		}
	}

	private boolean validateAccountNumber(String accountNumber) {
		if (accountNumber.length() != ATMConstants.ACCOUNT_NUMBER_LENGTH) {
			throw new ValidationException(ATMErrorKeys.BAD_REQUEST.getErrorKey());
		} else {
			return true;
		}
	}

	private boolean validatePin(int pin) {
		String pinStr = pin + "";
		if (pinStr.length() != ATMConstants.ACCOUNT_PIN_LENGTH) {
			throw new ValidationException(ATMErrorKeys.BAD_REQUEST.getErrorKey());
		} else {
			return true;
		}
	}

}
