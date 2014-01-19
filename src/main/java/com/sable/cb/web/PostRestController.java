package com.sable.cb.web;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sable.cb.domain.Post;
import com.sable.cb.service.PostService;
import com.sable.cb.service.PostStatus;
import com.sable.cb.service.PostType;

@Controller
public class PostRestController {

	Logger logger = Logger.getLogger(getClass());
	
	@Autowired
    PostService postService;

	@RequestMapping(value="/post/rest", method = RequestMethod.POST)
	@ResponseBody
	public String create(@RequestBody String content) {
		try {
			content = URLDecoder.decode(content, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		logger.info("content = " + content);
		Post post = new Post();
		post.setContent(content);
		post.setPostType(PostType.GENERAL.toString());
		postService.savePost(post);
		return "";
	}
	
	@RequestMapping(value="/post/admin", method = RequestMethod.POST)
	@ResponseBody
	public void createByAdmin(@RequestBody Post post) {
		
		logger.info("post = " + post.toString());
		
		post.setPostType(PostType.valueOf(post.getPostType()).toString());
		postService.savePostByAdmin(post);
	}
	
	@RequestMapping(value="/post/expire/{postId}", method = RequestMethod.GET)
	@ResponseBody
	public String expire(@PathVariable Long postId) {
		
		logger.info("post = " + postId.toString());
		
		postService.expirePost(postId);
		return "Done";
	}
	
	@RequestMapping(value="/post/approve/{orgId}/{postId}", method = RequestMethod.GET)
	@ResponseBody
	public String approve(@PathVariable Long orgId, @PathVariable Long postId) {
		
		logger.info("post = " + postId.toString());
		
		postService.approve(orgId, postId);
		
		return "Done";
	}
	
	@RequestMapping(value="/post/reject/{orgId}/{postId}", method = RequestMethod.GET)
	@ResponseBody
	public String reject(@PathVariable Long orgId, @PathVariable Long postId) {
		
		logger.info("post = " + postId.toString());
		
		postService.reject(orgId, postId);
		return "Done";
	}
	
	@RequestMapping(value="/post/{firstResult}/{maxResults}", method = RequestMethod.GET)
	@ResponseBody
	public List<Post> getAll(@PathVariable int firstResult, @PathVariable int maxResults) {
		
		logger.info("getAll = " + firstResult + " " + maxResults);
		
		return postService.findEligiblePosts(firstResult, maxResults);
	}
	
	
	
}
