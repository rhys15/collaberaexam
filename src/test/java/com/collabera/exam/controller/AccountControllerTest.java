package com.collabera.exam.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.collabera.exam.model.Account;
import com.collabera.exam.model.AccountResponse;
import com.collabera.exam.service.AccountService;

@RunWith(SpringJUnit4ClassRunner.class)
public class AccountControllerTest {
    
    @InjectMocks
    AccountController accountController;
    
    @Mock 
    AccountService accountService;
    AccountControllerTest accountControllerTest;
    @Before
    public void setUp() throws Exception {
        accountControllerTest = new AccountControllerTest();
        accountControllerTest.accountController = mock(AccountController.class);
        accountControllerTest.accountService = mock(AccountService.class);
    }
    
    @Test
    public void getAllAccounts() {
        ArrayList<Account> accounts  = new ArrayList<>();
        Account account = Account.builder().customerNumber(123).build();
        accounts.add(account);
        when(accountService.findAll()).thenReturn(accounts);
        Iterable<Account> result = accountController.getAllAccount();
        assertNotNull(result);
    }
    
    @Test
    public void saveAccountTestSuccess() {
        Account account = Account.builder().customerNumber(123).accountType("S").address1("address").customerEmail("email@email.com").customerMobile("1231245").customerName("test").build();
        accountService.saveOrUpdateAccout(account);
        when(accountService.getAccountById(Mockito.anyInt())).thenReturn(account);
        ResponseEntity<AccountResponse> result = accountController.saveAccount(account);
        assertNotNull(result);
        assertEquals(result.getStatusCode(), HttpStatus.CREATED);
    }
    
    @Test
    public void saveAccountTest_Fail_MissingEmail() {
        Account account = Account.builder().customerNumber(123).accountType("S").address1("address").customerMobile("1231245").customerName("test").build();
        accountService.saveOrUpdateAccout(account);
        when(accountService.getAccountById(Mockito.anyInt())).thenReturn(account);
        ResponseEntity<AccountResponse> result = accountController.saveAccount(account);
        assertNotNull(result);
        assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(result.getBody().getTransactionStatusDescription(),"Email is required field");
    }



}