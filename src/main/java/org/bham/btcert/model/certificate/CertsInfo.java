package org.bham.btcert.model.certificate;


import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
* @Title: CertsInfo.java 
* @Package org.bham.btcert.model.certificate 
* @Description: TODO
* @author rxl635@student.bham.ac.uk   
* @version V1.0
 */
@Document(collection = "certs_info")
public class CertsInfo {

	@Id
	private String id;
	private String transactionId;
	private String recipient;
	private String createTime;
	private Openbadges openbadges;
	
	private String cstate; 		//证书状态 new, checked,merged,valid,revoked
	private List<OperateDetail> odl;  //签发细节
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Openbadges getOpenbadges() {
		return openbadges;
	}
	public void setOpenbadges(Openbadges openbadges) {
		this.openbadges = openbadges;
	}
	public String getCstate() {
		return cstate;
	}
	public void setCstate(String cstate) {
		this.cstate = cstate;
	}
	public List<OperateDetail> getOdl() {
		return odl;
	}
	public void setOdl(List<OperateDetail> odl) {
		this.odl = odl;
	}
	@Override
	public String toString() {
		return "CertsInfo [id=" + id + ", transactionId=" + transactionId + ", recipient=" + recipient + ", createTime="
				+ createTime + ", openbadges=" + openbadges + ", cstate=" + cstate + ", odl=" + odl + "]";
	}
	
}
