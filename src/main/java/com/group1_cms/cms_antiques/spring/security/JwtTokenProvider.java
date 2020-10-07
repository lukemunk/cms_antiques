package com.group1_cms.cms_antiques.spring.security;

import com.group1_cms.cms_antiques.models.User;
import com.group1_cms.cms_antiques.services.UserService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Date;

public class JwtTokenProvider {

    public static String JWT_COOKIE_NAME = "sessionId";

    @Value("${security.jwt.token.secret-key}")
    private  String secretKey;

    @Value("${security.jwt.token.ttl}")
    private Duration ttl;

    private static final String BEARER_STRING = "Bearer ";

    private final UserService userService;

    @Autowired
    public JwtTokenProvider(UserService userService){
        this.userService = userService;
    }

    public Authentication getAuthentication(HttpServletRequest request){
        String token = getJwtTokenFromRequest(request);
        if(token != null && validateToken(token)){
            return getAuthentication(token);
        }
        return null;
    }

    public Authentication getAuthentication(String token){
        String username = getUsername(token);
        if(username != null){
            UserDetails userDetails = userService.loadUserByUsername(username);
            if(userDetails != null && userDetails.getUsername() != null){
                return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
            }
        }
        return null;
    }

    public String getUsername(String token){
        Jws<Claims> jws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        if(jws != null){
            Claims claims = jws.getBody();
            if(claims != null){
                return claims.get("username", String.class);
            }
        }
        return null;
    }

    public Date getExpiredTime(String token){
        return Jwts.parser().parseClaimsJwt(token).getBody().getExpiration();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        }
        catch (JwtException | IllegalArgumentException e){
            System.out.println("Error validating token");
        }
        return false;
    }

    public String getJwtTokenFromRequest(HttpServletRequest request){
        String response = getJwtTokenFromAuthHeader(request);
        if(response == null){
            response = getJwtTokenFromCookie(request);
        }
        return response;
    }

    public String getJwtTokenFromAuthHeader(HttpServletRequest request){
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(bearerToken != null && bearerToken.startsWith(BEARER_STRING)){
            String[] segments = bearerToken.split(BEARER_STRING);
            if(segments.length > 1){
                return segments[1];
            }
        }
        return null;
    }

    public String getJwtTokenFromCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie: cookies){
                if (JWT_COOKIE_NAME.equalsIgnoreCase(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public String createCookieTokenString(Authentication authentication){
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + ttl.toMillis());
        String jwt = createToken(authentication, expirationDate);
        return JWT_COOKIE_NAME + "=" + jwt + ";Max-Age=" + ttl.toSeconds() + ";Path=/";
    }

    public String createToken(User user, Date expirationTime){
        if (user != null){
            Claims claims = Jwts.claims().setSubject(user.getId().toString());
            claims.put("perms", user.getPermissions());
            claims.put("username", user.getUsername());
            claims.put("firstName", user.getFirstName());
            claims.put("lastName", user.getLastName());

            Date now = new Date();

            Date validity = expirationTime;

            if(validity == null){
                validity = new Date(now.getTime() + ttl.toMillis());
            }

            return Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(now)
                    .setExpiration(validity)
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compact();
        }
        return null;
    }

    public String createToken(Authentication authentication, Date date){
        if(authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof User){
            User user = (User) authentication.getPrincipal();
            return createToken(user, date);
        }
        return null;
    }
}
