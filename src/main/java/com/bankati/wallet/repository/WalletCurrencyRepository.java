package com.bankati.wallet.repository;

import com.bankati.wallet.model.CurrencyType;
import com.bankati.wallet.model.WalletCurrency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletCurrencyRepository extends JpaRepository<WalletCurrency, Long> {
    List<WalletCurrency> findByCurrencyType(CurrencyType currencyType);
}
