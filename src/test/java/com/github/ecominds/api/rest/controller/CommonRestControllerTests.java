/**
 * @author	: Rajiv Kumar
 * @project	: boot-rest-api
 * @since	: 0.0.1
 * @date	: 07-Jan-2023
 */

package com.github.ecominds.api.rest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@WebMvcTest(CommonRestController.class)
public class CommonRestControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void indexApiGetTest()throws Exception {
		// perform HTTP request and set the expectations with MockMVC
		mockMvc.perform(get("/"))
			.andExpect(status().isOk())
			.andExpect(content().string("Hello Docker World"))
			.andDo(print());
	}
	
	@Test
	public void homeApiGetTest() throws Exception{
		mockMvc.perform(get("/home"))
		.andExpect(status().isOk())
		.andExpect(content().string("home"))
		.andDo(print());
	}
	
	@Test
	public void calcApiPostTest() throws Exception{
		MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
		paramsMap.add("x", "4");
		paramsMap.add("y", "3");
		
		mockMvc.perform(post("/calc").params(paramsMap))
		.andExpect(status().isOk())
		.andExpect(content().string("7"))
		.andDo(print());
	}
}