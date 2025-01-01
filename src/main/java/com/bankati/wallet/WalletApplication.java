package com.bankati.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients
@SpringBootApplication(scanBasePackages = "com.bankati.wallet")
@EntityScan(basePackages = "com.bankati.wallet.model")
@EnableJpaRepositories(basePackages = "com.bankati.wallet.repository")
@ConfigurationPropertiesScan("com.bankati.wallet")
public class WalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletApplication.class, args);
	}

}
