/**
 * @author	: Rajiv Kumar
 * @project	: boot-rest-api
 * @since	: 0.0.2
 * @date	: 07-Jan-2023
 */

package com.github.ecominds.api.rest.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dept_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Department {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long recordId;

	@Column(unique = true)
	@NonNull
	private String code;

	@Column(length = 128)
	@NonNull
	private String name;

	private String remarks;

	private boolean active;
}