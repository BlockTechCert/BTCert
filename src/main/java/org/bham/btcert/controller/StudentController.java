package org.bham.btcert.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bham.btcert.model.PageModel;
import org.bham.btcert.model.StuConf;
import org.bham.btcert.model.certificate.CertsInfo;
import org.bham.btcert.service.IssuerService;
import org.bham.btcert.service.StudentService;
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

/**
 * 
 * @Title: StudentController.java
 * @Package org.bham.btcert.controller
 * @Description: TODO the controller page for student page
 * @author rxl635@student.bham.ac.uk
 * @version V1.0
 */
@Component
@ConfigurationProperties
@RestController
@RequestMapping("/student")
public class StudentController {

	@Value("${application.message}")
	String message;

	@Value("${application.appname}")
	String appname;

	@Autowired
	private StudentService studentService;

	@Autowired
	private IssuerService issuerService;

	/**
	 * get certificate state
	 * 
	 * @return
	 */
	@RequestMapping(value = "getCertState/{certId}", method = RequestMethod.GET)
	public String getCertState(@PathVariable String certId) {
		// certId = CryptoUtil.base64Decode(certId);
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUserName = userDetails.getUsername(); //
		CertsInfo certsInfo = studentService.getCertStateByUserId(currentUserName);
		String jsonString;
		if (certsInfo == null) {
			jsonString = "{\"certState\":\"0\"}";
		} else {
			jsonString = "{\"certState\":\"" + certsInfo.getCstate() + "\"}";
		}
		return jsonString;
	}

	/**
	 * get certificate information
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
	 * get student configure
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "getStuConfList", method = RequestMethod.POST)
	public String getStuConfList(@RequestBody String queryCondition) {
		// queryCondition = CryptoUtil.base64Decode(queryCondition);

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUserName = userDetails.getUsername(); // 

		PageModel<StuConf> pm = JSON.parseObject(queryCondition, PageModel.class);

		String QueryObject = (pm.getQueryObject() == null || "".equals(pm.getQueryObject())) ? "{}"
				: pm.getQueryObject();
		Map QueryObjectMap = JSON.parseObject(QueryObject, Map.class);
		QueryObjectMap.put("user_id", currentUserName);
		pm.setQueryObject(JSON.toJSONString(QueryObjectMap));

		PageModel<StuConf> pms = studentService.getStuConfPage(pm);
		String jsonString = JSON.toJSONString(pms);
		return jsonString;
	}

	/**
	 * get all the student configure
	 * 
	 * @return
	 */
	@RequestMapping(value = "getAllStuConf", method = RequestMethod.GET)
	public String getAllStuConf() {
		List<StuConf> list = studentService.findAllStuConf();
		String jsonString = JSON.toJSONString(list);
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
	 * delete student configure
	 * 
	 * @return
	 */
	@RequestMapping(value = "delStuConf/{delIds}", method = RequestMethod.GET)
	public String delStuConf(@PathVariable String delIds) {
		delIds = CryptoUtil.base64Decode(delIds);
		int success = studentService.delStuConf(delIds);
		String jsonString = "{\"success\":\"" + success + "\"}";
		return jsonString;
	}

	/**
	 * save student configure
	 * 
	 * @return
	 */
	@RequestMapping(value = "saveStuConf/{data}", method = RequestMethod.GET)
	public String saveStuConf(@PathVariable String data) {
		data = CryptoUtil.base64Decode(data);

		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String createTime = format.format(date);

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUserName = userDetails.getUsername(); // 获取当前用户

		StuConf StuConf = JSON.parseObject(data, StuConf.class);
		StuConf.setApply_state("new");
		StuConf.setApply_note("");
		StuConf.setApply_time(createTime);
		StuConf.setHandle_time("");
		StuConf.setId(CryptoUtil.getUUID());
		StuConf.setUser_id(currentUserName);

		int success = studentService.saveStuConf(StuConf);
		String jsonString = "{\"success\":\"" + success + "\"}";
		return jsonString;
	}

	/**
	 * 
	 * update student configure
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
	 * init data for testing 
	 * @deprecated 
	 * @return
	 */
	@RequestMapping(value = "initStuconfData", method = RequestMethod.GET)
	public String initStuconfData() {
		List<StuConf> stuConfList = new ArrayList<StuConf>();

		for (int i = 1; i < 30; i++) {
			StuConf stuConf = new StuConf();
			stuConf.setId(CryptoUtil.getUUID());
			stuConf.setGiven_name("rujia" + i);
			stuConf.setFamily_name("li");
			stuConf.setBirthday("1994-09-21");
			stuConf.setIdentity("rujia@bham.ac.uk");
			stuConf.setIdentity_type("email");
			stuConf.setFile_hash("XXXXXXX");
			stuConf.setApply_type("Certificate");
			stuConf.setUser_id("student@bham.ac.uk");
			stuConf.setApply_state("new");
			stuConf.setApply_note("");

			Date date = new Date();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String createTime = format.format(date);

			stuConf.setApply_time(createTime);
			stuConf.setHandle_time("");
			stuConfList.add(stuConf);
		}

		int success = studentService.saveStuConfBatch(stuConfList);
		String jsonString = "{\"success\":\"" + success + "\"}";
		return jsonString;
	}

}
