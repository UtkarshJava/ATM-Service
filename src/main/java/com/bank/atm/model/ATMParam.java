package com.bank.atm.model;



public class ATMParam {

	private int withdrawalAmount;
	
	private Account account;

	public int getWithdrawalAmount() {
		return withdrawalAmount;
	}

	public void setWithdrawalAmount(int withdrawalAmount) {
		this.withdrawalAmount = withdrawalAmount;
	}

	public Account getCustomer() {
		return account;
	}

	public void setCustomer(Account account) {
		this.account = account;
	}
	
}
