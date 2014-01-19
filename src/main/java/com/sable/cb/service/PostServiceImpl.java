package com.sable.cb.service;

import com.sable.cb.domain.Post;
import com.sable.cb.repository.PostRepository;

import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostServiceImpl implements PostService {

	@Autowired
    PostRepository postRepository;

	@Autowired
	EmailService emailService;
	
	public long countAllPosts() {
        return postRepository.count();
    }

	public void deletePost(Post post) {
        postRepository.delete(post);
    }

	public Post findPost(Long id) {
        return postRepository.findOne(id);
    }

	public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

	public List<Post> findPostEntries(int firstResult, int maxResults) {
        return postRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void savePost(Post post) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 7);
		post.setExpiration(calendar.getTime());
		
		post.setStatus(PostStatus.PENDING_APPROVAL.toString());
        postRepository.save(post);
        
        // TODO construct email content
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        emailService.sendMessage(user, "Post submitted", post.getContent());
        
    }
	
	public void savePostByAdmin(Post post) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 7);
		post.setExpiration(calendar.getTime());
		
		post.setStatus(PostStatus.APPROVED.toString());
        postRepository.save(post);
        
        // TODO construct email content
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        emailService.sendMessage(user, "Post submitted & auto approved", post.getContent());
        
    }

	public Post updatePost(Post post) {
        return postRepository.save(post);
    }
}
