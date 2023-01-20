/**
 * @author	: Rajiv Kumar
 * @project	: boot-rest-api
 * @since	: 0.0.2
 * @date	: 07-Jan-2023
 */

package com.github.ecominds.api.rest.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ecominds.api.rest.entity.Department;
import com.github.ecominds.api.rest.service.IDepartmentService;
import com.github.ecominds.api.test.DummyDataProvider;

@WebMvcTest(DepartmentApiController.class)
public class DepartmentApiControllerTests extends DummyDataProvider {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private IDepartmentService service;

	@Autowired
	private ObjectMapper objectMapper;

	private static final String API_BASE_PATH = "/api/departments";

	@DisplayName("JUnit test for REST end point - Create Department")
	@Test
	public void givenEntity_whenCreated_thenReturnSaved() throws Exception {
		// given - precondition or setup
		Department entity = buildDummyCol().get(0);

		given(service.create(any(Department.class))).willAnswer((invocation) -> invocation.getArgument(0));

		// when - action or behavior that we are going test
		ResultActions response = mockMvc.perform(
				post(API_BASE_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(entity)));

		// then - verify the result or output using assert statements
		response
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.name", is(entity.getName())))
			.andExpect(jsonPath("$.code", is(entity.getCode())));
	}

	@DisplayName("POSITIVE - JUnit test for REST end point - Find All")
	@Test
	public void givenEntityCol_whenFindAll_thenReturnAllEntities() throws Exception {
		// given - precondition or setup
		List<Department> deptCol = buildDummyCol();
		given(service.findAll()).willReturn(deptCol);

		ResultActions response = mockMvc.perform(get(API_BASE_PATH));

		response
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.size()", is(deptCol.size())));
	}
	
	@DisplayName("NEGATIVE - JUnit test for REST end point - Find All")
	@Test
	public void givenEmptyCol_whenFindAll_thenReturnEmpty() throws Exception {
		// given - precondition or setup
		List<Department> entityCol = Collections.emptyList();
		given(service.findAll()).willReturn(entityCol);

		ResultActions response = mockMvc.perform(get(API_BASE_PATH));

		response
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.size()", is(0)));
	}
	
	@DisplayName("POSITIVE - JUnit test for REST end point - findById")
	@Test
	public void givenEntityId_whenFindById_thenReturnEntity() throws Exception {
		// given - precondition or setup
		long recordId = 1L;
		Department entity = buildDummyCol().get(0);

		given(service.findById(recordId)).willReturn(Optional.of(entity));

		ResultActions response = mockMvc.perform(get(API_BASE_PATH + "/{deptId}", recordId));

		response
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.name", is(entity.getName())))
			.andExpect(jsonPath("$.code", is(entity.getCode())));
	}
	
	@DisplayName("NEGATIVE - JUnit test for REST end point - findById")
	@Test
	public void givenInvalidId_whenFindById_thenReturnEmpty() throws Exception {
		// given - precondition or setup
		long recordId = 1L;
		given(service.findById(recordId)).willReturn(Optional.empty());

		ResultActions response = mockMvc.perform(get(API_BASE_PATH + "/{deptId}", recordId));

		response
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	@DisplayName("POSITIVE - JUnit test for REST end point - Update")
	@Test
	public void givenEntityId_whenUpdateEntity_thenReturnUpdatedEntity() throws Exception {
		long recordId = 1L;

		Department savedEntity = buildDummyCol().get(0);
		given(service.findById(recordId)).willReturn(Optional.of(savedEntity));

		given(service.update(any(Department.class))).willAnswer(invocation -> invocation.getArgument(0));

		Department entityToUpdate = buildDummy("Finanace IT - Updated", "FINIT", null);

		ResultActions response = mockMvc.perform(
				put(API_BASE_PATH + "/{deptId}", recordId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(entityToUpdate)));

		response
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.name", is(entityToUpdate.getName())))
			.andExpect(jsonPath("$.code", is(entityToUpdate.getCode())));
	}

	@DisplayName("NEGATIVE - JUnit test for REST end point - Update")
	@Test
	public void givenInvalidId_whenUpdateEntity_thenReturn400() throws Exception {
		long recordId = 1L;
		given(service.findById(recordId)).willReturn(Optional.empty());

		given(service.update(any(Department.class))).willAnswer(invocation -> invocation.getArgument(0));

		Department entityToUpdate = buildDummy("Finanace IT - Updated", "FINIT", null);

		ResultActions response = mockMvc.perform(
				put(API_BASE_PATH + "/{deptId}", recordId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(entityToUpdate)));

		response
			.andExpect(status().isBadRequest())
			.andDo(print());
	}
	
	@DisplayName("UPDATE - JUnit test for REST end point - Delete")
	@Test
	public void givenEntityId_whenDeleteEntity_thenReturn200() throws Exception {
		long recordId = 1L;

		Department savedEntity = buildDummyCol().get(0);
		given(service.delete(recordId)).willReturn(Optional.of(savedEntity));

		ResultActions response = mockMvc.perform(delete(API_BASE_PATH + "/{deptId}", recordId));
		response
			.andExpect(status().isOk())
			.andDo(print());
	}

	@DisplayName("NEGATIVE - JUnit test for REST end point - Delete")
	@Test
	public void givenDeptId_whenDeleteDepartment_thenReturn400() throws Exception {
		long recordId = 1L;

		given(service.findById(recordId)).willReturn(Optional.empty());

		ResultActions response = mockMvc.perform(delete(API_BASE_PATH + "/{deptId}", recordId));
		response
			.andExpect(status().isBadRequest())
			.andDo(print());
	}
}