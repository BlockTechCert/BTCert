package org.bham.btcert.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.bham.btcert.model.PageModel;
import org.bham.btcert.model.RevokeTansacInfo;
import org.bham.btcert.model.StuRevoInfo;
import org.bham.btcert.model.TansacInfo;
import org.bham.btcert.model.certificate.Anchors;
import org.bham.btcert.model.certificate.Badge;
import org.bham.btcert.model.certificate.CertsInfo;
import org.bham.btcert.model.certificate.FileClaim;
import org.bham.btcert.model.certificate.IdentityClaim;
import org.bham.btcert.model.certificate.Issuer;
import org.bham.btcert.model.certificate.Openbadges;
import org.bham.btcert.model.certificate.OperateDetail;
import org.bham.btcert.model.certificate.Recipient;
import org.bham.btcert.model.certificate.RevocationClaim;
import org.bham.btcert.model.certificate.Signature;
import org.bham.btcert.model.certificate.Verification;
import org.bham.btcert.model.identity.SchIdentity;
import org.bham.btcert.service.AdminService;
import org.bham.btcert.service.IssuerService;
import org.bham.btcert.utils.CryptoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import info.blockchain.api.APIException;

/**
 * 
 * @Title: IssuerController.java
 * @Package org.bham.btcert.controller
 * @Description: TODO the controller page for Issuer page
 * @author rxl635@student.bham.ac.uk
 * @version V1.0
 */
@Component
@ConfigurationProperties
@RestController
@RequestMapping("/issuer")
public class IssuerController {

	@Value("${application.message}")
	String message;

	@Value("${application.appname}")
	String appname;

	@Autowired
	private IssuerService issuerService;

	/*
	 * @Autowired private CheckerService checkerService;
	 */

	@Autowired
	private AdminService adminService;

	/**
	 * this method is for testing
	 * 
	 * @deprecated
	 * @return
	 * @throws IOException
	 * @throws APIException
	 */
	@RequestMapping(value = "getUnspentOutputs", method = RequestMethod.GET)
	public String getUnspentOutputs(HttpServletRequest request) throws APIException, IOException {
		/*
		 * List<SchIdentity> schIdentityList =
		 * adminService.findAllSchIdentity(); SchIdentity schIdentity =
		 * schIdentityList.get(0); // 获取学校认证配置 String recycleAddress =
		 * schIdentity.getLatest_recycle_address(); String btcaddress =
		 * schIdentity.getBtc_address_list().get(0).keySet().iterator().next();
		 * String merkleRoot = "lirujia";
		 */
		String jsonString = "{\"success\":\"" + 0 + "\"}";
		return jsonString;
	}

	/**
	 * get transaction information by pages
	 * 
	 * @param queryCondition
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "postTansacInfoList", method = RequestMethod.POST)
	public String postTansacInfoList(@RequestBody String queryCondition) {
		PageModel<TansacInfo> pm = JSON.parseObject(queryCondition, PageModel.class);
		PageModel<TansacInfo> pms = issuerService.getTansacInfoPage(pm);
		String jsonString = JSON.toJSONString(pms);
		return jsonString;
	}

	/**
	 * post the revoked transaction
	 * 
	 * @param tansacInfoJson
	 * @return
	 */
	@RequestMapping(value = "postRevokeTansacInfo", method = RequestMethod.POST)
	public String postRevokeTansacInfo(@RequestBody String tansacInfoJson) {
		RevokeTansacInfo revoketansacInfo = JSON.parseObject(tansacInfoJson, RevokeTansacInfo.class);
		String jsonString;
		if (revoketansacInfo.getId() == null || "".equals(revoketansacInfo.getId())) {
			revoketansacInfo.setId(CryptoUtil.getUUID());
			revoketansacInfo.setBroadcast_state("0");

			// update state
			StuRevoInfo stuRevoInfo = adminService.getStuRevoInfo(revoketansacInfo.getRevoke_id());
			stuRevoInfo.setCstate("waiting revoked");
			adminService.updateStuRevoInfo(stuRevoInfo);

			int success = issuerService.saveRevokeTansacInfo(revoketansacInfo);
			jsonString = "{\"success\":\"" + success + "\"}";
		} else {
			int success = issuerService.updateRevokeTansacInfo(revoketansacInfo);
			jsonString = "{\"success\":\"" + success + "\"}";
		}
		return jsonString;
	}

