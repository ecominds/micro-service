/**
 * @author	: Rajiv Kumar
 * @project	: boot-rest-api
 * @since	: 0.0.2
 * @date	: 07-Jan-2023
 */

package com.github.ecominds.api.rest.service;

import java.util.List;
import java.util.Optional;

import com.github.ecominds.api.rest.entity.Department;
import com.github.ecominds.api.rest.exception.AppException;

public interface IDepartmentService {
	Department create(Department entity) throws AppException;
	Department update(Department entity) throws AppException;
	
	List<Department> findAll();
	Optional<Department> findById(Long recordId);
	Optional<Department> findByCode(String code);
	
	Optional<Department> delete(Long recordId);
}