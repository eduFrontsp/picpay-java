package com.picpay.picpaytest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@EnableJdbcAuditing
@SpringBootApplication
public class PicpayTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(PicpayTestApplication.class, args);
	}

}
