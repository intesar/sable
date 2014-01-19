package com.sable.cb.domain;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@Entity
public class Users {

	/**
     */
	@NotNull
	private String fullname;

	/**
     */
	@NotNull
	private String email;

	/**
     */
	@NotNull
	private String password;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

	@ManyToMany(cascade=CascadeType.ALL)
	Set<Organization> followedOrgs = new HashSet<Organization>();

	@ManyToMany(cascade=CascadeType.ALL)
	Set<Organization> adminOrgs = new HashSet<Organization>();

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

	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String getFullname() {
		return this.fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Organization> getFollowedOrgs() {
		return followedOrgs;
	}

	public void setFollowedOrgs(Set<Organization> followedOrgs) {
		this.followedOrgs = followedOrgs;
	}

	public Set<Organization> getAdminOrgs() {
		return adminOrgs;
	}

	public void setAdminOrgs(Set<Organization> adminOrgs) {
		this.adminOrgs = adminOrgs;
	}

}
