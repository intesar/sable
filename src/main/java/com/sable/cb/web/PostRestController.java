package com.sable.cb.web;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sable.cb.domain.Post;
import com.sable.cb.service.PostService;
import com.sable.cb.service.PostType;

//@RequestMapping("/rest/posts")
@Controller
public class PostRestController {

	@Autowired
    PostService postService;

	@RequestMapping(value="/post/rest", method = RequestMethod.POST)
	@ResponseBody
	public void create(@RequestBody String content) {
		
		Post post = new Post();
		post.setContent(content);
		post.setPostType(PostType.GENERAL.toString());
		postService.savePost(post);
	}
	
}
