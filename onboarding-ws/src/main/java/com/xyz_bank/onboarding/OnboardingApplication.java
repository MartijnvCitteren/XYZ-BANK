package com.xyz_bank.onboarding;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log4j2
public class OnboardingApplication {
	public static void main(String[] args) {
		log.info("Starting Onboarding Application");
		SpringApplication.run(OnboardingApplication.class, args);
	}
}
