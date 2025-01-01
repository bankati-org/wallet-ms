package com.bankati.wallet.repository;

import com.bankati.wallet.model.WalletCurrency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletCurrencyRepository extends JpaRepository<WalletCurrency, Long> {
}
