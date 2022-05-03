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
public class PostWithdrawMoneyController {

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
	 * Below steps will be executed, in order to withdraw balance from ATM/Account.
	 * 
	 * Is ATM working?
	 * validateCustomerAccount check account number is valid, atm pin is valid and atm pin is mapped to same account
	 * getMoney service will be used to withdraw amount requested by user account with valid pin provided
	 * 
	 */

	@RequestMapping(value = ATMEndPoints.ATM_POST_CASH_WITHDRAWAL_ENDPOINT, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ATMRequestResult> postWithdrawMoney(@RequestParam("accountNumber") String accountNumber,
			@RequestParam("pin") String pin, @RequestParam("requestAmount") int requestAmount) {

		System.out.println("WithdrawMoney from accountNumber: " + accountNumber + " and requestAmount is: " + requestAmount);

		ATMRequestResult requestResult = new ATMRequestResult();

		requestResult = getMoney(accountNumber, pin, requestAmount);

		ResponseEntity<ATMRequestResult> response = getControllerHelper().createResponse(requestResult);

		System.out.println("Exit from WithdrawMoney");

		return response;
	}

	private ATMRequestResult getMoney(String accountNumber, String pin, int requestAmount) {

		System.out.println("Inside WithdrawMoneyService execution");
		ATMRequestResult requestResult = null;

		try {

			atmService.validateCustomerAccount(accountNumber, Integer.valueOf(pin));

			requestResult = atmService.withdraw(Long.parseLong(accountNumber), Integer.valueOf(pin), requestAmount);

		} catch (Exception e) {
//			e.printStackTrace();

			requestResult = prepareErrorResponse(e.getMessage());
		}
		System.out.println("Exit WithdrawMoneyService execution");

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
