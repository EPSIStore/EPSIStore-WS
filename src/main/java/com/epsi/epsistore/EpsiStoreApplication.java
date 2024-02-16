package com.epsi.epsistore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.epsi.core", "com.epsi.epsistore"})
public class EpsiStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(EpsiStoreApplication.class, args);
	}

}
