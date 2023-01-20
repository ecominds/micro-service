/**
 * @author	: Rajiv Kumar
 * @project	: boot-rest-api
 * @since	: 0.0.2
 * @date	: 07-Jan-2023
 */

package com.github.ecominds.api.rest.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.ecominds.api.rest.entity.Department;

@Repository
public interface IDepartmentDAO extends JpaRepository<Department, Long>{
	Optional<Department> findByCode(String code);
}