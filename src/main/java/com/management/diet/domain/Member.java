package com.management.diet.domain;

import com.management.diet.enums.Theme;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

import static javax.persistence.EnumType.STRING;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member implements UserDetails {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long member_idx;

    @Column(name = "email")
    private String email;

    @Column(name="name")
    private String name;

    @Column(name="password")
    private String password;

    @Column(name="theme")
    @Enumerated(STRING)
    private Theme theme;

    @Column(name="profile")
    private String profile;

    public void updateProfile(String profile){
        this.profile=profile;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
