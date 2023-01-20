/**
 * @author	: Rajiv Kumar
 * @project	: boot-rest-api
 * @since	: 0.0.2
 * @date	: 07-Jan-2023
 */

package com.github.ecominds.api.rest.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.ecominds.api.rest.dao.IDepartmentDAO;
import com.github.ecominds.api.rest.entity.Department;
import com.github.ecominds.api.rest.exception.AppException;
import com.github.ecominds.api.rest.service.IDepartmentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DepartmentServiceImpl implements IDepartmentService {

	@Autowired
	private IDepartmentDAO repo;

	@Override
	public Department create(Department entity) throws AppException {
		log.info("Executing create entity");

		setupCode(entity);

		entity.setRecordId(0L); // Let the framework generate the recordId

		Optional<Department> savedEnity = repo.findByCode(entity.getCode());
		if (savedEnity.isPresent()) {
			throw new AppException("Entity already exists with given CODE: " + entity.getCode());
		}
		entity.setActive(true);
		return repo.save(entity);
	}

	@Override
	public Department update(Department entity) throws AppException {
		log.info("Executing update entity");
        if(entity.getRecordId() == null || entity.getRecordId() <= 0) {
        	throw new AppException("Invalid entity details. " + entity);
        }
        
        setupCode(entity);
        
        Optional<Department> savedEntityOpt = repo.findByCode(entity.getCode());
        
        if(savedEntityOpt.isPresent() 
        		&& savedEntityOpt.get().getRecordId() != entity.getRecordId()) {
        	throw new AppException("Another entity already exists with given CODE: " + entity.getCode());
        }
        return repo.save(entity);
	}

	@Override
	public List<Department> findAll() {
		log.info("Executing findAll");
        return repo.findAll();
	}

	@Override
	public Optional<Department> findById(Long recordId) {
		log.info("Executing findById");
        return repo.findById(recordId);
	}

	@Override
	public Optional<Department> findByCode(String code) {
		log.info("Executing findByCode");
        return repo.findByCode(code);
	}

	@Override
	public Optional<Department> delete(Long recordId) {
		return findById(recordId).map(entity -> {
			repo.deleteById(entity.getRecordId());
			return entity;
		});
	}
	
	private void setupCode(Department entity)throws AppException {
		if(entity.getCode() == null || entity.getCode().trim().length() < 1) {
        	throw new AppException("code must not be NULL");
        }
        
        entity.setCode(entity.getCode().toUpperCase());
	}
}