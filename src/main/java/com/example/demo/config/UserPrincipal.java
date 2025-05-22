package com.example.demo.config;

import com.example.demo.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class UserPrincipal implements UserDetails {

    private final Long id;

    private final String fullName;

    @JsonIgnore
    private final String userName;

    @JsonIgnore
    private String password;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(String userName, String password, Collection<? extends GrantedAuthority> authorities) {
        this(null, null, userName, password, authorities);
    }

    public UserPrincipal(Long id, String fullName, String userName, String password,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.fullName = fullName;
        this.userName = userName;
        this.password = password;

        if (authorities == null) {
            this.authorities = null;
        } else {
            this.authorities = new ArrayList<>(authorities);
        }
    }

    public static UserPrincipal create(UserEntity user) {
        List<GrantedAuthority> authorities = new LinkedList<>();
        if (user.getRole() != null && user.getRole().getName() != null) {
            authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
        }
        return new UserPrincipal(
                user.getId(),
                user.getFullName(),
                user.getUserName(),
                user.getPassword(),
                authorities
        );
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities == null ? null : new ArrayList<>(authorities);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        UserPrincipal that = (UserPrincipal) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}