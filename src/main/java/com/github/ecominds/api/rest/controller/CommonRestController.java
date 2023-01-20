/**
 * @author	: Rajiv Kumar
 * @project	: boot-rest-api
 * @since	: 0.0.1
 * @date	: 07-Jan-2023
 */

package com.github.ecominds.api.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CommonRestController {
	
	@GetMapping("/")
	public String index() {
		log.info("Root path accessed");
		return "Hello Docker World";
	}
	
	@GetMapping("/home")
	public String home() {
		log.info("Home path accessed");
		return "home";
	}
	
	@PostMapping("/calc")
	public int calc(int x, int y) {
		log.info("Calc executed X={}, Y={}", x, y);
		return (x + y);
	}
}