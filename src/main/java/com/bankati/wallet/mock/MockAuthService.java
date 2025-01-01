package com.bankati.wallet.mock;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class MockAuthService {
    public boolean isUserValid(Long userId) {
        // Mock simple de validation d'utilisateur
        return userId != null && userId > 0;
    }
}



