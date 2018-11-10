package org.nure.diagnosis.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JwtTokenProvider {

    @Autowired
    private DiagnosisUserDetailsService diagnosisUserDetailsService;

    @Value("${token.settings.signKey}")
    private String signingKey;

    @Value("${token.settings.tokenValid}")
    private long tokenValid;

    @Value("${token.settings.authoritiesKey}")
    private String authoritiesKey;

    @Value("${token.settings.tokenPrefix}")
    private String tokenPrefix;

    private final String authoritiesSeparator = ",";

    private  <T> T getClaimFromClaims(Claims claims, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(claims);
    }

    private JwtParser getParser() {
        return Jwts
                .parser()
                .setSigningKey(signingKey);
    }

    public UsernamePasswordAuthenticationToken validateToken(String token) {
        Claims claims = getParser()
                .parseClaimsJws(token)
                .getBody();
        String tokenUsername = getClaimFromClaims(claims, Claims::getSubject);
        UserDetails details = diagnosisUserDetailsService.loadUserByUsername(tokenUsername);
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(authoritiesKey).toString().split(authoritiesSeparator))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(details, "", authorities);
    }

    public String generateToken(Authentication auth) {
        String authorities = auth
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(authoritiesSeparator));
        return Jwts
                .builder()
                .setSubject(auth.getName())
                .claim(authoritiesKey, authorities)
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValid * 1000))
                .compact();
    }
}
