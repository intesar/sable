package com.sable.cb.service;

import com.sable.cb.domain.Organization;
import com.sable.cb.domain.Post;
import com.sable.cb.domain.Rsvp;
import com.sable.cb.domain.Users;
import com.sable.cb.repository.OrganizationRepository;
import com.sable.cb.repository.PostRepository;
import com.sable.cb.repository.UsersRepository;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostServiceImpl implements PostService {

	@Autowired
	PostRepository postRepository;

	@Autowired
	OrganizationRepository organizationRepository;

	@Autowired
	UsersRepository usersRepository;

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
		return postRepository.findAll(
				new org.springframework.data.domain.PageRequest(firstResult
						/ maxResults, maxResults)).getContent();
	}

	@Transactional
	public void savePost(Post post) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 7);
		post.setExpiration(calendar.getTime());

		post.setStatus(PostStatus.PENDING_APPROVAL.toString());

		String user = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		Users users = usersRepository.findByEmail(user);

		if (CollectionUtils.isEmpty(users.getFollowedOrgs())) {
			// throw new
			// RuntimeException("Ad cannot be posted if you are not following an org.");
			System.out.println("abc");
		}
		post.getPostedOrgs().addAll(users.getFollowedOrgs());

		post.setUser(users.getEmail());

		postRepository.saveAndFlush(post);

		// TODO construct email content
		// TODO link to expire ad
		String link = String.format(
				"\n Delete Post : http://localhost:8080/cb/post/expire/%s",
				post.getId());
		String post_ = String.format("Your post: %s", post.getContent());
		String status = String.format("\n Status: Pending");
		emailService.sendMessage(user, "Post submitted", post_ + status + link);

		// TODO send action email to admins
		for (Organization org : users.getFollowedOrgs()) {

			String org_ = String.format("\n Organization: %s", org.getName());
			String approveLink = String.format(
					"\n Approve : http://localhost:8080/cb/post/approve/%s/%s",
					org.getId(), post.getId());
			String rejectLink = String.format(
					"\n Reject : http://localhost:8080/cb/post/reject/%s/%s",
					org.getId(), post.getId());

			for (Users user_ : org.getAdmins()) {
				// TODO send email to approve
				emailService.sendMessage(user_.getEmail(),
						"Post submited, action required", org_ + post_
								+ approveLink + rejectLink);
			}
		}

	}

	public void savePostByAdmin(Post post) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 7);
		post.setExpiration(calendar.getTime());

		post.setStatus(PostStatus.PENDING_APPROVAL.toString());

		String user = SecurityContextHolder.getContext().getAuthentication()
				.getName();

		Users users = usersRepository.findByEmail(user);
		if (CollectionUtils.isEmpty(users.getAdminOrgs())) {
			throw new RuntimeException(
					"Ad cannot be posted if you are not following an org.");
		}

		post.getPostedOrgs().addAll(users.getFollowedOrgs());
		postRepository.saveAndFlush(post);

		// TODO construct email content
		// TODO link to expire ad
		String link = String.format(
				"\n Delete : http://localhost:8080/cb/post/expire/%s",
				post.getId());
		String post_ = String.format("Your post: %s", post.getContent());
		String status = String.format("\n Status: Pending");
		emailService.sendMessage(user, "Post submitted", post_ + status + link);

		// TODO send action email to admins
		for (Organization org : users.getAdminOrgs()) {

			String org_ = String.format("\n Organization: %s", org.getName());
			String approveLink = String.format(
					"\n Approve : http://localhost:8080/cb/post/approve/%s/%s",
					org.getId(), post.getId());
			String rejectLink = String.format(
					"\n Reject : http://localhost:8080/cb/post/reject/%s/%s",
					org.getId(), post.getId());

			for (Users user_ : org.getAdmins()) {
				// TODO send email to approve
				emailService.sendMessage(user_.getEmail(),
						"Post submited, action required", org_ + post_
								+ approveLink + rejectLink);
			}
		}

	}

	public Post updatePost(Post post) {
		return postRepository.save(post);
	}

	@Override
	public void expirePost(Long postId) {
		Post post = postRepository.findOne(postId);
		post.setExpiration(Calendar.getInstance().getTime());
		post.setStatus(PostStatus.EXPIRED.toString());
		postRepository.save(post);

	}

	@Override
	public void approve(Long orgId, Long postId) {
		Post post = postRepository.findOne(postId);
		Organization org = organizationRepository.findOne(orgId);

		post.getApprovedOrgs().add(org);
		post.setStatus(PostStatus.APPROVED.toString());
		postRepository.save(post);
		String post_ = String.format("Your post: %s", post.getContent());
		emailService.sendMessage(post.getUser(),
				String.format("Post is approved by %s!", org.getName()), post_);
	}

	@Override
	public void reject(Long orgId, Long postId) {
		Post post = postRepository.findOne(postId);
		Organization org = organizationRepository.findOne(orgId);
		post.getApprovedOrgs().remove(org);
		post.getRejectedOrgs().add(org);
		post.getPostedOrgs().remove(org);
		if (CollectionUtils.isEmpty(post.getApprovedOrgs())
				&& CollectionUtils.isEmpty(post.getPostedOrgs())) {
			post.setStatus(PostStatus.REJECTED.toString());
			String post_ = String.format("Your post: %s", post.getContent());
			emailService.sendMessage(post.getUser(),
					"Post is rejected by all Organizations!", post_);
		} else {
			String post_ = String.format("Your post: %s", post.getContent());
			emailService.sendMessage(post.getUser(),
					String.format("Post is rejected by %s!", org.getName()),
					post_);
		}
		postRepository.save(post);

	}

	@Override
	public List<Post> findEligiblePosts(final int firstResult,
			final int maxResults) {
		// find approved posts from user followed orgs.
		String user = SecurityContextHolder.getContext().getAuthentication()
				.getName();

		Users users = usersRepository.findByEmail(user);
		if (CollectionUtils.isEmpty(users.getAdminOrgs())) {
			// throw new
			// RuntimeException("Ad cannot be posted if you are not following an org.");
			// TODO
		}
		final Pageable page2 = new PageRequest(firstResult, maxResults,
				new Sort(new Order(Direction.DESC, "expiration")));
		return postRepository.findEligiblePosts(users.getFollowedOrgs());
	}

	@Override
	public Post like(Long id) {
		Post p = postRepository.findOne(id);
		String user = SecurityContextHolder.getContext().getAuthentication()
				.getName();

		Users users = usersRepository.findByEmail(user);
		if (CollectionUtils.isNotEmpty(p.getLikes()) || !p.getLikes().contains(users)) {
			p.getLikes().add(users);
		} else {
			return p;
		}
		
		emailService.sendMessage(p.getUser(),
				String.format("Post is liked by %s!", users.getFullname()),
				"Post : " + p.getContent());
		return postRepository.saveAndFlush(p);
	}

	@Override
	public Post dislike(Long id) {
		Post p = postRepository.findOne(id);
		String user = SecurityContextHolder.getContext().getAuthentication()
				.getName();

		Users users = usersRepository.findByEmail(user);
		if (CollectionUtils.isNotEmpty(p.getLikes()) || !p.getDislikes().contains(users)) {
			p.getDislikes().add(users);
		} else {
			return p;
		}
		
		emailService.sendMessage(p.getUser(),
				String.format("Post is disliked by %s!", users.getFullname()),
				"Post : " + p.getContent());
		return postRepository.saveAndFlush(p);
	}

	@Override
	public Post rsvp(Long id, int guests) {
		Post p = postRepository.findOne(id);
		String user = SecurityContextHolder.getContext().getAuthentication()
				.getName();

		Users users = usersRepository.findByEmail(user);
		Rsvp rsvp = new Rsvp();
		rsvp.setGuests(guests);
		rsvp.setUser(users);
		
		Long r = null;
		if (CollectionUtils.isNotEmpty(p.getRsvps()))
		for (Rsvp rsvp2: p.getRsvps()) {
			if (rsvp2.getUser().equals(users)) {
				r = rsvp2.getId();
				break;
			}
		}
		
		if (r != null) {
			Rsvp r1 = new Rsvp();
			r1.setId(r);
			
			p.getRsvps().remove(r1);
			p.getRsvps().add(rsvp);
			emailService.sendMessage(p.getUser(),
					String.format("Post %s Rsvp is updated by %s guests %s!", p.getId(), users.getFullname(), guests),
					"Post : " + p.getContent());
		} else {
			p.getRsvps().add(rsvp);
			emailService.sendMessage(p.getUser(),
					String.format("Post %s is Rsvp by %s guests %s!", p.getId(), users.getFullname(), guests),
					"Post : " + p.getContent());
		}
		
		return postRepository.saveAndFlush(p);
	}
}