	/**
	 * get revoked transaction by pages
	 * 
	 * @param queryCondition
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "postRevokeTansacInfoList", method = RequestMethod.POST)
	public String postRevokeTansacInfoList(@RequestBody String queryCondition) {
		PageModel<RevokeTansacInfo> pm = JSON.parseObject(queryCondition, PageModel.class);
		PageModel<RevokeTansacInfo> pms = issuerService.getRevokeTansacInfoPage(pm);
		String jsonString = JSON.toJSONString(pms);
		return jsonString;
	}

	/**
	 * update revoked transaction by pages
	 * 
	 * @return
	 */
	@RequestMapping(value = "signRevokeTansacInfo", method = RequestMethod.POST)
	public String signRevokeTansacInfo(@RequestBody String tansacInfoJson) {
		RevokeTansacInfo RevokeTansacInfo = JSON.parseObject(tansacInfoJson, RevokeTansacInfo.class);
		RevokeTansacInfo oldRevokeTansacInfo = issuerService.getRevokeTansacInfo(RevokeTansacInfo.getId());

		if (oldRevokeTansacInfo == null) {
			return "{\"success\":\"0\"}";
		}

		String sign_state = RevokeTansacInfo.getSign_state();
		String old_sign_state = oldRevokeTansacInfo.getSign_state();
		String new_sign_state = "";

		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String handle_time = format.format(date);

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUserName = userDetails.getUsername(); // get current user
															// name

		new_sign_state = old_sign_state + currentUserName + "_" + handle_time + sign_state + ",";
		System.out.println(new_sign_state);
		oldRevokeTansacInfo.setSign_state(new_sign_state);
		oldRevokeTansacInfo.setSign_number(RevokeTansacInfo.getSign_number());

		if (sign_state.substring(0, 6).equals("_agree")) {

			oldRevokeTansacInfo.setInput(RevokeTansacInfo.getInput());
			oldRevokeTansacInfo.setOutput(RevokeTansacInfo.getOutput());
			oldRevokeTansacInfo.setRaw_tran(RevokeTansacInfo.getRaw_tran());
			oldRevokeTansacInfo.setSignatures(RevokeTansacInfo.getSignatures());
		}

		int signNum = Integer.parseInt(RevokeTansacInfo.getSign_number());

		if (signNum >= 2) { // 改变原来的状态
			StuRevoInfo stuRevoInfo = adminService.getStuRevoInfo(oldRevokeTansacInfo.getRevoke_id());

			if (pattern(oldRevokeTansacInfo.getSign_state(), "reject") >= 2) {
				stuRevoInfo.setCstate("revoked reject");
			} else {
				stuRevoInfo.setCstate("revoked confirmed");
			}
			adminService.updateStuRevoInfo(stuRevoInfo);
		}

		int success = issuerService.updateRevokeTansacInfo(oldRevokeTansacInfo);
		String jsonString = "{\"success\":\"" + success + "\"}";
		return jsonString;
	}

	/**
	 * regular expression
	 * 
	 * @param text,
	 *            指定的字符串
	 * @return count
	 */
	private static int pattern(String text, String subtext) {
		Pattern pattern = Pattern.compile(subtext);
		Matcher matcher = pattern.matcher(text);
		int count = 0;
		while (matcher.find()) { // 如果匹配,则数量+1
			count++;
		}
		return count;
	}

	/**
	 * get revoked transaction by pages
	 * 
	 * @return
	 */
	@RequestMapping(value = "getRevokeTansacInfo/{id}", method = RequestMethod.GET)
	public String getRevokeTansacInfo(@PathVariable String id) {
		// id = CryptoUtil.base64Decode(id);
		RevokeTansacInfo RevokeTansacInfo = issuerService.getRevokeTansacInfo(id);
		String jsonString = JSON.toJSONString(RevokeTansacInfo);
		return jsonString;
	}

	/**
	 * get revoked transaction by address
	 * 
	 * @param revoke_address
	 * @return
	 */
	@RequestMapping(value = "getRevokeTansacInfobyAddress/{revoke_address}", method = RequestMethod.GET)
	public String getRevokeTansacInfobyAddress(@PathVariable String revoke_address) {
		// id = CryptoUtil.base64Decode(id);
		RevokeTansacInfo RevokeTansacInfo = issuerService.getRevokeTansacInfobyAddress(revoke_address);
		String jsonString = JSON.toJSONString(RevokeTansacInfo);
		return jsonString;
	}

