package org.bham.btcert.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bham.btcert.model.PageModel;
import org.bham.btcert.model.SchConf;
import org.bham.btcert.model.StuRevoInfo;
import org.bham.btcert.model.Users;
import org.bham.btcert.model.identity.IdentityClaim;
import org.bham.btcert.model.identity.SchIdentity;
import org.bham.btcert.service.AdminService;
import org.bham.btcert.utils.CryptoUtil;
/*import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;

/**
 * 
 * @Title: AdminController.java
 * @Package org.bham.btcert.controller
 * @Description: the controller page for admin page
 * @author rxl635@student.bham.ac.uk
 * @version V1.0
 */
@Component
@ConfigurationProperties
@RestController
@RequestMapping("/admin")
public class AdminController {

	@Value("${application.message}")
	String message;

	@Value("${application.appname}")
	String appname;

	@Autowired
	private AdminService adminService;

	/**
	 * 
	 * get school identity by pages
	 * 
	 * @return
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "postSchIdentityList", method = RequestMethod.POST)
	public String postSchIdentityList(@RequestBody String queryCondition) {
		// queryCondition = CryptoUtil.base64Decode(queryCondition);
		PageModel<SchIdentity> pm = JSON.parseObject(queryCondition, PageModel.class);
		PageModel<SchIdentity> pms = adminService.getSchIdentityPage(pm);
		String jsonString = JSON.toJSONString(pms);
		return jsonString;
	}

	/**
	 * get all school identity
	 * 
	 * @return
	 */
	@RequestMapping(value = "getAllSchIdentity", method = RequestMethod.GET)
	public String getAllSchIdentity() {
		List<SchIdentity> list = adminService.findAllSchIdentity();
		String jsonString = JSON.toJSONString(list);
		return jsonString;
	}

	/**
	 *
	 * update school identity
	 * 
	 * @return
	 */
	@RequestMapping(value = "updateSchIdentity/{data}", method = RequestMethod.GET)
	public String updateSchIdentity(@PathVariable String data) {
		data = CryptoUtil.base64Decode(data);
		SchIdentity schIdentity = JSON.parseObject(data, SchIdentity.class);
		int success = adminService.updateSchIdentity(schIdentity);
		String jsonString = "{\"success\":\"" + success + "\"}";
		return jsonString;
	}

	/**
	 * 
	 * get school identity
	 * 
	 * @return
	 */
	@RequestMapping(value = "getSchIdentity/{id}", method = RequestMethod.GET)
	public String getSchIdentity(@PathVariable String id) {
		id = CryptoUtil.base64Decode(id);
		SchIdentity schIdentity = adminService.getSchIdentity(id);
		String jsonString = JSON.toJSONString(schIdentity);
		return jsonString;
	}

