package org.bham.btcert.model.certificate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
* @Title: OperateDetail.java 
* @Package org.bham.btcert.model.certificate 
* @Description: TODO
* @author rxl635@student.bham.ac.uk   
* @version V1.0
 */
@Document(collection = "tansac_info")
public class OperateDetail {

	@Id
	private String id;

	private String cert_id;     //证书号码
	private String user_id;     //操作人ID
	private String user_name;   //操作人姓名
	private String oper_type;   //操作类型
	private String oper_time;   //操作时间
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCert_id() {
		return cert_id;
	}
	public void setCert_id(String cert_id) {
		this.cert_id = cert_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getOper_type() {
		return oper_type;
	}
	public void setOper_type(String oper_type) {
		this.oper_type = oper_type;
	}
	public String getOper_time() {
		return oper_time;
	}
	public void setOper_time(String oper_time) {
		this.oper_time = oper_time;
	}
	@Override
	public String toString() {
		return "OperateDetail [id=" + id + ", cert_id=" + cert_id + ", user_id=" + user_id + ", user_name=" + user_name
				+ ", oper_type=" + oper_type + ", oper_time=" + oper_time + "]";
	}

}
