package com.bank.atm.exception;

public class ATMException extends ServiceException {

	private static final long serialVersionUID = 1L;
	
	public ATMException(String messageKey, Throwable t) {
		super(messageKey, t);
	}
	
	public ATMException(String messageKey) {
		super(messageKey, null);
	}

}
