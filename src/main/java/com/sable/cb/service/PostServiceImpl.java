package com.sable.cb.service;

import com.sable.cb.domain.Post;
import com.sable.cb.repository.PostRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostServiceImpl implements PostService {

	@Autowired
    PostRepository postRepository;

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
        postRepository.save(post);
    }

	public Post updatePost(Post post) {
        return postRepository.save(post);
    }
}
