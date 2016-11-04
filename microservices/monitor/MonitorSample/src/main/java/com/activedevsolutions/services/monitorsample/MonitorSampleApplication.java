package com.activedevsolutions.services.monitorsample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableEurekaServer
@EnableHystrixDashboard
public class MonitorSampleApplication {
	public static void main(String[] args) {
		SpringApplication eurekaServer = new SpringApplication(MonitorSampleApplication.class);
		eurekaServer.addListeners(new ApplicationPidFileWriter("eureka-server.pid"));
		eurekaServer.run();
	}
}
