package com.sable.cb.service;
import com.sable.cb.domain.Users;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;

public interface UsersService {

    
    public void sendMessage(String mailFrom, String subject, String mailTo, String message);

	public abstract long countAllUserses();


	public abstract void deleteUsers(Users users);


	public abstract Users findUsers(Long id);


	public abstract List<Users> findAllUserses();


	public abstract List<Users> findUsersEntries(int firstResult, int maxResults);


	public abstract void saveUsers(Users users);


	public abstract Users updateUsers(Users users);

}
