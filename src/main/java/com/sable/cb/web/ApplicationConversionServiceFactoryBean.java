package com.sable.cb.web;

import com.sable.cb.domain.Organization;
import com.sable.cb.domain.Post;
import com.sable.cb.domain.Users;
import com.sable.cb.service.OrganizationService;
import com.sable.cb.service.PostService;
import com.sable.cb.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

@Configurable
/**
 * A central place to register application converters and formatters. 
 */
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}

	@Autowired
    OrganizationService organizationService;

	@Autowired
    PostService postService;

	@Autowired
    UsersService usersService;

	public Converter<Organization, String> getOrganizationToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.sable.cb.domain.Organization, java.lang.String>() {
            public String convert(Organization organization) {
                return new StringBuilder().append(organization.getName()).append(' ').append(organization.getStreet()).append(' ').append(organization.getCity()).append(' ').append(organization.getZipcode()).toString();
            }
        };
    }

	public Converter<Long, Organization> getIdToOrganizationConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.sable.cb.domain.Organization>() {
            public com.sable.cb.domain.Organization convert(java.lang.Long id) {
                return organizationService.findOrganization(id);
            }
        };
    }

	public Converter<String, Organization> getStringToOrganizationConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.sable.cb.domain.Organization>() {
            public com.sable.cb.domain.Organization convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Organization.class);
            }
        };
    }

	public Converter<Post, String> getPostToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.sable.cb.domain.Post, java.lang.String>() {
            public String convert(Post post) {
                return new StringBuilder().append(post.getPostType()).append(' ').append(post.getContent()).append(' ').append(post.getExpiration()).toString();
            }
        };
    }

	public Converter<Long, Post> getIdToPostConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.sable.cb.domain.Post>() {
            public com.sable.cb.domain.Post convert(java.lang.Long id) {
                return postService.findPost(id);
            }
        };
    }

	public Converter<String, Post> getStringToPostConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.sable.cb.domain.Post>() {
            public com.sable.cb.domain.Post convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Post.class);
            }
        };
    }

	public Converter<Users, String> getUsersToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.sable.cb.domain.Users, java.lang.String>() {
            public String convert(Users users) {
                return new StringBuilder().append(users.getFullname()).append(' ').append(users.getEmail()).append(' ').append(users.getPassword()).toString();
            }
        };
    }

	public Converter<Long, Users> getIdToUsersConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.sable.cb.domain.Users>() {
            public com.sable.cb.domain.Users convert(java.lang.Long id) {
                return usersService.findUsers(id);
            }
        };
    }

	public Converter<String, Users> getStringToUsersConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.sable.cb.domain.Users>() {
            public com.sable.cb.domain.Users convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Users.class);
            }
        };
    }

	public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getOrganizationToStringConverter());
        registry.addConverter(getIdToOrganizationConverter());
        registry.addConverter(getStringToOrganizationConverter());
        registry.addConverter(getPostToStringConverter());
        registry.addConverter(getIdToPostConverter());
        registry.addConverter(getStringToPostConverter());
        registry.addConverter(getUsersToStringConverter());
        registry.addConverter(getIdToUsersConverter());
        registry.addConverter(getStringToUsersConverter());
    }

	public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
}
