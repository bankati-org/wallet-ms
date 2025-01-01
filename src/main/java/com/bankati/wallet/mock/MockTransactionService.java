package com.bankati.wallet.mock;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MockTransactionService {
    public boolean isTransactionValid(Long userId, BigDecimal amount) {
        // Mock simple de validation de transaction
        return amount != null && amount.compareTo(BigDecimal.ZERO) != 0;
    }
}