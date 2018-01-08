package org.bham.btcert.model.identity;

/**
 * 
* @Title: IdentityClaim.java 
* @Package org.bham.btcert.model.identity 
* @Description: TODO
* @author rxl635@student.bham.ac.uk   
* @version V1.0
 */
public class IdentityClaim {

	private String public_key;
	private String signature;
	private String check_value;
	
	public String getPublic_key() {
		return public_key;
	}
	public void setPublic_key(String public_key) {
		this.public_key = public_key;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getCheck_value() {
		return check_value;
	}
	public void setCheck_value(String check_value) {
		this.check_value = check_value;
	}
	@Override
	public String toString() {
		return "IdentityClaim [public_key=" + public_key + ", signature=" + signature + ", check_value=" + check_value
				+ "]";
	}
	
}
