package com.bank.atm.response;

import java.util.ResourceBundle;

import org.springframework.stereotype.Component;

@Component
public class ErrorMessageProvider {

	private ResourceBundle messageBundle;

	public ErrorMessageProvider(String messageFilePath) {
		messageBundle = ResourceBundle.getBundle(messageFilePath);
	}
	
	public ResourceBundle getMessageBundle() {
		return messageBundle = ResourceBundle.getBundle("properties.errorMessages");
	}

	public ErrorMessageProvider() {
		
	}

	public String getErrorMessageForKey(String key) {
		return getMessageBundle().getString(key);
	}
	
}
