package org.bham.btcert.service;

import java.util.List;

import org.bham.btcert.model.PageModel;
import org.bham.btcert.model.StuConf;
import org.bham.btcert.model.certificate.CertsInfo;
import org.bham.btcert.persistence.BaseMongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @Title: StudentService.java
 * @Package org.bham.btcert.service
 * @Description: TODO
 * @author rxl635@student.bham.ac.uk
 * @version V1.0
 */
@Service
public class StudentService {

	@Autowired
	private BaseMongoTemplate baseMongoTemplate;

	/**
	 * getStuConfPage
	 * @param page
	 * @return
	 */
	public PageModel<StuConf> getStuConfPage(PageModel<StuConf> page) {
		page = baseMongoTemplate.getListPage(page, StuConf.class);
		return page;
	}

	/**
	 * getStuConf
	 * @param id
	 * @return
	 */
	public StuConf getStuConf(String id) {
		return baseMongoTemplate.getEntity("id", id, StuConf.class);
	}

	/**
	 * findAllStuConf
	 * @return
	 */
	public List<StuConf> findAllStuConf() {
		return baseMongoTemplate.findAll(StuConf.class);
	}

	/**
	 * getCertStateByUserId
	 * @param userId
	 * @return
	 */
	public CertsInfo getCertStateByUserId(String userId) {
		return baseMongoTemplate.getEntity("recipient", userId, CertsInfo.class);
	}

	/**
	 * delStuConf
	 * @param delIds
	 * @return
	 */
	public int delStuConf(String delIds) {
		return baseMongoTemplate.delEntity(delIds, StuConf.class);
	}

	/**
	 * saveStuConfBatch
	 * @param StuConfList
	 * @return
	 */
	public int saveStuConfBatch(List<StuConf> StuConfList) {
		return baseMongoTemplate.saveEntityBatch(StuConfList);
	}

	/**
	 * saveStuConf
	 * @param StuConf
	 * @return
	 */
	public int saveStuConf(StuConf StuConf) {
		return baseMongoTemplate.saveEntity(StuConf);
	}
	
	/**
	 * updateStuConf
	 * @param stuConf
	 * @return
	 */
	public int updateStuConf(StuConf stuConf) {
		String id = stuConf.getId();
		return baseMongoTemplate.updateEntity(id, stuConf, StuConf.class);
	}

}
