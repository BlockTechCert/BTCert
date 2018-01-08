package org.bham.btcert.model.certificate;

import java.util.List;

/**
 * 
* @Title: IdentityClaim.java 
* @Package org.bham.btcert.model.certificate 
* @Description: TODO
* @author rxl635@student.bham.ac.uk   
* @version V1.0
 */
public class IdentityClaim {

	private String identityName;
	private String validationtime;
	private String BTCaddress;
	private List<String> type; //"identityClaim","Extension"
	public String getIdentityName() {
		return identityName;
	}
	public void setIdentityName(String identityName) {
		this.identityName = identityName;
	}
	public String getValidationtime() {
		return validationtime;
	}
	public void setValidationtime(String validationtime) {
		this.validationtime = validationtime;
	}
	public String getBTCaddress() {
		return BTCaddress;
	}
	public void setBTCaddress(String bTCaddress) {
		BTCaddress = bTCaddress;
	}
	public List<String> getType() {
		return type;
	}
	public void setType(List<String> type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "IdentityClaim [identityName=" + identityName + ", validationtime=" + validationtime + ", BTCaddress="
				+ BTCaddress + ", type=" + type + "]";
	}
	
	
	
}




