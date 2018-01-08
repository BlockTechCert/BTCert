package org.bham.btcert.model.certificate;

import java.util.HashMap;
import java.util.List;

/**
 * 
* @Title: Signature.java 
* @Package org.bham.btcert.model.certificate 
* @Description: TODO
* @author rxl635@student.bham.ac.uk   
* @version V1.0
 */
public class Signature {

	private String context;
	private List<String> typelist;
	private String targetHash;
	private String merkleRoot;
	private List<HashMap<String,String>> proof;
	private List<Anchors> anchors;
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public List<String> getTypelist() {
		return typelist;
	}
	public void setTypelist(List<String> typelist) {
		this.typelist = typelist;
	}
	public String getTargetHash() {
		return targetHash;
	}
	public void setTargetHash(String targetHash) {
		this.targetHash = targetHash;
	}
	public String getMerkleRoot() {
		return merkleRoot;
	}
	public void setMerkleRoot(String merkleRoot) {
		this.merkleRoot = merkleRoot;
	}
	public List<HashMap<String, String>> getProof() {
		return proof;
	}
	public void setProof(List<HashMap<String, String>> proof) {
		this.proof = proof;
	}
	public List<Anchors> getAnchors() {
		return anchors;
	}
	public void setAnchors(List<Anchors> anchors) {
		this.anchors = anchors;
	}
	@Override
	public String toString() {
		return "Signature [context=" + context + ", typelist=" + typelist + ", targetHash=" + targetHash
				+ ", merkleRoot=" + merkleRoot + ", proof=" + proof + ", anchors=" + anchors + "]";
	}
	
}
