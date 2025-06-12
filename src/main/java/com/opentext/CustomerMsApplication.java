package com.opentext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class CustomerMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerMsApplication.class, args);
		System.out.println("Customer MS started successfully.....");
	}
	
	@Bean
	@LoadBalanced // for client side load balancing
	public RestTemplate giveRestTemplate() {
		System.out.println("Returning RestTemplate object");
		return new RestTemplate();
	}

}
