package com.bank.atm.constant;

public enum ATMErrorKeys {

	ATM_INSUFFICIENT_BALANCE("101"),
	ATM_OUT_OF_SERVICE("102"),
	ATM_MANAGED_ERROR("103"),
	BAD_REQUEST("201"),
	AUTHENTICATION_ERROR("202"),
	BAD_REQUEST_INVALID_AMOUNT("203"),
	ACCOUNT_INSUFFICIENT_BALANCE("301"),
	ACCOUNT_DAILY_LIMIT_WITHDRAWAL_REACHED("302");
	
	private String errorKey;

	private ATMErrorKeys(String errorKey) {
		this.errorKey = errorKey;
	}

	public String getErrorKey() {
		return errorKey;
	}
	
}