	/**
	 * update all the revoked transaction by address
	 * @param queryCondition
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "updateAllRevokeTansacInfobyAddress/{queryCondition}", method = RequestMethod.GET)
	public String updateAllRevokeTansacInfobyAddress(@PathVariable String queryCondition) {
		// id = CryptoUtil.base64Decode(id);

		queryCondition = CryptoUtil.base64Decode(queryCondition);
		Map map = JSON.parseObject(queryCondition, Map.class);
		String revoke_address = String.valueOf(map.get("revoke_address"));
		String revoke_id = String.valueOf(map.get("revoke_id"));
		String txid = String.valueOf(map.get("txid"));
		String broadcast_success = String.valueOf(map.get("broadcast_success"));

		RevokeTansacInfo RevokeTansacInfo = issuerService.getRevokeTansacInfobyAddress(revoke_address);

		System.err.println(revoke_address + "########" + txid);
		// 更新回收交易
		RevokeTansacInfo.setBroadcast_success(broadcast_success);
		RevokeTansacInfo.setBroadcast_transaction_id(txid);
		issuerService.updateRevokeTansacInfo(RevokeTansacInfo);

		// 更新回收表
		StuRevoInfo StuRevoInfo = adminService.getStuRevoInfo(revoke_id);
		StuRevoInfo.setCstate("revoked successfully");
		adminService.updateStuRevoInfo(StuRevoInfo);

		// 更新证书
		CertsInfo CertsInfo = issuerService.getCertsInfoByRevocationAddress(revoke_address);
		CertsInfo.setCstate("revoked");
		issuerService.updateCertsInfo(CertsInfo);

		String jsonString = "{\"success\":\"1\"}";
		return jsonString;
	}

	/**
	 * get transactions for pages
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getTansacInfoList/{queryCondition}", method = RequestMethod.GET)
	public String getTansacInfoList(@PathVariable String queryCondition) {
		// String queryCondition =
		// "{'pageNumber':1,'pageSize':20,'queryObject':'{}','rowCount':1,'skip':20,'sortColumn':'u_name','sortType':'DESC'}";
		queryCondition = CryptoUtil.base64Decode(queryCondition);

		PageModel<TansacInfo> pm = JSON.parseObject(queryCondition, PageModel.class);
		PageModel<TansacInfo> pms = issuerService.getTansacInfoPage(pm);
		String jsonString = JSON.toJSONString(pms);
		return jsonString;
	}

	/**
	 * getCertsInfoListFormBroadcastId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getCertsInfoListFormBroadcastId/{queryCondition}", method = RequestMethod.GET)
	public String getCertsInfoListFormBroadcastId(@PathVariable String queryCondition) {
		// String queryCondition =
		// "{'pageNumber':1,'pageSize':20,'queryObject':'{}','rowCount':1,'skip':20,'sortColumn':'u_name','sortType':'DESC'}";
		queryCondition = CryptoUtil.base64Decode(queryCondition);

		PageModel<TansacInfo> pm = JSON.parseObject(queryCondition, PageModel.class);
		PageModel<TansacInfo> pms = issuerService.getTansacInfoPage(pm);

		PageModel<CertsInfo> pm1 = JSON.parseObject(queryCondition, PageModel.class);

		String transactionId = "";
		if (pms == null || pms.getRows().size() == 0) {
			transactionId = "";
		} else {
			transactionId = pms.getRows().get(0).getId();
		}

		pm1.setQueryObject("{'transactionId':'" + transactionId + "'}");
		PageModel<CertsInfo> pms1 = issuerService.getCertsInfoPage(pm1);
		String jsonString = JSON.toJSONString(pms1);
		return jsonString;
	}

	/**
	 * get all transactions
	 * @return
	 */
	@RequestMapping(value = "getAllTansacInfo", method = RequestMethod.GET)
	public String getAllTansacInfo() {
		List<TansacInfo> list = issuerService.findAllTansacInfo();
		String jsonString = JSON.toJSONString(list);
		return jsonString;
	}

	/**
	 * 按照分页获取交易信息
	 * get transaction by ids
	 * @return
	 */
	@RequestMapping(value = "getTansacInfo/{id}", method = RequestMethod.GET)
	public String getTansacInfo(@PathVariable String id) {
		// id = CryptoUtil.base64Decode(id);
		TansacInfo TansacInfo = issuerService.getTansacInfo(id);
		String jsonString = JSON.toJSONString(TansacInfo);
		return jsonString;
	}

