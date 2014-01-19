package com.sable.cb.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sable.cb.domain.Organization;
import com.sable.cb.domain.Users;
import com.sable.cb.repository.OrganizationRepository;
import com.sable.cb.repository.UsersRepository;

@Component
@Transactional
public class ApplicationListenerBean {

	@Autowired
	private UsersRepository usersService;

	@Autowired
	private OrganizationRepository organizationService;

	@PostConstruct
	@Transactional
	public void load() {

//		for (Users u : usersService.findAllUserses()) {
//			
//			u.setAdminOrgs(null);
//			u.setFollowedOrgs(null);
//			u = usersService.updateUsers(u);
//			usersService.deleteUsers(u);
//		}
//
//		for (Organization o : organizationService.findAllOrganizations()) {
//			o.setAdmins(null);
//			o.setFollowers(null);
//			o = organizationService.updateOrganization(o);
//			organizationService.deleteOrganization(o);
//		}

		Users u = usersService.findByEmail("mdshannan@gmail.com");
		if (u != null && u.getId() != null) {
			return;
		}
		// Add users
		Users user1 = new Users();
		user1.setEmail("mdshannan@gmail.com");
		user1.setFullname("Intesar Mohammed");
		user1.setPassword("admin");
		//usersService.save(user1);

		Users user2 = new Users();
		user2.setEmail("ruuddin@yahoo.com");
		user2.setFullname("Riaz Uddin");
		user2.setPassword("admin");
		//usersService.save(user2);

		Users user3 = new Users();
		user3.setEmail("mohdhamedmscse@gmail.com");
		user3.setFullname("Hamed Mohammed");
		user3.setPassword("admin");
		//usersService.save(user3);

		// Add orgs
		Organization org1 = new Organization();
		org1.setName("MCA");
		org1.setCity("Santa Clara");
		org1.getAdmins().add(user1);
		org1.getFollowers().add(user2);

		user1.getAdminOrgs().add(org1);
		user1.getFollowedOrgs().add(org1);
		user2.getFollowedOrgs().add(org1);
	
		organizationService.save(org1);
		
	
		Organization org2 = new Organization();
		org2.setName("Jama Masjid");
		org2.setCity("Chicago");
		org2.getAdmins().add(user3);
		org2.getFollowers().add(user3);
		
		user3.getAdminOrgs().add(org2);
		user3.getFollowedOrgs().add(org2);
		
		organizationService.save(org2);
		

	}
}
