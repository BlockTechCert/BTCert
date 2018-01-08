package org.bham.btcert.model;

/**
 * 
* @Title: CheckInfo.java 
* @Package org.bham.btcert.model 
* @Description: TODO
* @author rxl635@student.bham.ac.uk   
* @version V1.0
 */
public class CheckInfo {

	
	private String id;

	private String standard;
	private String badgeClass;
	private String badgeId;
	private String badgeType;
	private String badgeName;
	private String badgeDescription;
	private String badgeImage;
	private String created;
	private String expires;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getBadgeClass() {
		return badgeClass;
	}
	public void setBadgeClass(String badgeClass) {
		this.badgeClass = badgeClass;
	}
	public String getBadgeId() {
		return badgeId;
	}
	public void setBadgeId(String badgeId) {
		this.badgeId = badgeId;
	}
	public String getBadgeType() {
		return badgeType;
	}
	public void setBadgeType(String badgeType) {
		this.badgeType = badgeType;
	}
	public String getBadgeName() {
		return badgeName;
	}
	public void setBadgeName(String badgeName) {
		this.badgeName = badgeName;
	}
	public String getBadgeDescription() {
		return badgeDescription;
	}
	public void setBadgeDescription(String badgeDescription) {
		this.badgeDescription = badgeDescription;
	}
	public String getBadgeImage() {
		return badgeImage;
	}
	public void setBadgeImage(String badgeImage) {
		this.badgeImage = badgeImage;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getExpires() {
		return expires;
	}
	public void setExpires(String expires) {
		this.expires = expires;
	}
	@Override
	public String toString() {
		return "CheckInfo [id=" + id + ", standard=" + standard + ", badgeClass=" + badgeClass + ", badgeId=" + badgeId
				+ ", badgeType=" + badgeType + ", badgeName=" + badgeName + ", badgeDescription=" + badgeDescription
				+ ", badgeImage=" + badgeImage + ", created=" + created + ", expires=" + expires + "]";
	}
	
	
	
}
