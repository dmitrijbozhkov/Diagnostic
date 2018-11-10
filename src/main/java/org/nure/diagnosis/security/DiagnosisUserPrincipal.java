package org.nure.diagnosis.security;

import org.nure.diagnosis.models.enums.PersonAuthorities;
import org.nure.diagnosis.models.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class DiagnosisUserPrincipal implements UserDetails {

    private final String email;
    private final String password;
    private final List<PersonAuthorities> userAutority;


    public DiagnosisUserPrincipal(Person person) {
        this.email = person.getEmail();
        this.password = person.getPassword();
        this.userAutority = PersonAuthorities.parseAuthorities(person.getLabels());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<GrantedAuthority>(this.userAutority);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
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
}
