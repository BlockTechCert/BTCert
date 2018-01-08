package org.bham.btcert.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
* @Title: TansacInfo.java 
* @Package org.bham.btcert.model 
* @Description: TODO
* @author rxl635@student.bham.ac.uk   
* @version V1.0
 */
@Document(collection = "tansac_info")
public class TansacInfo {

	@Id
	private String id;

	private String merkleRoot;
	private String certs_number;     //证书个数
	private String raw_tran;     //原生态交易信息
	private String tran_hash;    //transaction hash
	private String sign_number; //签发个数
	private String sign_state;  //签发状态 sign people : sign time
	private String broadcast_state;  //签发状态 sign people : sign time
	private String broadcast_transaction_id;  // 确认的ID
	private String broadcast_success;  // 是否广播成功
	private String serialize_state;  //签发状态 sign people_sign time
	private List<Map<String,String>> input;
	private List<Map<String,String>> output;
	private List<Map<String,String>> signatures;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMerkleRoot() {
		return merkleRoot;
	}
	public void setMerkleRoot(String merkleRoot) {
		this.merkleRoot = merkleRoot;
	}
	public String getCerts_number() {
		return certs_number;
	}
	public void setCerts_number(String certs_number) {
		this.certs_number = certs_number;
	}
	public String getRaw_tran() {
		return raw_tran;
	}
	public void setRaw_tran(String raw_tran) {
		this.raw_tran = raw_tran;
	}
	public String getTran_hash() {
		return tran_hash;
	}
	public void setTran_hash(String tran_hash) {
		this.tran_hash = tran_hash;
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
	public String getSerialize_state() {
		return serialize_state;
	}
	public void setSerialize_state(String serialize_state) {
		this.serialize_state = serialize_state;
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
		return "TansacInfo [id=" + id + ", merkleRoot=" + merkleRoot + ", certs_number=" + certs_number + ", raw_tran="
				+ raw_tran + ", tran_hash=" + tran_hash + ", sign_number=" + sign_number + ", sign_state=" + sign_state
				+ ", broadcast_state=" + broadcast_state + ", broadcast_transaction_id=" + broadcast_transaction_id
				+ ", broadcast_success=" + broadcast_success + ", serialize_state=" + serialize_state + ", input="
				+ input + ", output=" + output + ", signatures=" + signatures + "]";
	}
	
}
