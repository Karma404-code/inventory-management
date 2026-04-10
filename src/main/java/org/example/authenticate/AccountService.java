package org.example.authenticate;

import java.sql.PreparedStatement;

public class AccountService {
    static AccountService accountService;
    AccountRepository accountRepository;

    AccountService() {
        accountRepository = new AccountRepository();
    }

    public static AccountService getInstance() {
        if (accountService == null) {
            accountService = new AccountService();
        }
        return accountService;
    }

    public boolean authenticate(String username, String password) {
        return accountRepository.authenticate(username, password);
    }

}
