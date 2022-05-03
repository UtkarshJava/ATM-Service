package com.bank.atm.exception;

public class AccountException extends ServiceException {

	private static final long serialVersionUID = 1L;
	
	public AccountException(String messageKey, Throwable t) {
		super(messageKey, t);
	}
	
	public AccountException(String messageKey) {
		super(messageKey, null);
	}

}
