package com.collabera.exam.resource;

import org.springframework.data.repository.CrudRepository;

import com.collabera.exam.model.Account;

public interface AccountRepository extends CrudRepository<Account, Integer> {}