package com.henrytran1803.BEBakeManage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = {"com.henrytran1803.BEBakeManage"})
public class BeBakeManageApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeBakeManageApplication.class, args);
	}
}

