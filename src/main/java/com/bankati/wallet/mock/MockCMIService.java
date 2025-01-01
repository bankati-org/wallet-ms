package com.bankati.wallet.mock;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MockCMIService {
    public BigDecimal getInitialBalance(Long userId) {
        // Mock de récupération de solde initial
        return new BigDecimal("1000.00");
    }
}