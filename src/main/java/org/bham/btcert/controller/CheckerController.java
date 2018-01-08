package org.bham.btcert.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.bham.btcert.model.CheckInfo;
import org.bham.btcert.model.PageModel;
import org.bham.btcert.model.SchConf;
import org.bham.btcert.model.StuConf;
import org.bham.btcert.model.StuRevoInfo;
import org.bham.btcert.model.TansacInfo;
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
import org.bham.btcert.service.CheckerService;
import org.bham.btcert.service.IssuerService;
import org.bham.btcert.service.StudentService;
import org.bham.btcert.utils.CHexConver;
import org.bham.btcert.utils.CryptoUtil;
import org.bham.btcert.utils.merkle.MerkleTree;
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

/**
 * 
 * @Title: CheckerController.java
 * @Package org.bham.btcert.controller
 * @Description: TODO the controller page for checker page
 * @author rxl635@student.bham.ac.uk
 * @version V1.0
 */
@Component
@ConfigurationProperties
@RestController
@RequestMapping("/checker")
public class CheckerController {

	@Value("${application.message}")
	String message;

	@Value("${application.appname}")
	String appname;

	@Autowired
	private CheckerService checkerService;

	@Autowired
	private StudentService studentService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private IssuerService issuerService;

	/**
	 * merge the certificate
	 * 
	 * @return
	 */
	@RequestMapping(value = "mergeCertificate", method = RequestMethod.POST)
	public String mergeCertificate(HttpServletRequest request) {

		String certsId = request.getParameter("certsId");
		String curTranId = request.getParameter("curTranId");

		List<CertsInfo> listold = null;
		if (curTranId != null && !"".equals(curTranId)) {
			listold = checkerService.getCertsInfoByTranId(curTranId);
		}

		List<CertsInfo> list = new ArrayList<CertsInfo>();
		List<Signature> slist = new ArrayList<Signature>();
		MerkleTree merkleTree = new MerkleTree();

		if (certsId == null || "".equals(certsId)) {
			list = checkerService.getCertsInfoByCstate("new");
		} else {
			list = checkerService.getCertsInfoByIds(certsId);
		}

		if (listold != null && listold.size() > 0) {
			list.addAll(listold);
		}

		for (int i = 0; i < list.size(); i++) {
			CertsInfo certsInfo = list.get(i);
			String badgeName = certsInfo.getOpenbadges().getBadge().getName();
			String issuerName = certsInfo.getOpenbadges().getBadge().getIssuer().getName();
			String recipientName = certsInfo.getOpenbadges().getRecipient().getIdentity();
			String hashValue = badgeName + issuerName + recipientName;
			merkleTree.addLeaf(hashValue, true);
		}

		merkleTree.makeTree();

		for (int i = 0; i < list.size(); i++) {
			slist.add(merkleTree.makeReceiptObject(i));
		}

		// List<CertsInfo> newlist = new ArrayList<CertsInfo>();
		int success = 0;

		String merkleRoot = merkleTree.getMerkleRoot();
		String tansacInfoId = CryptoUtil.getUUID();
		TansacInfo tansacInfo = null;
		if (!"".equals(curTranId)) { // 创建新的
			tansacInfo = issuerService.getTansacInfo(curTranId);
			tansacInfo.setMerkleRoot(merkleRoot);
			tansacInfo.setCerts_number(list.size() + "");
			tansacInfoId = tansacInfo.getId();
			// tansacInfo.setCertIdList(cl);
		} else {
			tansacInfo = new TansacInfo();
			tansacInfo.setId(tansacInfoId);
			// tansacInfo.setCertIdList(cl);
			tansacInfo.setMerkleRoot(merkleRoot);
			tansacInfo.setCerts_number(list.size() + "");
			tansacInfo.setRaw_tran("none");
			tansacInfo.setSign_number("0");
			tansacInfo.setSerialize_state("0");
			tansacInfo.setBroadcast_state("0");
			tansacInfo.setSign_state("");
		}

		// StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			CertsInfo certsInfo = list.get(i);
			certsInfo.getOpenbadges().setSignature(slist.get(i));
			certsInfo.setCstate("merged");
			certsInfo.setTransactionId(tansacInfoId);
			// sb.append("'"+certsInfo.getId()+ "',");
			success = checkerService.updateCertsInfo(certsInfo);
		}

