package com.collabera.exam.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collabera.exam.model.Account;
import com.collabera.exam.resource.AccountRepository;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    public Iterable<Account> findAll() {
        List<Account> students = new ArrayList<>();
        accountRepository.findAll().forEach(students::add);
        return students;
    }

    public Account getAccountById(int id) {
        return accountRepository.findById(id).get();
    }

    public void saveOrUpdateAccout(Account account) {
        accountRepository.save(account);
    }

    public void delete(int id) {
        accountRepository.deleteById(id);
    }
}