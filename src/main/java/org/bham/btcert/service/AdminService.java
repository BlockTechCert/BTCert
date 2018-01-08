package org.bham.btcert.service;

import java.util.List;

import org.bham.btcert.model.PageModel;
import org.bham.btcert.model.SchConf;
import org.bham.btcert.model.StuRevoInfo;
import org.bham.btcert.model.Users;
import org.bham.btcert.model.identity.SchIdentity;
import org.bham.btcert.persistence.BaseMongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @Title: AdminService.java
 * @Package org.bham.btcert.service
 * @Description: TODO AdminService
 * @author rxl635@student.bham.ac.uk
 * @version V1.0
 */

@Service
public class AdminService {

	@Autowired
	private BaseMongoTemplate baseMongoTemplate;


	/**
	 * getSchIdentityPage
	 * @param page
	 * @return
	 */
	public PageModel<SchIdentity> getSchIdentityPage(PageModel<SchIdentity> page) {
		page = baseMongoTemplate.getListPage(page, SchIdentity.class);
		return page;
	}

	/**
	 * findAllSchIdentity
	 * @return
	 */
	public List<SchIdentity> findAllSchIdentity() {
		return baseMongoTemplate.findAll(SchIdentity.class);
	}

	/**
	 * updateSchIdentity
	 * @param schIdentity
	 * @return
	 */
	public int updateSchIdentity(SchIdentity schIdentity) {
		String id = schIdentity.getId();
		return baseMongoTemplate.updateEntity(id, schIdentity, SchIdentity.class);
	}


	/**
	 * getSchIdentity
	 * @param id
	 * @return
	 */
	public SchIdentity getSchIdentity(String id) {
		return (SchIdentity) baseMongoTemplate.getEntity("id", id, SchIdentity.class);
	}

	/**
	 * saveSchIdentity
	 * @param schIdentity
	 * @return
	 */
	public int saveSchIdentity(SchIdentity schIdentity) {
		return baseMongoTemplate.saveEntity(schIdentity);
	}

	/**
	 * getStuRevoInfoPage
	 * @param page
	 * @return
	 */
	public PageModel<StuRevoInfo> getStuRevoInfoPage(PageModel<StuRevoInfo> page) {
		page = baseMongoTemplate.getListPage(page, StuRevoInfo.class);
		return page;
	}

	/**
	 * getStuRevoInfo
	 * @param id
	 * @return
	 */
	public StuRevoInfo getStuRevoInfo(String id) {
		return baseMongoTemplate.getEntity("id", id, StuRevoInfo.class);
	}

	/**
	 * getStuRevoInfoByUserId
	 * @param u_name
	 * @return
	 */
	public StuRevoInfo getStuRevoInfoByUserId(String u_name) {
		return baseMongoTemplate.getEntity("u_name", u_name, StuRevoInfo.class);
	}

	/**
	 * findAllStuRevoInfo
	 * @return
	 */
	public List<StuRevoInfo> findAllStuRevoInfo() {
		return baseMongoTemplate.findAll(StuRevoInfo.class);
	}

	/**
	 * delStuRevoInfo
	 * @param delIds
	 * @return
	 */
	public int delStuRevoInfo(String delIds) {
		return baseMongoTemplate.delEntity(delIds, StuRevoInfo.class);
	}

	/**
	 * saveStuRevoInfoBatch
	 * @param stuRevoInfoList
	 * @return
	 */
	public int saveStuRevoInfoBatch(List<StuRevoInfo> stuRevoInfoList) {
		return baseMongoTemplate.saveEntityBatch(stuRevoInfoList);
	}

	/**
	 * saveStuRevoInfo
	 * @param stuRevoInfo
	 * @return
	 */
	public int saveStuRevoInfo(StuRevoInfo stuRevoInfo) {
		return baseMongoTemplate.saveEntity(stuRevoInfo);
	}
	
	/**
	 * updateStuRevoInfo
	 * @param stuRevoInfo
	 * @return
	 */
	public int updateStuRevoInfo(StuRevoInfo stuRevoInfo) {
		String id = stuRevoInfo.getId();
		return baseMongoTemplate.updateEntity(id, stuRevoInfo, StuRevoInfo.class);
	}

	/**
	 * getUsersPage
	 * @param page
	 * @return
	 */
	public PageModel<Users> getUsersPage(PageModel<Users> page) {
		page = baseMongoTemplate.getListPage(page, Users.class);
		return page;
	}

	/**
	 * getUsers
	 * @param id
	 * @return
	 */
	public Users getUsers(String id) {
		return (Users) baseMongoTemplate.getEntity("id", id, Users.class);
	}

	/**
	 * updateUsers
	 * @param users
	 * @return
	 */
	public int updateUsers(Users users) {
		String id = users.getId();
		return baseMongoTemplate.updateEntity(id, users, Users.class);
	}

	/**
	 * saveUsers
	 * @param schConf
	 * @return
	 */
	public int saveUsers(Users schConf) {
		return baseMongoTemplate.saveEntity(schConf);
	}

	/**
	 * getSchConfPage
	 * @param page
	 * @return
	 */
	public PageModel<SchConf> getSchConfPage(PageModel<SchConf> page) {
		page = baseMongoTemplate.getListPage(page, SchConf.class);
		return page;
	}

	/**
	 * findAllSchConf
	 * @return
	 */
	public List<SchConf> findAllSchConf() {
		return baseMongoTemplate.findAll(SchConf.class);
	}

	/**
	 * getSchConf
	 * @param id
	 * @return
	 */
	public SchConf getSchConf(String id) {
		return (SchConf) baseMongoTemplate.getEntity("id", id, SchConf.class);
	}
	
	/**
	 * updateSchConf
	 * @param schConf
	 * @return
	 */
	public int updateSchConf(SchConf schConf) {
		String id = schConf.getId();
		return baseMongoTemplate.updateEntity(id, schConf, SchConf.class);
	}

	/**
	 * saveSchConf
	 * @param schConf
	 * @return
	 */
	public int saveSchConf(SchConf schConf) {
		return baseMongoTemplate.saveEntity(schConf);
	}

}
