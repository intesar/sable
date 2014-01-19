package com.sable.cb.service;
import com.sable.cb.domain.Post;
import java.util.List;

public interface PostService {

	public abstract long countAllPosts();


	public abstract void deletePost(Post post);


	public abstract Post findPost(Long id);


	public abstract List<Post> findAllPosts();


	public abstract List<Post> findPostEntries(int firstResult, int maxResults);


	public abstract void savePost(Post post);
	
	public void savePostByAdmin(Post post);


	public abstract Post updatePost(Post post);


	public abstract void expirePost(Long postId);


	public abstract void approve(Long orgId, Long postId);


	public abstract void reject(Long orgId, Long postId);


	public abstract List<Post> findEligiblePosts(int firstResult,
			int maxResults);


	public abstract Post like(Long id);
	
	public abstract Post dislike(Long id);
	
	public abstract Post rsvp(Long id, int guests);

}
