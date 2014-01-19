package com.sable.cb.web;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sable.cb.domain.Users;
import com.sable.cb.service.UsersService;

@RequestMapping("/signup")
@Controller
public class RegisterUsersController {

	Logger logger = Logger.getLogger(getClass());
	
	@Autowired
    UsersService usersService;
	
	@RequestMapping(value="/rest", method = RequestMethod.POST)
	@ResponseBody
	public void create(@RequestBody Users user) {
		logger.info("content = " + user.toString());
		usersService.saveUsers(user);
	}
}
