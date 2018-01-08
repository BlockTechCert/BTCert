package org.bham.btcert.model.certificate;

import java.util.List;

/**
 * 
* @Title: RevocationClaim.java 
* @Package org.bham.btcert.model.certificate 
* @Description: TODO
* @author rxl635@student.bham.ac.uk   
* @version V1.0
 */
public class RevocationClaim {

	private String revocationAddress;
	private String batchRevocationAddress;
	private List<String> type; //"identityClaim","Extension"
	public String getRevocationAddress() {
		return revocationAddress;
	}
	public void setRevocationAddress(String revocationAddress) {
		this.revocationAddress = revocationAddress;
	}
	public String getBatchRevocationAddress() {
		return batchRevocationAddress;
	}
	public void setBatchRevocationAddress(String batchRevocationAddress) {
		this.batchRevocationAddress = batchRevocationAddress;
	}
	public List<String> getType() {
		return type;
	}
	public void setType(List<String> type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "RevocationClaim [revocationAddress=" + revocationAddress + ", batchRevocationAddress="
				+ batchRevocationAddress + ", type=" + type + "]";
	}
	
}


