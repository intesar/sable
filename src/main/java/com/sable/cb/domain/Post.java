package com.sable.cb.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

	/**
     */
	@NotNull
	private String postType;

	/**
     */
	@NotNull
	private String content;

	private String status;

	/**
     */
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	private Date expiration;

	private String user;

	@ManyToMany
	private Set<Organization> postedOrgs = new HashSet<Organization>();

	@ManyToMany
	private Set<Organization> approvedOrgs = new HashSet<Organization>();

	@ManyToMany
	private Set<Organization> rejectedOrgs = new HashSet<Organization>();

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getPostType() {
		return this.postType;
	}

	public void setPostType(String postType) {
		this.postType = postType;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getExpiration() {
		return this.expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Set<Organization> getPostedOrgs() {
		return postedOrgs;
	}

	public void setPostedOrgs(Set<Organization> postedOrgs) {
		this.postedOrgs = postedOrgs;
	}

	public Set<Organization> getApprovedOrgs() {
		return approvedOrgs;
	}

	public void setApprovedOrgs(Set<Organization> approvedOrgs) {
		this.approvedOrgs = approvedOrgs;
	}

	public Set<Organization> getRejectedOrgs() {
		return rejectedOrgs;
	}

	public void setRejectedOrgs(Set<Organization> rejectedOrgs) {
		this.rejectedOrgs = rejectedOrgs;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
