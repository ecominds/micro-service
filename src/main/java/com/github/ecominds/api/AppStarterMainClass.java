/**
 * @author	: Rajiv Kumar
 * @project	: boot-rest-api
 * @since	: 0.0.1
 * @date	: 07-Jan-2023
 */

package com.github.ecominds.api;

import javax.annotation.PreDestroy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class AppStarterMainClass {

	public static void main(String[] args) {
		log.info("Starting Application...");
		SpringApplication.run(AppStarterMainClass.class, args);
	}

	@PreDestroy
	public void onExit() {
		log.info("Destroying application context...");
	}
}