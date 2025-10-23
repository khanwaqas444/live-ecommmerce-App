
package com.livecommerce.auth.jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  private static final String SECRET_BASE64 = "MDEyMzQ1Njc4OUFCQ0RFRjAxMjM0NTY3ODlBQkNERUYwMTI="; // demo secret (base64)
  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_BASE64));
  }
  public String createAccessToken(String sub, Map<String,Object> claims, long ttlSeconds){
    Instant now = Instant.now();
    return Jwts.builder()
        .setSubject(sub)
        .addClaims(claims)
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plusSeconds(ttlSeconds)))
        .signWith(key(), SignatureAlgorithm.HS256)
        .compact();
  }
}
