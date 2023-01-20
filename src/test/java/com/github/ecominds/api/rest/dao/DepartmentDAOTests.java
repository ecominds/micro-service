/**
 * @author	: Rajiv Kumar
 * @project	: boot-rest-api
 * @since	: 0.0.2
 * @date	: 07-Jan-2023
 */

package com.github.ecominds.api.rest.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.github.ecominds.api.rest.entity.Department;
import com.github.ecominds.api.test.DummyDataProvider;

@DataJpaTest
public class DepartmentDAOTests extends DummyDataProvider{
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private IDepartmentDAO deptRepo;
	
	@Test
	public void injectedDataRepoComponentsAreNotNull() {
		assertThat(dataSource).isNotNull();
		assertThat(jdbcTemplate).isNotNull();
		assertThat(entityManager).isNotNull();
		
		assertThat(deptRepo).isNotNull();
	}
	
	@DisplayName("POSITIVE - JUnit test for save entity method")
	@Test
	public void givenEntity_whenSave_thenReturnSavedEntity(){
		Department entity = buildDummyCol().get(0);
		Department savedEntity = deptRepo.save(entity);
		
		assertThat(savedEntity).isNotNull();
		assertThat(savedEntity.getRecordId()).isGreaterThan(0);
	}
	
	@DisplayName("NEGATIVE - JUnit test for save entity method")
	@Test
	public void givenEmptyEntity_whenSave_thenThrowsException(){
		Department entity = null;
		
		assertThrows(InvalidDataAccessApiUsageException.class, ()->{
			deptRepo.save(entity);
		});
	}
	
	@DisplayName("POSITIVE - JUnit test for findAll method")
	@Test
	public void givenEntityCol_whenFindAll_thenReturnAllDeptCol() {
		List<Department> deptCol = buildDummyCol();
		deptCol.stream().forEach(entity -> {
			deptRepo.save(entity);
		});
		
		List<Department> resultCol = deptRepo.findAll();
		
		assertThat(resultCol).isNotNull();
		assertThat(resultCol.size()).isEqualTo(deptCol.size());
	}
	
	@DisplayName("NEGATIVE - JUnit test for findAll method")
	@Test
	public void givenEmptyl_whenFindAll_thenReturnEmpty() {
		List<Department> resultCol = deptRepo.findAll();
		
		assertThat(resultCol).isNotNull();
		assertThat(resultCol.size()).isEqualTo(0);
	}
	
	@DisplayName("POSITIVE - JUnit test for findById method")
    @Test
    public void givenEntity_whenFindById_thenReturnSavedEntity(){
    	List<Department> deptCol = buildDummyCol();
    	deptCol.stream().forEach(entity -> {
			deptRepo.save(entity);
		});
		
		Optional<Department> entity = deptRepo.findById(deptCol.get(0).getRecordId());
		assertTrue(entity.isPresent());
    }
	
	@DisplayName("NEGATIVE - JUnit test for findById method")
    @Test
    public void givenEmpty_whenFindById_thenReturnEmpty(){
		Long deptId = 1L;
		Optional<Department> entity = deptRepo.findById(deptId);
		assertFalse(entity.isPresent());
    }
	
	@DisplayName("JUnit test for update entity method")
    @Test
    public void givenEntityCol_whenUpdateEntity_thenReturnUpdatedEntity(){
    	List<Department> deptCol = buildDummyCol();
    	deptCol.stream().forEach(entity -> {
			deptRepo.save(entity);
		});
		
    	Department entity = deptRepo.findById(deptCol.get(deptCol.size()-1).getRecordId()).get();
    	entity.setActive(false);
    	entity.setRemarks(entity.getRemarks() + "-UPDATED");
    	
    	Department updatedEntity = deptRepo.save(entity);
    	
    	assertFalse(updatedEntity.isActive());
    	assertTrue(updatedEntity.getRemarks().equals(entity.getRemarks()));
    }
	
	@DisplayName("POSITIVE - JUnit test to delete entity method")
    @Test
    public void givenEntityCol_whenDeleteEntity_thenRemoveEntity(){
    	buildDummyCol().stream().forEach(entity -> {
			deptRepo.save(entity);
		});
    	
    	Department entityToDelete = deptRepo.findAll().get(1);
    	
    	deptRepo.deleteById(entityToDelete.getRecordId());
		
    	Optional<Department> entity = deptRepo.findById(entityToDelete.getRecordId());
    	
    	assertFalse(entity.isPresent());
    }
	
	@DisplayName("NEGATIVE - JUnit test to delete entity method")
    @Test
    public void givenInvalidId_whenDeleteEntity_thenRemoveEntity(){
    	assertThrows(EmptyResultDataAccessException.class, () -> {
    		deptRepo.deleteById(1L);
    	});
    }
}