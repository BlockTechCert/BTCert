package org.bham.btcert.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
* @Title: MyResourceNotFoundHandler.java 
* @Package org.bham.btcert.exception 
* @Description: TODO handle 404
* @author rxl635@student.bham.ac.uk   
* @version V1.0
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class MyResourceNotFoundHandler extends RuntimeException{
	 	
     	private static final long serialVersionUID = 1L;

		@ResponseStatus(HttpStatus.NOT_FOUND)
	    public String handleResourceNotFoundException() {
	        return "/404";
	    }
}
