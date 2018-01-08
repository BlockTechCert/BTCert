package org.bham.btcert.model.certificate;

/**
 * 
* @Title: Recipient.java 
* @Package org.bham.btcert.model.certificate 
* @Description: TODO
* @author rxl635@student.bham.ac.uk   
* @version V1.0
 */
public class Recipient {

	private String hashed;
	private String identity;
	private String type;
	public String getHashed() {
		return hashed;
	}
	public void setHashed(String hashed) {
		this.hashed = hashed;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "Recipient [hashed=" + hashed + ", identity=" + identity + ", type=" + type + "]";
	}
}
