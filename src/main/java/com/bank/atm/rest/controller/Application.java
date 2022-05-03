package com.bank.atm.rest.controller;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.bank.atm.model.ATM;
import com.bank.atm.model.ApplicationData;


@ComponentScan(value="com.bank.atm")
@EnableAutoConfiguration
@EnableConfigurationProperties(ATM.class)
@PropertySources({
	@PropertySource("classpath:/properties/errorMessages.properties"),
	@PropertySource("classpath:/properties/application.properties")
})

public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);              
    }
    @Bean
	public ApplicationData applicationData() {
		return new ApplicationData();
	}
}