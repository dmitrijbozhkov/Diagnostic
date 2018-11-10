package org.nure.diagnosis.models.enums;

import org.springframework.security.core.GrantedAuthority;

import java.util.*;

public enum PersonAuthorities implements GrantedAuthority {
    PATIENT("ROLE_PATIENT"),
    DOCTOR("ROLE_DOCTOR"),
    ADMIN("ROLE_ADMIN");

    private final String authorithyName;

    private PersonAuthorities(String authorithyName) {
        this.authorithyName = authorithyName;
    }

    @Override
    public String getAuthority() {
        return this.authorithyName;
    }

    public static Optional<PersonAuthorities> findAuthority(PersonAuthorities authority, List<String> authorities) {
        return authorities
                .stream()
                .filter((a) -> {
                    return authority.getAuthority().equals(a);
                })
                .map((a) -> authority)
                .findFirst();
    }

    public static List<PersonAuthorities> parseAuthorities(List<String> authorities) {
        List<PersonAuthorities> personAuthorities = new ArrayList<>();
        Optional<PersonAuthorities> patientAuthority = findAuthority(PersonAuthorities.PATIENT, authorities);
        Optional<PersonAuthorities> doctorAuthority = findAuthority(PersonAuthorities.DOCTOR, authorities);
        Optional<PersonAuthorities> adminAuthority = findAuthority(PersonAuthorities.ADMIN, authorities);
        if (patientAuthority.isPresent()) {
            personAuthorities.add(patientAuthority.get());
        }
        if (doctorAuthority.isPresent()) {
            personAuthorities.add(doctorAuthority.get());
        }
        if (adminAuthority.isPresent()) {
            personAuthorities.add(adminAuthority.get());
        }
        return personAuthorities;
    }
}
