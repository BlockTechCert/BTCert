package org.bham.btcert.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
* @Title: StuConf.java 
* @Package org.bham.btcert.model 
* @Description: TODO
* @author rxl635@student.bham.ac.uk   
* @version V1.0
 */
@Document(collection = "stu_conf")
public class StuConf {

	@Id
	private String id;

	private String user_id;
	private String given_name;
	private String family_name;
	private String identity;
	private String identity_type;
	private String birthday;
	private String file_hash;
	private String apply_type;
	private String apply_state; //new, passed, rejected
	private String apply_note; //备注 ,被拒绝后的附言
	private String apply_time;  //申请时间
	private String handle_time; //处理时间
	private String certs_id; //证书ID
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getGiven_name() {
		return given_name;
	}
	public void setGiven_name(String given_name) {
		this.given_name = given_name;
	}
	public String getFamily_name() {
		return family_name;
	}
	public void setFamily_name(String family_name) {
		this.family_name = family_name;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public String getIdentity_type() {
		return identity_type;
	}
	public void setIdentity_type(String identity_type) {
		this.identity_type = identity_type;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getFile_hash() {
		return file_hash;
	}
	public void setFile_hash(String file_hash) {
		this.file_hash = file_hash;
	}
	public String getApply_type() {
		return apply_type;
	}
	public void setApply_type(String apply_type) {
		this.apply_type = apply_type;
	}
	public String getApply_state() {
		return apply_state;
	}
	public void setApply_state(String apply_state) {
		this.apply_state = apply_state;
	}
	public String getApply_note() {
		return apply_note;
	}
	public void setApply_note(String apply_note) {
		this.apply_note = apply_note;
	}
	public String getApply_time() {
		return apply_time;
	}
	public void setApply_time(String apply_time) {
		this.apply_time = apply_time;
	}
	public String getHandle_time() {
		return handle_time;
	}
	public void setHandle_time(String handle_time) {
		this.handle_time = handle_time;
	}
	public String getCerts_id() {
		return certs_id;
	}
	public void setCerts_id(String certs_id) {
		this.certs_id = certs_id;
	}
	@Override
	public String toString() {
		return "StuConf [id=" + id + ", user_id=" + user_id + ", given_name=" + given_name + ", family_name="
				+ family_name + ", identity=" + identity + ", identity_type=" + identity_type + ", birthday=" + birthday
				+ ", file_hash=" + file_hash + ", apply_type=" + apply_type + ", apply_state=" + apply_state
				+ ", apply_note=" + apply_note + ", apply_time=" + apply_time + ", handle_time=" + handle_time
				+ ", certs_id=" + certs_id + "]";
	}
	
}
