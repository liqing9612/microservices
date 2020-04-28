package com.dgn.src.networkwalletservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class NetworkWalletServiceApplication implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(NetworkWalletServiceApplication.class);

	public static void main(String[] args) throws Exception {
		SpringApplication.run(NetworkWalletServiceApplication.class);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("################# The service is running.... ###########");
		//System.exit(0);
	}
}
