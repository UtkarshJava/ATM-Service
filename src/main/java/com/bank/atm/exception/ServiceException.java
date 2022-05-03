package com.bank.atm.exception;

public class ServiceException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ServiceException(String messageKey, Throwable t) {
		super(messageKey, t);
	}

}
