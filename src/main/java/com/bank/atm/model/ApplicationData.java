package com.bank.atm.model;

import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;


public class ApplicationData {

	private ATM atmMachine;
	
	private static TreeSet<Denomination> denominationAll = null;
	
	public ATM getAtmMachine() {
		return atmMachine;
	}

	// ATM initialization
	@Autowired
	public void setAtmMachine(ATM atmMachine) {
		
		atmMachine.setDenominationAll(getDenominationAll());
		
		this.atmMachine = atmMachine;
	}

	/**
	 * TODO - to be removed
	 * Properties not being loaded as a List from properties file.
	 * So temporarily setting from here.
	 */
	private static TreeSet<Denomination> getDenominationAll() {
		
		if (denominationAll == null) {
			denominationAll = new TreeSet<Denomination>();
			denominationAll.add(new Denomination(50, 10));
			denominationAll.add(new Denomination(20, 30));
			denominationAll.add(new Denomination(10, 30));
			denominationAll.add(new Denomination(5, 20));
		}
		return denominationAll;
	}

}
