package org.bham.btcert.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
* @Title: Users.java 
* @Package org.bham.btcert.model 
* @Description: TODO
* @author rxl635@student.bham.ac.uk   
* @version V1.0
 */
@Document(collection = "users")
public class Users {

	@Id
	private String id;

	private String u_name;
	private String u_passwd;
	private String role;
	private String state;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getU_name() {
		return u_name;
	}
	public void setU_name(String u_name) {
		this.u_name = u_name;
	}
	public String getU_passwd() {
		return u_passwd;
	}
	public void setU_passwd(String u_passwd) {
		this.u_passwd = u_passwd;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "Users [id=" + id + ", u_name=" + u_name + ", u_passwd=" + u_passwd + ", role=" + role + ", state="
				+ state + "]";
	}
	
}
