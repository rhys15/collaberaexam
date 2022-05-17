package com.collabera.exam.controller;

import java.util.regex.Pattern;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.collabera.exam.model.Account;
import com.collabera.exam.model.AccountResponse;
import com.collabera.exam.service.AccountService;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/account")
    private Iterable<com.collabera.exam.model.Account> getAllAccount() {
        return accountService.findAll();
    }

    @GetMapping("/account/{id}")
    private ResponseEntity<AccountResponse> getAccount(@PathVariable("id") int id) {
        try {
            Account account = accountService.getAccountById(id);
            AccountResponse reponse = AccountResponse.builder().account(account).transactionStatusCode(302).transactionStatusDescription("Customer Account found").build();
            return ResponseEntity.status(HttpStatus.FOUND).body(reponse);
        } catch (Exception e) {
            AccountResponse reponse = AccountResponse.builder().transactionStatusCode(404).transactionStatusDescription("Customer Accoun not found").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(reponse);
        }
    }

    @DeleteMapping("/account/{id}")
    private void Account(@PathVariable("id") int id) {
        accountService.delete(id);
    }

    @PostMapping("/account")
    private ResponseEntity<AccountResponse> saveAccount(@RequestBody Account account) {
        AccountResponse validateAccountResponse = validateInput(account);
        if (ObjectUtils.isNotEmpty(validateAccountResponse)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validateAccountResponse);
        }
        accountService.saveOrUpdateAccout(account);
        Account savedAccount = accountService.getAccountById(account.getCustomerNumber());
        AccountResponse response = null;
        if (null != savedAccount) {
            response =  AccountResponse.builder().transactionStatusCode(201).customerNumber(Integer.toString(savedAccount.getCustomerNumber())).transactionStatusDescription("Customer account created").build();
            return ResponseEntity.status(HttpStatus.CONTINUE).body(response);
        }
        response = AccountResponse.builder().transactionStatusCode(400).transactionStatusDescription("error").build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private AccountResponse validateInput(com.collabera.exam.model.Account account) {
        if (StringUtils.isEmpty(account.getCustomerEmail())) {
            return AccountResponse.builder().transactionStatusCode(400).transactionStatusDescription("Email is required field").build();
        } else if (!isValid(account.getCustomerEmail())) {
            return AccountResponse.builder().transactionStatusCode(400).transactionStatusDescription("Invalid Email").build();
        } else if (StringUtils.isEmpty(account.getCustomerName())) {
            return AccountResponse.builder().transactionStatusCode(400).transactionStatusDescription("Name is required field").build();
        } else if (StringUtils.isEmpty(account.getAddress1())) {
            return AccountResponse.builder().transactionStatusCode(400).transactionStatusDescription("Address 1 is required field").build();
        } else if (StringUtils.isEmpty(account.getCustomerMobile())) {
            return AccountResponse.builder().transactionStatusCode(400).transactionStatusDescription("Mobile is required field").build();
        } else if (StringUtils.isEmpty(account.getAccountType()) || !(account.getAccountType().equals("S") || account.getAccountType().equals("C"))) {
            return AccountResponse.builder().transactionStatusCode(400).transactionStatusDescription("Invalid Account Type").build();
        } 
        return null;
    }

    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    public static boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}