package com.sable.cb.domain;
import com.sable.cb.repository.PostRepository;
import com.sable.cb.service.PostService;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
public class PostDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Post> data;

	@Autowired
    PostService postService;

	@Autowired
    PostRepository postRepository;

	public Post getNewTransientPost(int index) {
        Post obj = new Post();
        setContent(obj, index);
        setExpiration(obj, index);
        setPostType(obj, index);
        return obj;
    }

	public void setContent(Post obj, int index) {
        String content = "content_" + index;
        obj.setContent(content);
    }

	public void setExpiration(Post obj, int index) {
        Date expiration = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setExpiration(expiration);
    }

	public void setPostType(Post obj, int index) {
        String postType = "postType_" + index;
        obj.setPostType(postType);
    }

	public Post getSpecificPost(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Post obj = data.get(index);
        Long id = obj.getId();
        return postService.findPost(id);
    }

	public Post getRandomPost() {
        init();
        Post obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return postService.findPost(id);
    }

	public boolean modifyPost(Post obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = postService.findPostEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Post' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Post>();
        for (int i = 0; i < 10; i++) {
            Post obj = getNewTransientPost(i);
            try {
                postService.savePost(obj);
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            postRepository.flush();
            data.add(obj);
        }
    }
}
