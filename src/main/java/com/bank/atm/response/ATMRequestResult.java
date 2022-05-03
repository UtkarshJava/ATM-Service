package com.bank.atm.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class ATMRequestResult {

	private ServiceError error;
	
//	@JsonProperty("serviceResponse")
	private AccountResult accountResult;
	

	public AccountResult getCustomerResult() {
		return accountResult;
	}

	public void setCustomerResult(AccountResult accountResult) {
		this.accountResult = accountResult;
	}

	public ServiceError getServiceError() {
		return error;
	}

	public void setServiceError(ServiceError error) {
		this.error = error;
	}

}
