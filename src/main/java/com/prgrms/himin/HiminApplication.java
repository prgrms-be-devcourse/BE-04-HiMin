package com.prgrms.himin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@ConfigurationPropertiesScan("com.prgrms.himin.global.config.security")
public class HiminApplication {

	public static void main(String[] args) {
		SpringApplication.run(HiminApplication.class, args);
	}

}
