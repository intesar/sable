package com.sable.cb.service;

import com.sable.cb.domain.Users;
import com.sable.cb.repository.UsersRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsersServiceImpl implements UsersService {
	
	@Autowired
    private transient MailSender mailTemplate;

    org.springframework.mail.SimpleMailMessage mailMessage = new org.springframework.mail.SimpleMailMessage();

	public void sendMessage(String mailFrom, String subject, String mailTo, String message)
	{}

	@Autowired
    UsersRepository usersRepository;

	public long countAllUserses() {
        return usersRepository.count();
    }

	public void deleteUsers(Users users) {
        usersRepository.delete(users);
    }

	public Users findUsers(Long id) {
        return usersRepository.findOne(id);
    }

	public List<Users> findAllUserses() {
        return usersRepository.findAll();
    }

	public List<Users> findUsersEntries(int firstResult, int maxResults) {
        return usersRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void saveUsers(Users users) {
        usersRepository.save(users);
    }

	public Users updateUsers(Users users) {
        return usersRepository.save(users);
    }
}
