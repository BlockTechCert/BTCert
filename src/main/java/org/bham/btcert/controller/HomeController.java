package org.bham.btcert.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @Title: HomeController.java
 * @Package org.bham.btcert.controller
 * @Description: TODO the controller page for home(common) page
 * @author rxl635@student.bham.ac.uk
 * @version V1.0
 */
@ConfigurationProperties
@Controller
@RequestMapping("/")
public class HomeController {

	@Value("${application.message}")
	String message;

	@Value("${application.appname}")
	String appname;

	@SuppressWarnings("unchecked")
	@GetMapping("/")
	public String home1() {

		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if ("anonymousUser".equals(obj)) {
			return "redirect:home";
		} else {
			UserDetails userDetails = (UserDetails) obj;
			Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) userDetails.getAuthorities();
			SimpleGrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");
			SimpleGrantedAuthority ROLE_STUDENT = new SimpleGrantedAuthority("ROLE_STUDENT");
			SimpleGrantedAuthority ROLE_ISSUER = new SimpleGrantedAuthority("ROLE_ISSUER");
			SimpleGrantedAuthority ROLE_CHECKER = new SimpleGrantedAuthority("ROLE_CHECKER");

			if (authorities.contains(ROLE_ADMIN)) {
				return "redirect:admin/admin_home";
			} else if (authorities.contains(ROLE_STUDENT)) {
				return "redirect:student/student_home";
			} else if (authorities.contains(ROLE_CHECKER)) {
				return "redirect:checker/checker_home";
			} else if (authorities.contains(ROLE_ISSUER)) {
				return "redirect:issuer/issuer_home";
			}
		}

		return "redirect:home";
	}

	@GetMapping("/demo")
	public String demo() {
		return "demo";
	}

	@GetMapping("/home")
	public String home() {
		return "home";
	}

	@GetMapping("/about")
	public String about() {
		return "about";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/verify")
	public String verify() {
		return "verify";
	}

	@GetMapping("/download")
	public String download() {
		return "download";
	}

	@GetMapping("/403")
	public String error403() {
		return "error/403";
	}

	@GetMapping("/404")
	public String error404() {
		return "error/404";
	}

	@GetMapping("/student/student_home")
	public String student() {
		return "student/student_home";
	}

	@GetMapping("/student/apply_form")
	public String applyForm() {
		return "student/apply_form";
	}

	@GetMapping("/student/apply_history")
	public String apply_history() {
		return "student/apply_history";
	}

	@GetMapping("/student/issued_certificate")
	public ModelAndView issued_certificate() {
		ModelAndView mav = new ModelAndView("student/issued_certificate");
		return mav;
	}

	@GetMapping("/student/revoked_certificate")
	public ModelAndView revoked_certificate() {
		ModelAndView mav = new ModelAndView("student/revoked_certificate");
		return mav;
	}

	@GetMapping("/admin/admin_home")
	public String admin() {
		return "admin/admin_home";
	}

	@GetMapping("/admin/admin_index")
	public String admin_index() {
		return "admin/admin_index";
	}

	@GetMapping("/admin/sch_config_manage")
	public ModelAndView sch_config_manage() {
		ModelAndView mav = new ModelAndView("admin/sch_config_manage");
		return mav;
	}

	@GetMapping("/admin/sch_Identity_manage")
	public ModelAndView sch_Identity_manage() {
		ModelAndView mav = new ModelAndView("admin/sch_Identity_manage");
		return mav;
	}

	@GetMapping("/admin/stu_revoInfo_manage")
	public ModelAndView stu_revoInfo_manage() {
		ModelAndView mav = new ModelAndView("admin/stu_revoInfo_manage");
		return mav;
	}

	@GetMapping("/admin/registered_user_manage")
	public ModelAndView registered_user_manage() {
		ModelAndView mav = new ModelAndView("admin/registered_user_manage");
		return mav;
	}

	@GetMapping("/admin/transaction_address")
	public ModelAndView transaction_address() {
		ModelAndView mav = new ModelAndView("admin/transaction_address");
		return mav;
	}

	@GetMapping("/admin/latest_confirmed_transaction")
	public ModelAndView latest_confirmed_transaction() {
		ModelAndView mav = new ModelAndView("admin/latest_confirmed_transaction");
		return mav;
	}

	@GetMapping("/checker/checker_home")
	public String checker() {
		return "checker/checker_home";
	}

	@GetMapping("/checker/checked_history_list")
	public ModelAndView checked_history_list() {
		ModelAndView mav = new ModelAndView("checker/checked_history_list");
		return mav;
	}

	@GetMapping("/checker/unchecked_list")
	public ModelAndView unchecked_list() {
		ModelAndView mav = new ModelAndView("checker/unchecked_list");
		return mav;
	}

	@GetMapping("/checker/checker_form")
	public ModelAndView checker_form() {
		ModelAndView mav = new ModelAndView("checker/checker_form");
		return mav;
	}

	@GetMapping("/checker/merged_certificate_list")
	public ModelAndView merged_certificate_list() {
		ModelAndView mav = new ModelAndView("checker/merged_certificate_list");
		return mav;
	}

	@GetMapping("/checker/unmerged_certificate_list")
	public ModelAndView unmerged_certificate_list() {
		ModelAndView mav = new ModelAndView("checker/unmerged_certificate_list");
		return mav;
	}

	@GetMapping("/issuer/issuer_home")
	public String issuer() {
		return "issuer/issuer_home";
	}

	@GetMapping("/admin/broadcasted_transaction_list")
	public ModelAndView broadcasted_transaction_list() {
		ModelAndView mav = new ModelAndView("admin/broadcasted_transaction_list");
		return mav;
	}

	@GetMapping("/admin/unbroadcasted_transaction_list")
	public ModelAndView unbroadcasted_transaction_list() {
		ModelAndView mav = new ModelAndView("admin/unbroadcasted_transaction_list");
		return mav;
	}

	@GetMapping("/issuer/unserialized_transaction_list")
	public ModelAndView unserialized_transaction_list() {
		ModelAndView mav = new ModelAndView("issuer/unserialized_transaction_list");
		return mav;
	}

	@GetMapping("/issuer/unsigned_transaction_list")
	public ModelAndView unsigned_transaction_list() {
		ModelAndView mav = new ModelAndView("issuer/unsigned_transaction_list");
		return mav;
	}

	@GetMapping("/issuer/signed_transaction_list")
	public ModelAndView signed_transaction_list() {
		ModelAndView mav = new ModelAndView("issuer/signed_transaction_list");
		return mav;
	}

	@GetMapping("/issuer/unconfirmed_revocation_list")
	public ModelAndView unconfirmed_revocation_list() {
		ModelAndView mav = new ModelAndView("issuer/unconfirmed_revocation_list");
		return mav;
	}

	@GetMapping("/issuer/confirmed_revocation_list")
	public ModelAndView confirmed_revocation_list() {
		ModelAndView mav = new ModelAndView("issuer/confirmed_revocation_list");
		return mav;
	}

}
