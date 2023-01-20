/**
 * @author	: Rajiv Kumar
 * @project	: boot-rest-api
 * @since	: 0.0.2
 * @date	: 07-Jan-2023
 */

package com.github.ecominds.api.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.ecominds.api.rest.entity.Department;
import com.github.ecominds.api.rest.exception.AppException;
import com.github.ecominds.api.rest.service.IDepartmentService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/departments")
@Slf4j
public class DepartmentApiController {
	@Autowired
	private IDepartmentService service;
	
	@GetMapping
	public List<Department> findAll() {
		log.info("Request received to find all entities");
		return service.findAll();
	}
	
	@GetMapping("{deptId}")
	public ResponseEntity<Department> findById(@PathVariable Long deptId) {
		log.info("Request received to find entity. DEPT ID : " + deptId);
		return service.findById(deptId)
				.map(ResponseEntity::ok)
				.orElseThrow(() -> new AppException(String.format("No such entity found with DEPT_ID='%s'", deptId)));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Department create(@RequestBody Department enity) {
		log.info("Request received to create entity : " + enity);
		return service.create(enity);
	}
	
	@PutMapping("{deptId}")
	public ResponseEntity<Department> update(@PathVariable Long deptId,
			@RequestBody Department entity) {
		log.info("Request received to update entity. DEPT ID : " + deptId);
		return service.findById(deptId)
				.map(savedEntity -> {
					savedEntity.setName(entity.getName());
					savedEntity.setCode(entity.getCode());
					savedEntity.setRemarks(entity.getRemarks());
					savedEntity.setActive(entity.isActive());
					
					Department updatedEntity = service.update(savedEntity);
					return ResponseEntity.ok(updatedEntity); 
				})
				.orElseThrow(() -> new AppException(String.format("No such entity found with DEPT_ID='%s'", deptId)));
	}
	
	@DeleteMapping("{deptId}")
	public ResponseEntity<String> delete(@PathVariable Long deptId){
		log.info("Request received to delete entity. DEPT ID : " + deptId);
		return service.delete(deptId)
			.map(deletedEntity -> {
				return new ResponseEntity<>(
						String.format("Deleted department NAME='%s'", deletedEntity.getName()), 
						HttpStatus.OK);
			}).orElseThrow(() -> new AppException(String.format("No such entity found with DEPT_ID='%s'", deptId)));
	}
}