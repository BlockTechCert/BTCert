package org.bham.btcert.model.certificate;

import java.util.List;

/**
 * 
* @Title: Verification.java 
* @Package org.bham.btcert.model.certificate 
* @Description: TODO
* @author rxl635@student.bham.ac.uk   
* @version V1.0
 */
public class Verification {

private List<String> type; //"identityClaim","Extension"

public List<String> getType() {
	return type;
}

public void setType(List<String> type) {
	this.type = type;
}

@Override
public String toString() {
	return "Verification [type=" + type + "]";
}
	
	
}
