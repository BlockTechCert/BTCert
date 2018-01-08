package org.bham.btcert.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
* @Title: SchConf.java 
* @Package org.bham.btcert.model 
* @Description: TODO
* @author rxl635@student.bham.ac.uk   
* @version V1.0
 */
@Document(collection = "sch_conf")
public class SchConf {

	@Id
	private String id;

	private String name;
	private String email;
	private String url;
	private String image;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
}
