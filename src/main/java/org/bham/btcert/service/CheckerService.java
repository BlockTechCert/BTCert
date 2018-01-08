package org.bham.btcert.service;

import java.util.List;

import org.bham.btcert.model.PageModel;
import org.bham.btcert.model.certificate.CertsInfo;
import org.bham.btcert.persistence.BaseMongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @Title: CheckerService.java
 * @Package org.bham.btcert.service
 * @Description: TODO CheckerService
 * @author rxl635@student.bham.ac.uk
 * @version V1.0
 */

@Service
public class CheckerService {

	@Autowired
	private BaseMongoTemplate baseMongoTemplate;

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
	 * getCertsInfoByIds
	 * @param ids
	 * @return
	 */
	public List<CertsInfo> getCertsInfoByIds(String ids) {
		return baseMongoTemplate.getEntity(ids, CertsInfo.class);
	}

	/**
	 * getCertsInfoByCstate
	 * @param cstate
	 * @return
	 */
	public List<CertsInfo> getCertsInfoByCstate(String cstate) {
		return baseMongoTemplate.getEntityList("cstate", cstate, CertsInfo.class);
	}

    /**
     * getCertsInfoByTranId
     * @param transactionId
     * @return
     */
	public List<CertsInfo> getCertsInfoByTranId(String transactionId) {
		return baseMongoTemplate.getEntityList("transactionId", transactionId, CertsInfo.class);
	}
	
	/**
	 * findAllCertsInfo
	 * @return
	 */
	public List<CertsInfo> findAllCertsInfo() {
		return baseMongoTemplate.findAll(CertsInfo.class);
	}

	/**
	 * delCertsInfo
	 * @param delIds
	 * @return
	 */
	public int delCertsInfo(String delIds) {
		return baseMongoTemplate.delEntity(delIds, CertsInfo.class);
	}


	/**
	 * saveCertsInfoBatch
	 * @param CertsInfoList
	 * @return
	 */
	public int saveCertsInfoBatch(List<CertsInfo> CertsInfoList) {
		return baseMongoTemplate.saveEntityBatch(CertsInfoList);
	}

	/**
	 * saveCertsInfo
	 * @param CertsInfo
	 * @return
	 */
	public int saveCertsInfo(CertsInfo CertsInfo) {
		return baseMongoTemplate.saveEntity(CertsInfo);
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

}