	/**
	 * delete transaction by ids
	 * example 127.0.0.1:8080/admin/delTansacInfo/'1','2'
	 * @return
	 */
	@RequestMapping(value = "delTansacInfo/{delIds}", method = RequestMethod.GET)
	public String delTansacInfo(@PathVariable String delIds) {
		delIds = CryptoUtil.base64Decode(delIds);
		int success = issuerService.delTansacInfo(delIds);
		String jsonString = "{\"success\":\"" + success + "\"}";
		return jsonString;
	}

	/**
	 * 
	 * save transaction information
	 * @return
	 */
	@RequestMapping(value = "saveTansacInfo/{data}", method = RequestMethod.GET)
	public String saveTansacInfo(@PathVariable String data) {

		data = CryptoUtil.base64Decode(data);
		TansacInfo TansacInfo = JSON.parseObject(data, TansacInfo.class);
		int success = issuerService.saveTansacInfo(TansacInfo);
		String jsonString = "{\"success\":\"" + success + "\"}";
		return jsonString;
	}

	@RequestMapping(value = "postTansacInfo", method = RequestMethod.POST)
	public String postTansacInfo(@RequestBody String tansacInfoJson) {
		TansacInfo tansacInfo = JSON.parseObject(tansacInfoJson, TansacInfo.class);
		String jsonString;
		if (tansacInfo.getId() == null || "".equals(tansacInfo.getId())) {
			tansacInfo.setId(CryptoUtil.getUUID());
			tansacInfo.setBroadcast_state("0");
			tansacInfo.setSerialize_state("0");
			int success = issuerService.saveTansacInfo(tansacInfo);
			jsonString = "{\"success\":\"" + success + "\"}";
		} else { // 更新
			TansacInfo tansacInfoOld = issuerService.getTansacInfo(tansacInfo.getId());
			tansacInfo.setId(tansacInfoOld.getId());
			tansacInfo.setMerkleRoot(tansacInfoOld.getMerkleRoot());
			tansacInfo.setSignatures(tansacInfoOld.getSignatures());
			tansacInfo.setBroadcast_state("0");
			tansacInfo.setSerialize_state("1");
			int success = issuerService.updateTansacInfo(tansacInfo);
			jsonString = "{\"success\":\"" + success + "\"}";
		}
		return jsonString;
	}

	/**
	 * update the transaction information
	 * 
	 * @return
	 */
	@RequestMapping(value = "updateTansacInfo", method = RequestMethod.POST)
	public String updateTansacInfo(@RequestBody String tansacInfoJson) {
		TansacInfo TansacInfo = JSON.parseObject(tansacInfoJson, TansacInfo.class);
		int success = issuerService.updateTansacInfo(TansacInfo);
		String jsonString = "{\"success\":\"" + success + "\"}";
		return jsonString;
	}

	/**
	 * 
	 * sign the transaction
	 * @return
	 */
	@RequestMapping(value = "signTansacInfo", method = RequestMethod.POST)
	public String signTansacInfo(@RequestBody String tansacInfoJson) {
		TansacInfo TansacInfo = JSON.parseObject(tansacInfoJson, TansacInfo.class);

		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String handle_time = format.format(date);

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUserName = userDetails.getUsername(); // get current user name
		String sign_state = TansacInfo.getSign_state();
		sign_state = sign_state + currentUserName + "_" + handle_time + ",";
		TansacInfo.setSign_state(sign_state);

		int success = issuerService.updateTansacInfo(TansacInfo);
		String jsonString = "{\"success\":\"" + success + "\"}";
		return jsonString;
	}

