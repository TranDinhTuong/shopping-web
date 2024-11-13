package com.example.Shopping.Cart.security.jwt;

import com.example.Shopping.Cart.security.user.ShopUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

//giúp bảo vệ các API hoặc trang web của ứng dụng khỏi các truy cập trái phép.
@Component
public class JwtUtils {

    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.token.expirationInMils}")
    private int expirationTime; //thoi gian het han cua token

    public String generateTokenForUser(Authentication authentication) {
        ShopUserDetails userPrincipal = (ShopUserDetails) authentication.getPrincipal();

        List<String> roles = userPrincipal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder()
                .setSubject(userPrincipal.getEmail()) //Đặt email của người dùng làm subject của token.
                .claim("id", userPrincipal.getId()) //Thêm thuộc tính id của người dùng vào token.
                .claim("roles", roles) // Thêm danh sách vai trò của người dùng vào token.
                .setIssuedAt(new Date()) //Đặt thời điểm phát hành của token.
                .setExpiration(new Date((new Date()).getTime() + expirationTime))//Đặt thời gian hết hạn của token.
                .signWith(key(), SignatureAlgorithm.HS256).compact();  //Tạo chữ ký cho token bằng thuật toán HS256 với khóa bí mật.
    }

    //Tạo khóa bảo mật từ chuỗi bí mật jwtSecret.
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    //Lấy email (username) của người dùng từ JWT.
    public String getUsernameFromToken(String token) {
        return Jwts.parser() //để phân tích (parse) JWT.
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject(); // Trả về subject của token, tức là email của người dùng.
    }

    //Kiểm tra tính hợp lệ của JWT.
    public boolean validateToken(String token) {
        try {
            Jwts.parser() //để phân tích và kiểm tra token.
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            throw new JwtException(e.getMessage());
        }
    }

}