	/**
	 * 
	 * init and save the school identity data, this method is only for testing
	 * 
	 * @return
	 */
	@RequestMapping(value = "initSchIdentityData", method = RequestMethod.GET)
	public String initSchIdentityData() {
		/*
		 * pub: MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDTSXz+hLDdKjFRRQKJEyXXBJ6E
		 * QZcqNSlTy6Ozfly/uz4xs8weePVhmy3ZluuqnyeU+zrUDXyXF36NIXdm26EEyXDZ
		 * 8tNw5sEaeIKh48tDuTwTcMLKlN5XKxCHvBZ88X6brbeG82Hk+XIkA7LsSfrktGrA
		 * u10a5/+Z1CpIDdu2CwIDAQAB
		 */

		/*
		 * pri: MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANNJfP6EsN0qMVFF
		 * AokTJdcEnoRBlyo1KVPLo7N+XL+7PjGzzB549WGbLdmW66qfJ5T7OtQNfJcXfo0h
		 * d2bboQTJcNny03DmwRp4gqHjy0O5PBNwwsqU3lcrEIe8Fnzxfputt4bzYeT5ciQD
		 * suxJ+uS0asC7XRrn/5nUKkgN27YLAgMBAAECgYAsQUVEDkj5WiAQVLMOQQt4+Qe2
		 * zgI6nf31Bj9YlkwZAMArKuzDKTTEZ/t3iuv7xfBFfUW6KsUA5LPmHa9jxPb5pu5E
		 * GaRvIUQqr7njzJN5Dj5ur39QEeSEH9oBO2iKsivBFDVF3spP90d6ULHn60joNQPO
		 * 4/F6m8xxfoD1bvXFKQJBAPz01bR9nSikudEEoFNVx05zuIfkGMtS+tKlTO9Owd+9
		 * 0ous02SKElXPDQkRJ6h6wBdzAR1CsWlPF/yjkIOH2NUCQQDV1E1Zy7uUICVG0l24
		 * iAfFQqPxIbDs8tiGVMr9PEmTGMTOmRG8dL5DV5E9um4FBFIU8F14djs4eT5ek5rX
		 * KMNfAkAvAwHEraPLrfGq2wVA96w29GLTPp0tDJr5EyedQjHd91VuCAyxnseRkw2F
		 * Jd8M/OM+eG04lvCJ9d0rYuq0x0aRAkAmdgVjvW2D+zQ5ROVup6keJhpu6Ox18khY
		 * ATBx1EI3nyDnOEkkaKtAWaPaQoo/hMNu2L03SazlP4mkYVBcmwxpAkEAjC5oovVs
		 * YA32UfbYBLne9HG/wD9bHu2/p4UbEYUtL8iLcw2hpReahQcStr+IFj9CIbvq9219
		 * 3m5zo5QI46xkSg==
		 */

		SchIdentity schIdentity = new SchIdentity();

		schIdentity.setId(CryptoUtil.getUUID());
		List<Map<String, String>> btc_address_list = new ArrayList<Map<String, String>>();
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("3DWwmE19gHdv3xM3zPrVwqoRmbJQ6cCPHd", "1500942301");
		map1.put("3LQ7QbYi6tL8fT5HQ6qKc96xkVyzxzNngE", "1469406301");
		btc_address_list.add(map1);
		schIdentity.setBtc_address_list(btc_address_list);
		schIdentity.setBtc_address_list_hash("f24ed3c811777c00b94d53b81c4d0413f94649f9227f4d864b3fb1e793a1d3e3");

		List<IdentityClaim> identityClaimList = new ArrayList<IdentityClaim>();

		IdentityClaim identityClaim1 = new IdentityClaim();
		identityClaim1.setPublic_key("http://www.bham.ac.uk/uob/pub.asc");
		identityClaim1.setSignature(
				"YDFeWyueIallhvdNcoKTT1D8Z4WrmbVsyOYNdUmtWsOcFMzca0k4AopJPwu4Gw83JcU91q/UNDyel6JOZRJU93RPz6f7b7JmQwZzZT8XN6322z7RQDVXboPx8Uym+ZsIGgEtFVxsMkxRlp5S4cerOgdwtF4duRJwNrzKLsfnDis=");
		identityClaim1
				.setCheck_value("www.bham.ac.uk/uob#f24ed3c811777c00b94d53b81c4d0413f94649f9227f4d864b3fb1e793a1d3e3");

		IdentityClaim identityClaim2 = new IdentityClaim();
		identityClaim2.setPublic_key("https://twitter.com/uob/pub.asc");
		identityClaim2.setSignature(
				"YDFeWyueIallhvdNcoKTT1D8Z4WrmbVsyOYNdUmtWsOcFMzca0k4AopJPwu4Gw83JcU91q/UNDyel6JOZRJU93RPz6f7b7JmQwZzZT8XN6322z7RQDVXboPx8Uym+ZsIGgEtFVxsMkxRlp5S4cerOgdwtF4duRJwNrzKLsfnDis=");
		identityClaim2
				.setCheck_value("twitter.com/uob#f24ed3c811777c00b94d53b81c4d0413f94649f9227f4d864b3fb1e793a1d3e3");

		IdentityClaim identityClaim3 = new IdentityClaim();
		identityClaim3.setPublic_key("http://www.facebook.ac.uk/uob/pub.asc");
		identityClaim3.setSignature(
				"YDFeWyueIallhvdNcoKTT1D8Z4WrmbVsyOYNdUmtWsOcFMzca0k4AopJPwu4Gw83JcU91q/UNDyel6JOZRJU93RPz6f7b7JmQwZzZT8XN6322z7RQDVXboPx8Uym+ZsIGgEtFVxsMkxRlp5S4cerOgdwtF4duRJwNrzKLsfnDis=");
		identityClaim3.setCheck_value(
				"www.facebook.ac.uk/uob#f24ed3c811777c00b94d53b81c4d0413f94649f9227f4d864b3fb1e793a1d3e3");

		identityClaimList.add(identityClaim1);
		identityClaimList.add(identityClaim2);
		identityClaimList.add(identityClaim3);

		schIdentity.setIdentityClaimList(identityClaimList);

		int success = adminService.saveSchIdentity(schIdentity);

		String jsonString = "{\"success\":\"" + success + "\"}";
		return jsonString;
	}

