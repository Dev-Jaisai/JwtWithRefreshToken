package com.Jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service for handling JWT operations like creation, parsing, and validation.
 *
 * @author Jaisai
 * @version 1.0
 * @since 2024-09-03
 */
@Component
public class JwtService {

    // The secret key used for signing JWTs. It should be kept secure and not exposed.
    public static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";

    /**
     * Extracts the username from the JWT token.
     *
     * @param token the JWT token
     * @return the username from the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims :: getSubject);
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token the JWT token
     * @return the expiration date of the token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims :: getExpiration);
    }

    /**
     * Generic method to extract a specific claim from a token.
     *
     * @param token          the JWT token
     * @param claimsResolver a function to extract the desired claim
     * @param <T>            the type of the claim to extract
     * @return the extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the given JWT token.
     *
     * @param token the JWT token
     * @return all claims as a Claims object
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if the JWT token has expired.
     *
     * @param token the JWT token
     * @return true if the token has expired, false otherwise
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Validates a JWT token against a UserDetails object.
     *
     * @param token       the JWT token
     * @param userDetails the user details to validate against
     * @return true if the token is valid, false otherwise
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Generates a JWT token for a specified username.
     *
     * @param username the username for which the token is generated
     * @return a JWT token as a String
     */
    public String GenerateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    /**
     * Creates a JWT token with claims and subject.
     *
     * @param claims   the claims to set in the token
     * @param username the subject of the token
     * @return a JWT token as a String
     */
    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 60 mins
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Retrieves the signing key from a BASE64-encoded string.
     *
     * @return a Key object used for signing JWTs
     */
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
