package com.bank.atm.model;

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix="com.atm.resourcemappings")
public class ATM {

	private TreeSet<Denomination> denominationAll;
	
	private List<Withdrawal> withdrawalsAll;
	
	public TreeSet<Denomination> getDenominationAll() {
		return denominationAll;
	}

	public void setDenominationAll(TreeSet<Denomination> denominationAll) {
		this.denominationAll = denominationAll;
	}

	public long getBalanceMoney() {
		
		long balanceMoney = 0;
		Iterator<Denomination> iterator = this.denominationAll.iterator();
		
		while (iterator.hasNext()) {
			Denomination d = iterator.next();
			balanceMoney += (d.getNoteValue() * d.getNoteQuantity()); 
		}
		
		return balanceMoney;
	}

	public List<Withdrawal> getWithdrawalsAll() {
		return withdrawalsAll;
	}

	public void setWithdrawalsAll(List<Withdrawal> withdrawalsAll) {
		this.withdrawalsAll = withdrawalsAll;
	}

}
