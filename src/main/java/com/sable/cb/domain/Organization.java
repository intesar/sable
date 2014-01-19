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
import javax.persistence.ManyToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@Entity
public class Organization {

	/**
     */
	@NotNull
	private String name;

	/**
     */

	private String street;

	/**
     */

	private String city;

	/**
     */

	private String zipcode;

	/**
     */

	private String countryState;

	/**
     */

	private String country;

	@ManyToMany(cascade=CascadeType.ALL)
	Set<Users> followers = new HashSet<Users>();

	@ManyToMany(cascade=CascadeType.ALL)
	Set<Users> admins = new HashSet<Users>();

	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet() {
		return this.street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipcode() {
		return this.zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCountryState() {
		return this.countryState;
	}

	public void setCountryState(String countryState) {
		this.countryState = countryState;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

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

	public Set<Users> getFollowers() {
		return followers;
	}

	public void setFollowers(Set<Users> followers) {
		this.followers = followers;
	}

	public Set<Users> getAdmins() {
		return admins;
	}

	public void setAdmins(Set<Users> admins) {
		this.admins = admins;
	}

}
