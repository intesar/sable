package com.sable.cb.domain;
import com.sable.cb.repository.UsersRepository;
import com.sable.cb.service.UsersService;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Component
@Configurable
public class UsersDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Users> data;

	@Autowired
    UsersService usersService;

	@Autowired
    UsersRepository usersRepository;

	public Users getNewTransientUsers(int index) {
        Users obj = new Users();
        setEmail(obj, index);
        setFullname(obj, index);
        setPassword(obj, index);
        return obj;
    }

	public void setEmail(Users obj, int index) {
        String email = "foo" + index + "@bar.com";
        obj.setEmail(email);
    }

	public void setFullname(Users obj, int index) {
        String fullname = "fullname_" + index;
        obj.setFullname(fullname);
    }

	public void setPassword(Users obj, int index) {
        String password = "password_" + index;
        obj.setPassword(password);
    }

	public Users getSpecificUsers(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Users obj = data.get(index);
        Long id = obj.getId();
        return usersService.findUsers(id);
    }

	public Users getRandomUsers() {
        init();
        Users obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return usersService.findUsers(id);
    }

	public boolean modifyUsers(Users obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = usersService.findUsersEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Users' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Users>();
        for (int i = 0; i < 10; i++) {
            Users obj = getNewTransientUsers(i);
            try {
                usersService.saveUsers(obj);
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            usersRepository.flush();
            data.add(obj);
        }
    }
}
