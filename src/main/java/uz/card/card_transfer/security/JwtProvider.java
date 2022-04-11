package uz.card.card_transfer.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    static long expireTime = 36_000_000;
    static String maxfiy = "MeningbirinchiJwttokengenratsiyaqilishim";
    // bu jwete tokeni username orqali generatsiya qiladi
    public String generateToken(String userName){
        return  Jwts
                .builder()
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(SignatureAlgorithm.HS512, maxfiy)
                .compact();
    }
    //token aynan yaroqli va shu userni tokeni ekanini tekshiradi
    public boolean validateToken(String token){
        try {
                    Jwts
                        .parser()
                        .setSigningKey(maxfiy)
                        .parseClaimsJws(token);
                return true;
        }catch (SignatureException ex){
            System.err.println("Invalid  Jwt signature");
        }catch (MalformedJwtException ml){
            System.err.println("Invalid jwt token");
        }catch (ExpiredJwtException expiredJwtException){
            System.err.println("Expired JWT token");
        }
        return false;
    }

    // bu metod jwtni pars qilib usernameni olib beradi

    public String getUsernameFromToken(String token){
        String userName = Jwts
                .parser()
                .setSigningKey(maxfiy)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return userName;
    }

}
