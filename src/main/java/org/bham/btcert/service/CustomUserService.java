package org.bham.btcert.service;

import java.util.ArrayList;
import java.util.List;

import org.bham.btcert.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 
 * @Title: CustomUserService.java
 * @Package org.bham.btcert.service
 * @Description: TODO
 * @author rxl635@student.bham.ac.uk
 * @version V1.0
 */

@Service
public class CustomUserService implements UserDetailsService { 

	@Autowired
	private MongoTemplate mongoTemplate;

	// static final PasswordEncoder passwordEncoder = new
	// BCryptPasswordEncoder();

	@Override
	public UserDetails loadUserByUsername(String username) { // 重写loadUserByUsername

		// List<Users> userslist = studentService.findByName(username);
		// String encodedPassword = passwordEncoder.encode("test");

		Query query = new Query(Criteria.where("u_name").is(username));
		List<Users> userslist = mongoTemplate.find(query, Users.class);

		if (userslist == null || userslist.size() == 0) {
			throw new UsernameNotFoundException("Username Not Found");
		}

		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		// userslist.get(0).getRole();

		String role = userslist.get(0).getRole();
		String[] rs = role.split(",");
		for (int i = 0; i < rs.length; i++) {
			authorities.add(new SimpleGrantedAuthority(rs[i]));
		}
		System.err.println("login seccess ----- " + userslist.get(0));
		return new User(username, userslist.get(0).getU_passwd(), authorities);

	}
}