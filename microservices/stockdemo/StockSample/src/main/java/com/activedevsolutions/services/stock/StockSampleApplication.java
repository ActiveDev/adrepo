package com.activedevsolutions.services.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class StockSampleApplication {

	public static void main(String[] args) {
        SpringApplication stockSampleService = new SpringApplication(StockSampleApplication.class);
        stockSampleService.addListeners(new ApplicationPidFileWriter("stock-sample-service.pid"));
        stockSampleService.run(args);		
	}
}
