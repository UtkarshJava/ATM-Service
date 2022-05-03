package com.bank.atm.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.atm.constant.ATMEndPoints;
import com.bank.atm.response.ATMRequestResult;
import com.bank.atm.response.ErrorMessageProvider;
import com.bank.atm.response.ServiceError;
import com.bank.atm.service.ATMService;

@RestController
@RequestMapping("/atm")
public class GetBalanceController  {

	
	@Autowired
	private ATMService atmService;
	@Autowired
	@Qualifier("controllerHelper")
	private ControllerHelper controllerHelper;
	
	public ControllerHelper getControllerHelper() {
		return controllerHelper;
	}

	@Autowired
	public void setControllerHelper(ControllerHelper controllerHelper) {
		this.controllerHelper = controllerHelper;
	}
	private ErrorMessageProvider errorMessagesProvider;
	
	public ErrorMessageProvider getErrorMessagesProvider() {
		return errorMessagesProvider;
	}

	@Autowired
	public void setErrorMessagesProvider(ErrorMessageProvider errorMessagesProvider) {
		this.errorMessagesProvider = errorMessagesProvider;
	}
	
	/**
	 * Below steps will be executed, in order to check balance from ATM/Account.
	 * 
	 * Is ATM working?
	 * validateCustomerAccount check account number is valid, atm pin is valid and atm pin is mapped to same account
	 * getbalance for user account with valid pin provided
	 * 
	 */

	@RequestMapping(value = ATMEndPoints.ATM_GET_BALANCE_ENQUIRY_ENDPOINT, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ATMRequestResult> getBalanceEnquiry(
			@RequestParam("accountNumber") String accountNumber, @RequestParam("pin") String pin) {

		System.out.println("GetBalanceEnquiry for: " + accountNumber);
		ATMRequestResult requestResult = null;

		requestResult = getBalance(accountNumber, pin);

		ResponseEntity<ATMRequestResult> response = getControllerHelper().createResponse(requestResult);

		System.out.println("Exit GetBalanceEnquiry");

		return response;
	}


	public ATMRequestResult getBalance(String accountNumber, String pin) {

		System.out.println("Inside getBalanceService execution");
		ATMRequestResult requestResult = null;

		try {

			atmService.validateCustomerAccount(accountNumber, Integer.valueOf(pin));
			
			requestResult = atmService.checkBalance(Long.parseLong(accountNumber), Integer.valueOf(pin));

		} catch (Exception e) {
//			e.printStackTrace();

			requestResult = prepareErrorResponse(e.getMessage());
		}
		System.out.println("Exit getBalanceService execution");

		return requestResult;
	}

	private ATMRequestResult prepareErrorResponse(String errorCode) {
		
		ATMRequestResult result = new ATMRequestResult();
		String errorMessageForKey = getErrorMessagesProvider().getErrorMessageForKey(errorCode);
		ServiceError serviceError = new ServiceError(errorCode, errorMessageForKey);
		result.setServiceError(serviceError);
		
		return result;
	}

}
