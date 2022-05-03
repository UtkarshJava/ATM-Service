package com.bank.atm.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bank.atm.model.ATMParam;
import com.bank.atm.model.Account;
import com.bank.atm.response.ATMRequestResult;


@Service
public class ControllerHelper {

	/**
	 * Create an object out of input parameters.
	 */
	protected ATMParam createParam(String accountNumber, int pin) {
		
		return createParam(accountNumber, pin, 0);
	}
	
	protected ATMParam createParam(String accountNumber, int pin, int requestAmount) {
		
		ATMParam inputParam = new ATMParam();
		Account account = new Account();
		account.setAccountNumber(accountNumber);
		account.setPin(pin);
		inputParam.setCustomer(account);
		inputParam.setWithdrawalAmount(requestAmount);
		
		return inputParam;
	}
	
	protected ResponseEntity<ATMRequestResult> createResponse(ATMRequestResult requestResult) {
		
		ResponseEntity<ATMRequestResult> response = null;
		if (requestResult.getServiceError() != null) {
			if (requestResult.getServiceError().getErrorCode().startsWith("1")) {
				response = new ResponseEntity<ATMRequestResult>(requestResult, HttpStatus.INTERNAL_SERVER_ERROR);
			} else if (requestResult.getServiceError().getErrorCode().startsWith("2")) {
				response = new ResponseEntity<ATMRequestResult>(requestResult, HttpStatus.BAD_REQUEST);
			} else if (requestResult.getServiceError().getErrorCode().startsWith("3")) {
				response = new ResponseEntity<ATMRequestResult>(requestResult, HttpStatus.UNAUTHORIZED);
			}
		} else {
			response = new ResponseEntity<ATMRequestResult>(requestResult, HttpStatus.OK);
		}
		return response;
	}
}
