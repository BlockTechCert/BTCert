package org.bham.btcert.model.certificate;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
* @Title: Openbadges.java 
* @Package org.bham.btcert.model.certificate 
* @Description: TODO
* @author rxl635@student.bham.ac.uk   
* @version V1.0
 */
@Document(collection = "openbadges")
public class Openbadges {

	
	
	@Id
	private String id;
	
	private List<String> context;
	private String type;
	
	private Recipient recipient;
	private String issuedOn;
	private Badge badge;
	private Verification verification;
	private Signature signature;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getContext() {
		return context;
	}
	public void setContext(List<String> context) {
		this.context = context;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Recipient getRecipient() {
		return recipient;
	}
	public void setRecipient(Recipient recipient) {
		this.recipient = recipient;
	}
	public String getIssuedOn() {
		return issuedOn;
	}
	public void setIssuedOn(String issuedOn) {
		this.issuedOn = issuedOn;
	}
	public Badge getBadge() {
		return badge;
	}
	public void setBadge(Badge badge) {
		this.badge = badge;
	}
	public Verification getVerification() {
		return verification;
	}
	public void setVerification(Verification verification) {
		this.verification = verification;
	}
	public Signature getSignature() {
		return signature;
	}
	public void setSignature(Signature signature) {
		this.signature = signature;
	}
	@Override
	public String toString() {
		return "Openbadges [id=" + id + ", context=" + context + ", type=" + type + ", recipient=" + recipient
				+ ", issuedOn=" + issuedOn + ", badge=" + badge + ", verification=" + verification + ", signature="
				+ signature + "]";
	}
	
	
}
