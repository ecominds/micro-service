/**
 * @author	: Rajiv Kumar
 * @project	: boot-rest-api
 * @since	: 0.0.2
 * @date	: 07-Jan-2023
 */

package com.github.ecominds.api.rest.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.ecominds.api.rest.dao.IDepartmentDAO;
import com.github.ecominds.api.rest.entity.Department;
import com.github.ecominds.api.rest.exception.AppException;
import com.github.ecominds.api.test.DummyDataProvider;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTests extends DummyDataProvider{
	
	@Mock
	private IDepartmentDAO repo;
	
	@InjectMocks
	private DepartmentServiceImpl service;
	
	@DisplayName("POSITIVE - JUnit test for create entity method")
	@Test
	public void givenEntity_whenCreate_thenReturnCreatedEntity(){
		Department entity = buildDummyCol().get(0);
		
		given(repo.findByCode(entity.getCode())).willReturn(Optional.empty());
		given(repo.save(entity)).willReturn(entity);
		
		Department savedEntity = service.create(entity);
		assertThat(savedEntity).isNotNull();
	}
	
	@DisplayName("NEGATIVE - JUnit test for create entity method (duplicate record)")
	@Test
	public void givenEntity_whenSaveEntitySecond_thenThrowsException(){
		Department entity = buildDummyCol().get(0);
		
		given(repo.findByCode(entity.getCode())).willReturn(Optional.of(entity));

		assertThrows(RuntimeException.class, ()->{
			service.create(entity);
		});
		
		verify(repo, never()).save(any(Department.class));
	}

	@DisplayName("POSITIVE - JUnit test for findAll method")
    @Test
    public void givenEntityCol_whenFindAll_thenReturnAllEntities(){
    	List<Department> deptCol = buildDummyCol();
    	given(repo.findAll()).willReturn(deptCol);
    	
    	List<Department> resultCol = service.findAll();
    	
    	assertThat(resultCol).isNotNull();
    	assertThat(resultCol.size()).isEqualTo(deptCol.size());
    }
	
	@DisplayName("NEGATIVE - JUnit test for findAll method")
    @Test
    public void givenEmptyEntityCol_whenFindAll_thenReturnEmptyCol(){
    	given(repo.findAll()).willReturn(Collections.emptyList());
    	
    	List<Department> resultCol = service.findAll();
    	
    	assertThat(resultCol).isNotNull();
    	assertThat(resultCol).isEmpty();
    	assertThat(resultCol.size()).isEqualTo(0);
    }
	
	@DisplayName("POSITIVE - JUnit test for findById method")
    @Test
    public void givenEntity_whenFindById_thenReturnEntity(){
    	Department entity = buildDummyCol().get(0);
    	given(repo.findById(entity.getRecordId())).willReturn(Optional.of(entity));
    	
    	Optional<Department> savedEntity = service.findById(entity.getRecordId());
    	
    	assertThat(savedEntity).isNotNull();
    	assertTrue(savedEntity.isPresent());
    }
	
	@DisplayName("NEGATIVE - JUnit test for findById method")
    @Test
    public void givenInvalidId_whenFindById_thenReturnEmpty(){
		long recordId = 1L;
    	given(repo.findById(recordId)).willReturn(Optional.empty());
    	
    	Optional<Department> savedEntity = service.findById(recordId);
    	
    	assertThat(savedEntity).isNotNull();
    	assertFalse(savedEntity.isPresent());
    }
	
	@DisplayName("POSITIVE - JUnit test for update entity method")
	@Test
	public void givenEntity_whenUpdateEntity_thenReturnUpdatedEntity(){
		Department entity = buildDummyCol().get(0);
		entity.setRecordId(1L);
		
		given(repo.findByCode(entity.getCode())).willReturn(Optional.empty());
		given(repo.save(entity)).willReturn(entity);

    	entity.setActive(false);
    	entity.setRemarks(entity.getRemarks() + "-UPDATED");
    	
    	Department updatedEntity = service.update(entity);
    	
    	assertFalse(updatedEntity.isActive());
    	assertThat(updatedEntity.getRemarks().equals(entity.getRemarks()));
	}
	
	@DisplayName("NEGATIVE - JUnit test for update entity method (invalid record)")
	@Test
	public void givenInvalidId_whenUpdateEntity_thenThrowsException(){
		Department entity = buildDummyCol().get(0);

		entity.setActive(false);
    	
    	assertThrows(AppException.class, ()-> {
    		service.update(entity);
    	});
	}
	
	@DisplayName("NEGATIVE - JUnit test for update entity method (duplicate record)")
	@Test
	public void givenDuplicateEntity_whenUpdateEntity_thenThrowsException(){
		Department entityToUpdate = buildDummyCol().get(0);
		entityToUpdate.setRecordId(1L);
		
		Department anotherEntityWithSameCode = buildDummyCol().get(0);
		anotherEntityWithSameCode.setRecordId(2L);
		
		given(repo.findByCode(entityToUpdate.getCode())).willReturn(Optional.of(anotherEntityWithSameCode));

		entityToUpdate.setActive(false);
    	
    	assertThrows(AppException.class, ()-> {
    		service.update(entityToUpdate);
    	});
	}
	
	@DisplayName("JUnit test for delete entity method")
	@Test
	public void givenEntity_whenDelete_thenNothing() throws Exception{
		Department entity = buildDummyCol().get(0);
		entity.setRecordId(3L);
		
		given(service.findById(entity.getRecordId())).willReturn(Optional.of(entity));
		
		
		willDoNothing().given(repo).deleteById(entity.getRecordId());
    	
    	service.delete(entity.getRecordId());
    	
    	verify(repo, times(1)).deleteById(entity.getRecordId());
	}
}