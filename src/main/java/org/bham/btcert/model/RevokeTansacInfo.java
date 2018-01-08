package org.bham.btcert.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
* @Title: RevokeTansacInfo.java 
* @Package org.bham.btcert.model 
* @Description: TODO
* @author rxl635@student.bham.ac.uk   
* @version V1.0
 */
@Document(collection = "revoke_tansac_info")
public class RevokeTansacInfo {

	@Id
	private String id;

	private String student_name;     //原生态交易信息
	private String revoke_address;     //原生态交易信息
	private String revoke_id;     //原生态交易信息
	private String raw_tran;     //原生态交易信息
	private String sign_number; //签发个数
	private String sign_state;  //签发状态 sign people : sign time
	private String broadcast_state;  //签发状态 sign people : sign time
	private String broadcast_transaction_id;  // 确认的ID
	private String broadcast_success;  // 是否广播成功
	private List<Map<String,String>> input;
	private List<Map<String,String>> output;
	private List<Map<String,String>> signatures;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStudent_name() {
		return student_name;
	}
	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}
	public String getRevoke_address() {
		return revoke_address;
	}
	public void setRevoke_address(String revoke_address) {
		this.revoke_address = revoke_address;
	}
	public String getRevoke_id() {
		return revoke_id;
	}
	public void setRevoke_id(String revoke_id) {
		this.revoke_id = revoke_id;
	}
	public String getRaw_tran() {
		return raw_tran;
	}
	public void setRaw_tran(String raw_tran) {
		this.raw_tran = raw_tran;
	}
	public String getSign_number() {
		return sign_number;
	}
	public void setSign_number(String sign_number) {
		this.sign_number = sign_number;
	}
	public String getSign_state() {
		return sign_state;
	}
	public void setSign_state(String sign_state) {
		this.sign_state = sign_state;
	}
	public String getBroadcast_state() {
		return broadcast_state;
	}
	public void setBroadcast_state(String broadcast_state) {
		this.broadcast_state = broadcast_state;
	}
	public String getBroadcast_transaction_id() {
		return broadcast_transaction_id;
	}
	public void setBroadcast_transaction_id(String broadcast_transaction_id) {
		this.broadcast_transaction_id = broadcast_transaction_id;
	}
	public String getBroadcast_success() {
		return broadcast_success;
	}
	public void setBroadcast_success(String broadcast_success) {
		this.broadcast_success = broadcast_success;
	}
	public List<Map<String, String>> getInput() {
		return input;
	}
	public void setInput(List<Map<String, String>> input) {
		this.input = input;
	}
	public List<Map<String, String>> getOutput() {
		return output;
	}
	public void setOutput(List<Map<String, String>> output) {
		this.output = output;
	}
	public List<Map<String, String>> getSignatures() {
		return signatures;
	}
	public void setSignatures(List<Map<String, String>> signatures) {
		this.signatures = signatures;
	}
	@Override
	public String toString() {
		return "RevokeTansacInfo [id=" + id + ", student_name=" + student_name + ", revoke_address=" + revoke_address
				+ ", revoke_id=" + revoke_id + ", raw_tran=" + raw_tran + ", sign_number=" + sign_number
				+ ", sign_state=" + sign_state + ", broadcast_state=" + broadcast_state + ", broadcast_transaction_id="
				+ broadcast_transaction_id + ", broadcast_success=" + broadcast_success + ", input=" + input
				+ ", output=" + output + ", signatures=" + signatures + "]";
	}
}