	/**
	 * @deprecated
	 * init transaction information, this method is for testing
	 * @return
	 */
	@RequestMapping(value = "initTansacInfoData", method = RequestMethod.GET)
	public String initTansacInfoData() {
		List<TansacInfo> TansacInfoList = new ArrayList<TansacInfo>();

		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(date);

		for (int i = 0; i < 30; i++) {
			TansacInfo TansacInfo = new TansacInfo();
			TansacInfo.setId(CryptoUtil.getUUID());
			// TansacInfo.setCert_id("cert_id" + i);
			TansacInfo.setRaw_tran("XXXXXXX");
			TansacInfo.setTran_hash("056e761177d2c0c7607fa261899a41b325a8b2c70e79b3af99be3b8854613fda");
			TansacInfo.setSign_number("2");
			TansacInfo.setSign_state("siger1_" + time + ",siger2_0,siger3_0");
			TansacInfoList.add(TansacInfo);
		}

		int success = issuerService.saveTansacInfoBatch(TansacInfoList);
		String jsonString = "{\"success\":\"" + success + "\"}";
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
	 * get certificate information by pages 
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getCertsInfoList/{queryCondition}", method = RequestMethod.GET)
	public String getCertsInfoList(@PathVariable String queryCondition) {
		queryCondition = CryptoUtil.base64Decode(queryCondition);
		// String queryCondition =
		// "{'pageNumber':1,'pageSize':20,'queryObject':'{}','rowCount':1,'skip':20,'sortColumn':'u_name','sortType':'DESC'}";
		PageModel<CertsInfo> pm = JSON.parseObject(queryCondition, PageModel.class);
		PageModel<CertsInfo> pms = issuerService.getCertsInfoPage(pm);
		String jsonString = JSON.toJSONString(pms);
		return jsonString;
	}

	/**
	 * update certificate for broadcasting
	 * @param queryCondition
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "updateCertsInfoListForBoradCast/{queryCondition}", method = RequestMethod.GET)
	public String updateCertsInfoListForBoradCast(@PathVariable String queryCondition) {
		String jsonString = "";
		queryCondition = CryptoUtil.base64Decode(queryCondition);
		Map map = JSON.parseObject(queryCondition, Map.class);
		String ids = String.valueOf(map.get("ids"));
		String txid = String.valueOf(map.get("txid"));
		String broadcast_success = String.valueOf(map.get("broadcast_success"));

		System.err.println(ids);

		List<CertsInfo> list = issuerService.getCertsInfoListByTranId(ids);

		if (list == null) {
			jsonString = "{\"success\":\"0\"}";
		} else {
			for (int i = 0; i < list.size(); i++) {
				CertsInfo certsInfo = list.get(i);

				List<Anchors> anchorslist = new ArrayList<Anchors>();
				Anchors anchors = new Anchors();
				anchors.setSourceId(txid);
				anchors.setType("BTCOpReturn");
				anchorslist.add(anchors);
				certsInfo.getOpenbadges().getSignature().setAnchors(anchorslist);
				if ("1".equals(broadcast_success)) {
					certsInfo.setCstate("valid");
				}
				issuerService.updateCertsInfo(certsInfo);
			}
			TansacInfo tansacInfo = issuerService.getTansacInfo(ids);
			tansacInfo.setBroadcast_success(broadcast_success);
			tansacInfo.setBroadcast_transaction_id(txid);
			tansacInfo.setBroadcast_state("1");
			issuerService.updateTansacInfo(tansacInfo);
			jsonString = "{\"success\":\"1\"}";
		}

		return jsonString;
	}

	/**
	 * 
	 * get certificate information
	 * 
	 * @return
	 */
	@RequestMapping(value = "getCertsInfo/{id}", method = RequestMethod.GET)
	public String getCertsInfo(@PathVariable String id) {
		id = CryptoUtil.base64Decode(id);
		CertsInfo StuConf = issuerService.getCertsInfo(id);
		String jsonString = JSON.toJSONString(StuConf);
		return jsonString;
	}

	/**
	 * @deprecated init data for testing
	 * @return
	 */
	@RequestMapping(value = "initCertsInfoData", method = RequestMethod.GET)
	public String initCertsInfoData() {
		List<CertsInfo> CertsInfoList = new ArrayList<CertsInfo>();

		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String createTime = format.format(date);

		for (int i = 0; i < 10; i++) {
			CertsInfo TansacInfo = new CertsInfo();

			String id = CryptoUtil.getUUID();
			TansacInfo.setCreateTime(createTime);
			TansacInfo.setCstate("new"); // 状态
			TansacInfo.setId(id);
			TansacInfo.setRecipient("rujia" + i);

			List<OperateDetail> odl = new ArrayList<OperateDetail>();
			OperateDetail OperateDetail = new OperateDetail();
			OperateDetail.setCert_id(id);
			OperateDetail.setOper_time(createTime);
			OperateDetail.setOper_type("add");
			OperateDetail.setUser_id("user1");
			OperateDetail.setUser_name("username1");
			odl.add(OperateDetail);

			TansacInfo.setOdl(odl); // 操作细节

			Openbadges openbadges = new Openbadges();

			List<String> context = new ArrayList<String>();
			context.add("https://w3id.org/openbadges/v2");
			context.add("https://w3id.org/blockcerts/v2");
			context.add("https://blocktechcert.github.io/www/json/context.json");
			openbadges.setContext(context);

			openbadges.setType("BadgeClass");

			openbadges.setId("tsetId" + i);

			Recipient recipient = new Recipient();
			recipient.setHashed("false");
			recipient.setIdentity("rujia@bham.ac.uk");
			recipient.setType("email");
			openbadges.setRecipient(recipient);
			openbadges.setIssuedOn(createTime);

			Badge badge = new Badge();
			badge.setId("https://example.org/robotics-badge.json");
			badge.setType("Certificate");
			badge.setName("Awesome Badge");
			badge.setDescription("this is a test");
			badge.setImage("https://www.bham.ac.uk/test.png");

			Issuer issuer = new Issuer();
			issuer.setEmail("btcert@bham.ac.uk");
			issuer.setId("id");
			issuer.setImage("http://www.bham.ac.uk/issuer.png");
			issuer.setName("University of Birmingham");
			issuer.setType("Profile");
			issuer.setUrl("http://www.bham.ac.uk");
			badge.setIssuer(issuer);

			FileClaim fileClaim = new FileClaim();
			fileClaim.setFilehash("hash");
			fileClaim.setFilestore("PDF");
			List<String> ftypelist = new ArrayList<String>();
			ftypelist.add("fileClaim");
			ftypelist.add("Extension");
			fileClaim.setType(ftypelist);

			badge.setFileClaim(fileClaim);

			IdentityClaim identityClaim = new IdentityClaim();
			identityClaim.setBTCaddress("n1rEmrTY29VuNb9KcYxkwiJ5JjvFv6p6SR");
			identityClaim.setIdentityName("University of Birmingham");
			identityClaim.setValidationtime("2020-09-01");
			List<String> itypelist = new ArrayList<String>();
			itypelist.add("identityClaim");
			itypelist.add("Extension");
			identityClaim.setType(itypelist);

			badge.setIdentityClaim(identityClaim);

			RevocationClaim revocationClaim = new RevocationClaim();
			revocationClaim.setBatchRevocationAddress("15vq6bvfzcWQk9K8AsLvzfejj5Kd7TBfFy");
			revocationClaim.setRevocationAddress("1Kw43msvozjqwce6QqaM4Cj42TCZcoYWaV");
			List<String> rtypelist = new ArrayList<String>();
			rtypelist.add("revocationClaim");
			rtypelist.add("Extension");
			revocationClaim.setType(rtypelist);

			badge.setRevocationClaim(revocationClaim);

			openbadges.setBadge(badge);
			Verification verification = new Verification();
			List<String> vtypelist = new ArrayList<String>();
			vtypelist.add("MerkleProofVerification2017");
			vtypelist.add("Extension");
			verification.setType(vtypelist);

			openbadges.setVerification(verification);

			Signature signature = new Signature();
			List<Anchors> anchorslist = new ArrayList<Anchors>();
			Anchors anchors = new Anchors();
			anchors.setSourceId("f3be82fe1b5d8f18e009cb9a491781289d2e01678311fe2b2e4e84381aafadee");
			anchors.setType("BTCOpReturn");
			anchorslist.add(anchors);
			signature.setAnchors(anchorslist);
			signature.setContext("https://w3id.org/chainpoint/v2");
			signature.setMerkleRoot("51296468ea48ddbcc546abb85b935c73058fd8acdb0b953da6aa1ae966581a7a");

			List<HashMap<String, String>> proof = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> hash = new HashMap<String, String>();
			hash.put("left", "bdf8c9bdf076d6aff0292a1c9448691d2ae283f2ce41b045355e2c8cb8e85ef2");
			hash.put("left", "cb0dbbedb5ec5363e39be9fc43f56f321e1572cfcf304d26fc67cb6ea2e49faf");
			hash.put("right", "cb0dbbedb5ec5363e39be9fc43f56f321e1572cfcf304d26fc67cb6ea2e49faf");
			proof.add(hash);
			signature.setProof(proof);
			signature.setTargetHash("bdf8c9bdf076d6aff0292a1c9448691d2ae283f2ce41b045355e2c8cb8e85ef2");
			List<String> typelist = new ArrayList<String>();
			typelist.add("MerkleProof2017");
			typelist.add("Extension");
			signature.setTypelist(typelist);

			openbadges.setSignature(signature);

			TansacInfo.setOpenbadges(openbadges);

			CertsInfoList.add(TansacInfo);
		}

		int success = issuerService.saveCertsInfoBatch(CertsInfoList);
		String jsonString = "{\"success\":\"" + success + "\"}";
		return jsonString;
	}
}
