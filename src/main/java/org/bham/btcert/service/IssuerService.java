package org.bham.btcert.service;

import java.util.List;

import org.bham.btcert.model.PageModel;
import org.bham.btcert.model.RevokeTansacInfo;
import org.bham.btcert.model.TansacInfo;
import org.bham.btcert.model.certificate.CertsInfo;
import org.bham.btcert.persistence.BaseMongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @Title: IssuerService.java
 * @Package org.bham.btcert.service
 * @Description: TODO
 * @author rxl635@student.bham.ac.uk
 * @version V1.0
 */
@Service
public class IssuerService {

	@Autowired
	private BaseMongoTemplate baseMongoTemplate;

	/**
	 * saveRevokeTansacInfo
	 * 
	 * @param TansacInfoList
	 * @return
	 */
	public int saveRevokeTansacInfo(RevokeTansacInfo RevokeTansacInfo) {
		return baseMongoTemplate.saveEntity(RevokeTansacInfo);
	}

	/**
	 * updateRevokeTansacInfo
	 * @param RevokeTansacInfo
	 * @return
	 */
	public int updateRevokeTansacInfo(RevokeTansacInfo RevokeTansacInfo) {
		String id = RevokeTansacInfo.getId();
		return baseMongoTemplate.updateEntity(id, RevokeTansacInfo, RevokeTansacInfo.class);
	}

	/**
	 * getRevokeTansacInfoPage
	 * @param page
	 * @return
	 */
	public PageModel<RevokeTansacInfo> getRevokeTansacInfoPage(PageModel<RevokeTansacInfo> page) {
		page = baseMongoTemplate.getListPage(page, RevokeTansacInfo.class);
		return page;
	}

	/**
	 * getRevokeTansacInfo
	 * @param id
	 * @return
	 */
	public RevokeTansacInfo getRevokeTansacInfo(String id) {
		return baseMongoTemplate.getEntity("id", id, RevokeTansacInfo.class);
	}

	/**
	 * getRevokeTansacInfobyAddress
	 * @param revoke_address
	 * @return
	 */
	public RevokeTansacInfo getRevokeTansacInfobyAddress(String revoke_address) {
		return baseMongoTemplate.getEntity("revoke_address", revoke_address, RevokeTansacInfo.class);
	}

	/**
	 * getTansacInfoPage
	 * @param page
	 * @return
	 */
	public PageModel<TansacInfo> getTansacInfoPage(PageModel<TansacInfo> page) {
		page = baseMongoTemplate.getListPage(page, TansacInfo.class);
		return page;
	}

	/**
	 * getTansacInfo
	 * @param id
	 * @return
	 */
	public TansacInfo getTansacInfo(String id) {
		return baseMongoTemplate.getEntity("id", id, TansacInfo.class);
	}
	
	/**
	 * findAllTansacInfo
	 * @return
	 */
	public List<TansacInfo> findAllTansacInfo() {
		return baseMongoTemplate.findAll(TansacInfo.class);
	}
	
	/**
	 * delTansacInfo
	 * @param delIds
	 * @return
	 */
	public int delTansacInfo(String delIds) {
		return baseMongoTemplate.delEntity(delIds, TansacInfo.class);
	}
	
	/**
	 * saveTansacInfoBatch
	 * @param TansacInfoList
	 * @return
	 */
	public int saveTansacInfoBatch(List<TansacInfo> TansacInfoList) {
		return baseMongoTemplate.saveEntityBatch(TansacInfoList);
	}
	
	/**
	 * saveTansacInfo
	 * @param TansacInfo
	 * @return
	 */
	public int saveTansacInfo(TansacInfo TansacInfo) {
		return baseMongoTemplate.saveEntity(TansacInfo);
	}

	/**
	 * updateTansacInfo
	 * @param TansacInfo
	 * @return
	 */
	public int updateTansacInfo(TansacInfo TansacInfo) {
		String id = TansacInfo.getId();
		return baseMongoTemplate.updateEntity(id, TansacInfo, TansacInfo.class);
	}

	/**
	 * updateCertsInfo
	 * @param CertsInfo
	 * @return
	 */
	public int updateCertsInfo(CertsInfo CertsInfo) {
		String id = CertsInfo.getId();
		return baseMongoTemplate.updateEntity(id, CertsInfo, CertsInfo.class);
	}

	/**
	 * getCertsInfoPage
	 * @param page
	 * @return
	 */
	public PageModel<CertsInfo> getCertsInfoPage(PageModel<CertsInfo> page) {
		page = baseMongoTemplate.getListPage(page, CertsInfo.class);
		return page;
	}

	/**
	 * getCertsInfo
	 * @param id
	 * @return
	 */
	public CertsInfo getCertsInfo(String id) {
		return baseMongoTemplate.getEntity("id", id, CertsInfo.class);
	}

	/**
	 * getCertsInfoByRevocationAddress
	 * @param revocationAddress
	 * @return
	 */
	public CertsInfo getCertsInfoByRevocationAddress(String revocationAddress) {
		return baseMongoTemplate.getEntity("openbadges.badge.revocationClaim.revocationAddress", revocationAddress,
				CertsInfo.class);
	}

	/**
	 * getCertsInfoList
	 * @param ids
	 * @return
	 */
	public List<CertsInfo> getCertsInfoList(String ids) {
		return baseMongoTemplate.getEntity(ids, CertsInfo.class);
	}
	
	/**
	 * getCertsInfoListByTranId
	 * @param transactionId
	 * @return
	 */
	public List<CertsInfo> getCertsInfoListByTranId(String transactionId) {
		return baseMongoTemplate.getEntityList("transactionId", transactionId, CertsInfo.class);
	}

	/**
	 * saveCertsInfoBatch
	 * @param CertsInfoList
	 * @return
	 */
	public int saveCertsInfoBatch(List<CertsInfo> CertsInfoList) {
		return baseMongoTemplate.saveEntityBatch(CertsInfoList);
	}
}
