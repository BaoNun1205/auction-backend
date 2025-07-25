package com.example.auction_web;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AuctionWebApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("AZURE_API_KEY", dotenv.get("AZURE_API_KEY"));
		System.setProperty("AZURE_ENDPOINT", dotenv.get("AZURE_ENDPOINT"));
		System.setProperty("DEPLOYMENT_NAME", dotenv.get("DEPLOYMENT_NAME"));
		System.setProperty("AZURE_API_KEY_EMBEDDING", dotenv.get("AZURE_API_KEY_EMBEDDING"));
		System.setProperty("AZURE_ENDPOINT_EMBEDDING", dotenv.get("AZURE_ENDPOINT_EMBEDDING"));
		System.setProperty("DEPLOYMENT_NAME_EMBEDDING", dotenv.get("DEPLOYMENT_NAME_EMBEDDING"));
		SpringApplication.run(AuctionWebApplication.class, args);
	}

}