		int savesuccess = issuerService.saveTansacInfo(tansacInfo);

		success = success * savesuccess;

		String jsonString = "{\"success\":\"" + success + "\",\"merkleRoot\":\""
				+ CHexConver.str2HexStr(merkleRoot).replace(" ", "") + "\"}";
		return jsonString;
	}

	/**
	 * post TansacInfo List
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
	 * get certificate information bu pages
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "postCertsInfoList", method = RequestMethod.POST)
	public String postCertsInfoList(@RequestBody String queryCondition) {
		PageModel<CertsInfo> pm = JSON.parseObject(queryCondition, PageModel.class);
		PageModel<CertsInfo> pms = issuerService.getCertsInfoPage(pm);
		String jsonString = JSON.toJSONString(pms);
		return jsonString;
	}

	/**
	 * check student configure
	 * 
	 * @return
	 */
	@RequestMapping(value = "checkStuConf", method = RequestMethod.POST)
	public String checkStuConf(HttpServletRequest request) {

		String stuConfStr = request.getParameter("stuConfStr");
		stuConfStr = CryptoUtil.base64Decode(stuConfStr);
		StuConf stuConf = JSON.parseObject(stuConfStr, StuConf.class);
		String applyState = stuConf.getApply_state();

		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String handle_time = format.format(date);
		stuConf.setHandle_time(handle_time); // 设置处理时间

		int success = 0;

		if ("passed".equalsIgnoreCase(applyState)) { // 如果通过，开始组建证书

			String checkInfoStr = request.getParameter("checkInfoStr");
			checkInfoStr = CryptoUtil.base64Decode(checkInfoStr);
			CheckInfo checkInfo = JSON.parseObject(checkInfoStr, CheckInfo.class);

			List<SchConf> schlist = adminService.findAllSchConf();
			SchConf schConf = schlist.get(0); // 获取学校配置信息

			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			String currentUserName = userDetails.getUsername(); // 获取当前用户

			String user_id = stuConf.getUser_id();
			StuRevoInfo stuRevoInfo = adminService.getStuRevoInfoByUserId(user_id);

			List<SchIdentity> schIdentityList = adminService.findAllSchIdentity();
			SchIdentity schIdentity = schIdentityList.get(0); // 获取学校认证配置

			CertsInfo certsInfo = assembleCertificate(currentUserName, stuConf, schConf, stuRevoInfo, schIdentity,
					checkInfo);

			int insertFlag = checkerService.saveCertsInfo(certsInfo); // 保存最新的证书

			stuConf.setCerts_id(certsInfo.getId());
			int updateFlag = studentService.updateStuConf(stuConf); // 更新申请表

			success = insertFlag * updateFlag;

		} else if ("rejected".equalsIgnoreCase(applyState)) { // 如果被拒绝
			success = studentService.updateStuConf(stuConf); // 更新申请表
		}

		String jsonString = "{\"success\":\"" + success + "\"}";
		return jsonString;

	}

	/**
	 * 
	 * @param stuConf
	 *            学生信息
	 * @param schConf
	 *            学校信息
	 * @param stuRevoInfo
	 *            学生证书回收信息
	 * @return
	 */
	private CertsInfo assembleCertificate(String currentUserName, StuConf stuConf, SchConf schConf,
			StuRevoInfo stuRevoInfo, SchIdentity schIdentity, CheckInfo checkInfo) {

		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String createTime = format.format(date);

		CertsInfo TansacInfo = new CertsInfo();

		String id = CryptoUtil.getUUID();
		TansacInfo.setCreateTime(createTime);
		TansacInfo.setCstate("new"); // 状态
		TansacInfo.setId(id);
		TansacInfo.setRecipient(stuConf.getUser_id());

		List<OperateDetail> odl = new ArrayList<OperateDetail>();
		OperateDetail OperateDetail = new OperateDetail();
		OperateDetail.setCert_id(CryptoUtil.getUUID());
		OperateDetail.setOper_time(createTime);
		OperateDetail.setOper_type("add");
		OperateDetail.setUser_id(currentUserName);
		OperateDetail.setUser_name(currentUserName);
		odl.add(OperateDetail);

		TansacInfo.setOdl(odl); // 操作细节

		Openbadges openbadges = new Openbadges();

		List<String> context = new ArrayList<String>();
		// context.add("https://w3id.org/openbadges/v2");
		// context.add("https://w3id.org/blockcerts/v2");
		// context.add("https://blocktechcert.github.io/www/json/context.json");

		String standard = checkInfo.getStandard();
		String[] standardArr = standard.split(",");
		for (int i = 0; i < standardArr.length; i++) {
			context.add(standardArr[i]);
		}

		openbadges.setContext(context);

		openbadges.setType(checkInfo.getBadgeClass());

		openbadges.setId(CryptoUtil.getUUID());

		Recipient recipient = new Recipient();
		recipient.setHashed("false");
		recipient.setIdentity(stuConf.getIdentity());
		recipient.setType(stuConf.getIdentity_type());
		openbadges.setRecipient(recipient);
		openbadges.setIssuedOn(createTime);

		Badge badge = new Badge();
		badge.setId(checkInfo.getBadgeId());
		badge.setType(checkInfo.getBadgeType());
		badge.setName(checkInfo.getBadgeName());
		badge.setDescription(checkInfo.getBadgeDescription());
		badge.setImage(checkInfo.getBadgeImage());
		badge.setCreated(checkInfo.getCreated());
		badge.setExpires(checkInfo.getExpires());

		Issuer issuer = new Issuer();
		issuer.setEmail(schConf.getEmail());
		issuer.setId(schConf.getId());
		issuer.setImage(schConf.getImage());
		issuer.setName(schConf.getName());
		issuer.setType("Profile");
		issuer.setUrl(schConf.getUrl());
		badge.setIssuer(issuer);

		FileClaim fileClaim = new FileClaim();
		fileClaim.setFilehash(stuConf.getFile_hash());
		fileClaim.setFilestore("PDF");
		List<String> ftypelist = new ArrayList<String>();
		ftypelist.add("fileClaim");
		ftypelist.add("Extension");
		fileClaim.setType(ftypelist);

		badge.setFileClaim(fileClaim);

		List<Map<String, String>> schIdentitylist = schIdentity.getBtc_address_list();
		Map<String, String> schIdentitymap = schIdentitylist.get(0);

		String BTCaddress = schIdentitymap.keySet().iterator().next(); // 第一个
		String validationtime = schIdentitymap.get(BTCaddress); // 时间戳

		IdentityClaim identityClaim = new IdentityClaim();
		identityClaim.setBTCaddress(BTCaddress);
		identityClaim.setIdentityName(schConf.getName());
		identityClaim.setValidationtime(validationtime);
		List<String> itypelist = new ArrayList<String>();
		itypelist.add("identityClaim");
		itypelist.add("Extension");
		identityClaim.setType(itypelist);

		badge.setIdentityClaim(identityClaim);

		RevocationClaim revocationClaim = new RevocationClaim();
		revocationClaim.setBatchRevocationAddress(stuRevoInfo.getRb_address());
		revocationClaim.setRevocationAddress(stuRevoInfo.getR_address());
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

		TansacInfo.setOpenbadges(openbadges);

		return TansacInfo;
	}

	/**
	 * get student configure by pages
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "postStuConfList", method = RequestMethod.POST)
	public String getStuConfList(@RequestBody String queryCondition) {
		PageModel<StuConf> pm = JSON.parseObject(queryCondition, PageModel.class);
		PageModel<StuConf> pms = studentService.getStuConfPage(pm);
		String jsonString = JSON.toJSONString(pms);
		return jsonString;
	}

	/**
	 * 
	 * update student configure
	 * 
	 * @return
	 */
	@RequestMapping(value = "updateStuConf/{stuConfInfo}", method = RequestMethod.GET)
	public String updateStuConf(@PathVariable String stuConfInfo) {
		stuConfInfo = CryptoUtil.base64Decode(stuConfInfo);
		StuConf stuConf = JSON.parseObject(stuConfInfo, StuConf.class);
		int success = studentService.updateStuConf(stuConf);
		String jsonString = "{\"success\":\"" + success + "\"}";
		return jsonString;
	}

	/**
	 * get student configure
	 * 
	 * @return
	 */
	@RequestMapping(value = "getStuConf/{id}", method = RequestMethod.GET)
	public String getStuConf(@PathVariable String id) {
		id = CryptoUtil.base64Decode(id);
		StuConf StuConf = studentService.getStuConf(id);
		String jsonString = JSON.toJSONString(StuConf);
		return jsonString;
	}

	/**
	 * get update cerificate information
	 * 
	 * @param queryCondition
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getCertsInfoList/{queryCondition}", method = RequestMethod.GET)
	public String getCertsInfoList(@PathVariable String queryCondition) {
		queryCondition = CryptoUtil.base64Decode(queryCondition);
		PageModel<CertsInfo> pm = JSON.parseObject(queryCondition, PageModel.class);
		PageModel<CertsInfo> pms = checkerService.getCertsInfoPage(pm);
		String jsonString = JSON.toJSONString(pms);
		return jsonString;
	}

	/**
	 * get all certificate information
	 * 
	 * @return
	 */
	@RequestMapping(value = "getAllCertsInfo", method = RequestMethod.GET)
	public String getAllCertsInfo() {
		List<CertsInfo> list = checkerService.findAllCertsInfo();
		String jsonString = JSON.toJSONString(list);
		return jsonString;
	}

	/**
	 * get the certificate information by pages
	 * 
	 * @return
	 */
	@RequestMapping(value = "getCertsInfo/{id}", method = RequestMethod.GET)
	public String getCertsInfo(@PathVariable String id) {
		id = CryptoUtil.base64Decode(id);
		CertsInfo CertsInfo = checkerService.getCertsInfo(id);
		String jsonString = JSON.toJSONString(CertsInfo);
		return jsonString;
	}

	/**
	 * delete certificate information
	 * 
	 * @return
	 */
	@RequestMapping(value = "delCertsInfo/{delIds}", method = RequestMethod.GET)
	public String delCertsInfo(@PathVariable String delIds) {
		delIds = CryptoUtil.base64Decode(delIds);
		int success = checkerService.delCertsInfo(delIds);
		String jsonString = "{\"success\":\"" + success + "\"}";
		return jsonString;
	}

	/**
	 * save certificate information
	 * 
	 * @return
	 */
	@RequestMapping(value = "saveCertsInfo/{data}", method = RequestMethod.GET)
	public String saveCertsInfo(@PathVariable String data) {
		data = CryptoUtil.base64Decode(data);
		CertsInfo CertsInfo = JSON.parseObject(data, CertsInfo.class);

		int success = checkerService.saveCertsInfo(CertsInfo);
		String jsonString = "{\"success\":\"" + success + "\"}";
		return jsonString;
	}

	/**
	 * 
	 * update certificate information
	 * 
	 * @return
	 */
	@RequestMapping(value = "updateCertsInfo/{CertsInfoInfo}", method = RequestMethod.GET)
	public String updateCertsInfo(@PathVariable String CertsInfoInfo) {
		CertsInfoInfo = CryptoUtil.base64Decode(CertsInfoInfo);
		CertsInfo CertsInfo = JSON.parseObject(CertsInfoInfo, CertsInfo.class);
		int success = checkerService.updateCertsInfo(CertsInfo);
		String jsonString = "{\"success\":\"" + success + "\"}";
		return jsonString;
	}

}
