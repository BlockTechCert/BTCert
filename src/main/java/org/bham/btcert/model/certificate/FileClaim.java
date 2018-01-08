package org.bham.btcert.model.certificate;

import java.util.List;

/**
 * 
* @Title: FileClaim.java 
* @Package org.bham.btcert.model.certificate 
* @Description: TODO
* @author rxl635@student.bham.ac.uk   
* @version V1.0
 */
public class FileClaim {

	private String filehash;
	private String filestore;
	private String filetype;
	private List<String> type; //"identityClaim","Extension"
	public String getFilehash() {
		return filehash;
	}
	public void setFilehash(String filehash) {
		this.filehash = filehash;
	}
	public String getFilestore() {
		return filestore;
	}
	public void setFilestore(String filestore) {
		this.filestore = filestore;
	}
	public String getFiletype() {
		return filetype;
	}
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	public List<String> getType() {
		return type;
	}
	public void setType(List<String> type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "FileClaim [filehash=" + filehash + ", filestore=" + filestore + ", filetype=" + filetype + ", type="
				+ type + "]";
	}
	
}


