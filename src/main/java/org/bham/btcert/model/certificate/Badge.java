package org.bham.btcert.model.certificate;

/**
 * 
 * @Title: Badge.java
 * @Package org.bham.btcert.model.certificate
 * @Description: TODO
 * @author rxl635@student.bham.ac.uk
 * @version V1.0
 */
public class Badge {

	private String id;
	private String type;
	private String name;
	private String description;
	private String created;
	private String expires;
	private String image;
	private Issuer issuer;
	private FileClaim fileClaim;
	private IdentityClaim identityClaim;
	private RevocationClaim revocationClaim;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Issuer getIssuer() {
		return issuer;
	}

	public void setIssuer(Issuer issuer) {
		this.issuer = issuer;
	}

	public FileClaim getFileClaim() {
		return fileClaim;
	}

	public void setFileClaim(FileClaim fileClaim) {
		this.fileClaim = fileClaim;
	}

	public IdentityClaim getIdentityClaim() {
		return identityClaim;
	}

	public void setIdentityClaim(IdentityClaim identityClaim) {
		this.identityClaim = identityClaim;
	}

	public RevocationClaim getRevocationClaim() {
		return revocationClaim;
	}

	public void setRevocationClaim(RevocationClaim revocationClaim) {
		this.revocationClaim = revocationClaim;
	}

	@Override
	public String toString() {
		return "Badge [id=" + id + ", type=" + type + ", name=" + name + ", description=" + description + ", created="
				+ created + ", expires=" + expires + ", image=" + image + ", issuer=" + issuer + ", fileClaim="
				+ fileClaim + ", identityClaim=" + identityClaim + ", revocationClaim=" + revocationClaim + "]";
	}

}
