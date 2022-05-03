package com.bank.atm.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import javax.script.ScriptException;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import com.bank.atm.model.Account;
import com.bank.atm.response.ATMRequestResult;
import com.bank.atm.service.ATMServiceImpl;

@SpringApplicationConfiguration
@RunWith(MockitoJUnitRunner.class)
public class AccountRepositoryDaoTest {
	
//	@Autowired
//	private JdbcTemplate jdbcTemplate;

	
	@Autowired
	private AccountRepositoryDaoImpl customerAccountDao;
	
	@Test
	public void test_inValidAccountNumber() {
		
//		ATMRequestResult requestResult = customerAccountDao.getBalance(121212121, 1210);
	}
	
	@Test
	public void whenInjectInMemoryDataSource_thenReturnCorrectAccountCount() {

	    assertEquals(2, customerAccountDao.getCountOfAccounts());
	}

	@Test
	public void test_fetchCustomerBalance_validAccountNumb() {
		
		
//		ATMRequestResult requestResult = atmMachineRequestManager.checkBalance(121212121, 1212);
		ATMRequestResult requestResult = customerAccountDao.getBalance(121212121, 1212);
		long balance = requestResult.getCustomerResult().getBalance();
		assertEquals(800,balance);
		
		
	}
	
	@Test
	public void test_Withdraw_validAccountNumb() {
		
		
//		ATMRequestResult requestResult = atmMachineRequestManager.checkBalance(121212121, 1212);
		boolean requestResult = customerAccountDao.withdraw(121212121, 12);
		
		assertTrue(requestResult);
		
		
	}

	
	@Bean(destroyMethod = "shutdown")
	@Before
	public void initDatasource() {
		DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
			      .addScript("classpath:scripts/create/account_test.sql")
			      .addScript("classpath:scripts/create/test-data.sql")
			      .build();
		
		customerAccountDao = new AccountRepositoryDaoImpl();
		customerAccountDao.setDataSource(dataSource);
//		customerAccountDao.postConstruct();
	}
	
	@Bean
	public void cleanDB() {
		DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
			      .addScript("classpath:scripts/create/dropaccount_test.sql")
			      .build();
		customerAccountDao.setDataSource(dataSource);
	}

}
