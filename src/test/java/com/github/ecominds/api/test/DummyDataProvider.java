/**
 * @author	: Rajiv Kumar
 * @project	: boot-rest-api
 * @since	: 0.0.2
 * @date	: 07-Jan-2023
 */

package com.github.ecominds.api.test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.ecominds.api.rest.entity.Department;

public class DummyDataProvider {
	protected List<Department> buildDummyCol() {
		return Stream
				.of(buildDummy("Finance IT", "FIN-IT", "Department for handling finance operations"),
						buildDummy("HR IT", "HR-IT", "Department for handling HR operations"),
						buildDummy("Technology", "TECH", "Department for handling technical operations"))
				.collect(Collectors.toList());
	}

	protected Department buildDummy(String name, String code, String remarks) {
		return Department.builder().name(name).code(code).remarks(remarks).active(true).build();
	}
}