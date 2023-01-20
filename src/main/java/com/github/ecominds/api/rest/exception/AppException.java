/**
 * @author	: Rajiv Kumar
 * @project	: boot-rest-api
 * @since	: 0.0.2
 * @date	: 07-Jan-2023
 */

package com.github.ecominds.api.rest.exception;

public class AppException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public AppException(String errorMessage) {
        super(errorMessage);
    }
}