	/**
	 * 
	 * @param queryCondition
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getStuRevoInfoList", method = RequestMethod.POST)
	public String getStuRevoInfoList(@RequestBody String queryCondition) {

		// queryCondition = CryptoUtil.base64Decode(queryCondition);
		System.out.println(queryCondition + "DDDDDDD");
		PageModel<StuRevoInfo> pm = JSON.parseObject(queryCondition, PageModel.class);
		PageModel<StuRevoInfo> pms = adminService.getStuRevoInfoPage(pm);
		String jsonString = JSON.toJSONString(pms);
		return jsonString;
	}

	/**
	 * get all student revocation information
	 * 
	 * @return
	 */
	@RequestMapping(value = "getAllStuRevoInfo", method = RequestMethod.GET)
	public String getAllStuRevoInfo() {
		List<StuRevoInfo> list = adminService.findAllStuRevoInfo();
		String jsonString = JSON.toJSONString(list);
		return jsonString;
	}

	/**
	 * getStuRevoInfo
	 * 
	 * @return
	 */
	@RequestMapping(value = "getStuRevoInfo/{id}", method = RequestMethod.GET)
	public String getStuRevoInfo(@PathVariable String id) {
		id = CryptoUtil.base64Decode(id);
		StuRevoInfo stuRevoInfo = adminService.getStuRevoInfo(id);
		String jsonString = JSON.toJSONString(stuRevoInfo);
		return jsonString;
	}

	/**
	 * delete student revocation information
	 * 127.0.0.1:8080/admin/delStuRevoInfo/'1','2'
	 * 
	 * @return
	 */
	@RequestMapping(value = "delStuRevoInfo/{delIds}", method = RequestMethod.GET)
	public String delStuRevoInfo(@PathVariable String delIds) {
		delIds = CryptoUtil.base64Decode(delIds);
		int success = adminService.delStuRevoInfo(delIds);
		String jsonString = "{\"success\":\"" + success + "\"}";
		return jsonString;
	}

	/**
	 * save student revocation information
	 * 
	 * @return
	 */
	@RequestMapping(value = "saveStuRevoInfo/{data}", method = RequestMethod.GET)
	public String saveStuRevoInfo(@PathVariable String data) {

		/*
		 * NetworkParameters params = MainNetParams.get(); ECKey key = new
		 * ECKey(); String rb_pri_key =
		 * key.getPrivateKeyEncoded(params).toString(); String rb_address =
		 * key.toAddress(params).toString();
		 * 
		 * StuRevoInfo stuRevoInfo = new StuRevoInfo();
		 * stuRevoInfo.setId(CryptoUtil.getUUID());
		 * stuRevoInfo.setU_id("ceshi"); stuRevoInfo.setU_name("student_ceshi");
		 * 
		 * ECKey keyTemp = new ECKey(); String r_pri_key =
		 * keyTemp.getPrivateKeyEncoded(params).toString(); String r_addrsss =
		 * keyTemp.toAddress(params).toString();
		 * 
		 * stuRevoInfo.setR_address(r_addrsss);
		 * stuRevoInfo.setR_pri_key(r_pri_key);
		 * 
		 * stuRevoInfo.setRb_address(rb_address);
		 * stuRevoInfo.setRb_pri_key(rb_pri_key);
		 */

		data = CryptoUtil.base64Decode(data);
		StuRevoInfo stuRevoInfo = JSON.parseObject(data, StuRevoInfo.class);

		int success = adminService.saveStuRevoInfo(stuRevoInfo);
		String jsonString = "{\"success\":\"" + success + "\"}";
		return jsonString;
	}

	/**
	 * update student revocation information
	 * 
	 * @return
	 */
	@RequestMapping(value = "updateStuRevoInfo/{stuConfInfoString}", method = RequestMethod.GET)
	public String updateStuRevoInfo(@PathVariable String stuConfInfoString) {

		stuConfInfoString = CryptoUtil.base64Decode(stuConfInfoString);
		StuRevoInfo stuRevoInfo = JSON.parseObject(stuConfInfoString, StuRevoInfo.class);
		int success = adminService.updateStuRevoInfo(stuRevoInfo);
		String jsonString = "{\"success\":\"" + success + "\"}";
		return jsonString;
	}

