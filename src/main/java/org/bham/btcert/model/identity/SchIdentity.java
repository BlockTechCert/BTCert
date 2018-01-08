package org.bham.btcert.model.identity;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
* @Title: SchIdentity.java 
* @Package org.bham.btcert.model.identity 
* @Description: TODO
* @author rxl635@student.bham.ac.uk   
* @version V1.0
 */
@Document(collection = "sch_identity")
public class SchIdentity {

	@Id
	private String id;

	private String sch_id; //学校ID
	private List<Map<String,String>> btc_address_list;
	private String btc_address_list_hash;
	private List<IdentityClaim> identityClaimList;
	private String latest_recycle_address;
	private String latest_redeem_script;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSch_id() {
		return sch_id;
	}
	public void setSch_id(String sch_id) {
		this.sch_id = sch_id;
	}
	public List<Map<String, String>> getBtc_address_list() {
		return btc_address_list;
	}
	public void setBtc_address_list(List<Map<String, String>> btc_address_list) {
		this.btc_address_list = btc_address_list;
	}
	public String getBtc_address_list_hash() {
		return btc_address_list_hash;
	}
	public void setBtc_address_list_hash(String btc_address_list_hash) {
		this.btc_address_list_hash = btc_address_list_hash;
	}
	public List<IdentityClaim> getIdentityClaimList() {
		return identityClaimList;
	}
	public void setIdentityClaimList(List<IdentityClaim> identityClaimList) {
		this.identityClaimList = identityClaimList;
	}
	public String getLatest_recycle_address() {
		return latest_recycle_address;
	}
	public void setLatest_recycle_address(String latest_recycle_address) {
		this.latest_recycle_address = latest_recycle_address;
	}
	public String getLatest_redeem_script() {
		return latest_redeem_script;
	}
	public void setLatest_redeem_script(String latest_redeem_script) {
		this.latest_redeem_script = latest_redeem_script;
	}
	@Override
	public String toString() {
		return "SchIdentity [id=" + id + ", sch_id=" + sch_id + ", btc_address_list=" + btc_address_list
				+ ", btc_address_list_hash=" + btc_address_list_hash + ", identityClaimList=" + identityClaimList
				+ ", latest_recycle_address=" + latest_recycle_address + ", latest_redeem_script="
				+ latest_redeem_script + "]";
	}
	
}
