package org.bham.btcert.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
* @Title: StuRevoInfo.java 
* @Package org.bham.btcert.model 
* @Description: TODO
* @author rxl635@student.bham.ac.uk   
* @version V1.0
 */
@Document(collection = "stu_revo_info")
public class StuRevoInfo {

	@Id
	private String id;

	private String u_id;   //学生ID
	private String u_name; //学生名称
	
	private String r_pri_key;
	private String rb_pri_key;
	
	private String r_address;  //单独回收地址
	private String rb_address; //批量回收地址
	private String cstate; 		// revoked, valid
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getU_id() {
		return u_id;
	}
	public void setU_id(String u_id) {
		this.u_id = u_id;
	}
	public String getU_name() {
		return u_name;
	}
	public void setU_name(String u_name) {
		this.u_name = u_name;
	}
	public String getR_pri_key() {
		return r_pri_key;
	}
	public void setR_pri_key(String r_pri_key) {
		this.r_pri_key = r_pri_key;
	}
	public String getRb_pri_key() {
		return rb_pri_key;
	}
	public void setRb_pri_key(String rb_pri_key) {
		this.rb_pri_key = rb_pri_key;
	}
	public String getR_address() {
		return r_address;
	}
	public void setR_address(String r_address) {
		this.r_address = r_address;
	}
	public String getRb_address() {
		return rb_address;
	}
	public void setRb_address(String rb_address) {
		this.rb_address = rb_address;
	}
	public String getCstate() {
		return cstate;
	}
	public void setCstate(String cstate) {
		this.cstate = cstate;
	}
	@Override
	public String toString() {
		return "StuRevoInfo [id=" + id + ", u_id=" + u_id + ", u_name=" + u_name + ", r_pri_key=" + r_pri_key
				+ ", rb_pri_key=" + rb_pri_key + ", r_address=" + r_address + ", rb_address=" + rb_address + ", cstate="
				+ cstate + "]";
	}
	
}