	/**
	 * @deprecated
	 * @return
	 */
	@RequestMapping(value = "initStuRevoInfoData", method = RequestMethod.GET)
	public String initStuRevoInfoData() {
		List<StuRevoInfo> stuRevoInfoList = new ArrayList<StuRevoInfo>();
		/*
		NetworkParameters params = MainNetParams.get();
		ECKey key = new ECKey();
		String rb_pri_key = key.getPrivateKeyEncoded(params).toString();
		String rb_address = key.toAddress(params).toString();

		for (int i = 0; i < 100; i++) {
			StuRevoInfo stuRevoInfo = new StuRevoInfo();
			stuRevoInfo.setId(CryptoUtil.getUUID());
			stuRevoInfo.setU_id(i + "");
			stuRevoInfo.setU_name("student" + i);

			ECKey keyTemp = new ECKey();
			String r_pri_key = keyTemp.getPrivateKeyEncoded(params).toString();
			String r_addrsss = keyTemp.toAddress(params).toString();

			stuRevoInfo.setR_address(r_addrsss);
			stuRevoInfo.setR_pri_key(r_pri_key);

			stuRevoInfo.setRb_address(rb_address);
			stuRevoInfo.setRb_pri_key(rb_pri_key);
			stuRevoInfo.setCstate("valid");

			stuRevoInfoList.add(stuRevoInfo);
		}*/

		int success = adminService.saveStuRevoInfoBatch(stuRevoInfoList);
		String jsonString = "{\"success\":\"" + success + "\"}";
		return jsonString;
	}

	/**
	 * get user list
	 * 
	 * @param queryCondition
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "postUsersList", method = RequestMethod.POST)
	public String postUsersList(@RequestBody String queryCondition) {
		// queryCondition = CryptoUtil.base64Decode(queryCondition);
		PageModel<Users> pm = JSON.parseObject(queryCondition, PageModel.class);
		PageModel<Users> pms = adminService.getUsersPage(pm);
		String jsonString = JSON.toJSONString(pms);
		return jsonString;
	}

	/**
	 * get school configure list
	 * 
	 * @param queryCondition
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "postSchConfList", method = RequestMethod.POST)
	public String postSchConfList(@RequestBody String queryCondition) {
		// queryCondition = CryptoUtil.base64Decode(queryCondition);
		PageModel<SchConf> pm = JSON.parseObject(queryCondition, PageModel.class);
		PageModel<SchConf> pms = adminService.getSchConfPage(pm);
		String jsonString = JSON.toJSONString(pms);
		return jsonString;
	}

	/**
	 * 
	 * get all the school configure
	 * 
	 * @return
	 */
	@RequestMapping(value = "getAllSchConf", method = RequestMethod.GET)
	public String getAllSchConf() {
		List<SchConf> list = adminService.findAllSchConf();
		String jsonString = JSON.toJSONString(list);
		return jsonString;
	}

	/**
	 * get the school configure
	 * 
	 * @return
	 */
	@RequestMapping(value = "getSchConf/{id}", method = RequestMethod.GET)
	public String getSchConf(@PathVariable String id) {
		id = CryptoUtil.base64Decode(id);
		SchConf schConf = adminService.getSchConf(id);
		String jsonString = JSON.toJSONString(schConf);
		return jsonString;
	}

	/**
	 * update the school configure
	 * 
	 * @return
	 */
	@RequestMapping(value = "updateSchConf/{data}", method = RequestMethod.GET)
	public String updateSchConf(@PathVariable String data) {
		data = CryptoUtil.base64Decode(data);
		SchConf schConf = JSON.parseObject(data, SchConf.class);
		int success = adminService.updateSchConf(schConf);
		String jsonString = "{\"success\":\"" + success + "\"}";
		return jsonString;
	}

	/**
	 * init data
	 * 
	 * @deprecated
	 * @return
	 */
	@RequestMapping(value = "initSchConfData", method = RequestMethod.GET)
	public String initSchConfData() {
		SchConf schConf = new SchConf();
		schConf.setEmail("admin@bham.ac.uk");
		schConf.setName("University of Birmingham");
		schConf.setId(CryptoUtil.getUUID());
		schConf.setUrl("http://www.bham.ac.uk");
		schConf.setImage("http://www.bham.ac.uk/test.png");
		int success = adminService.saveSchConf(schConf);
		String jsonString = "{\"success\":\"" + success + "\"}";
		return jsonString;
	}

}
