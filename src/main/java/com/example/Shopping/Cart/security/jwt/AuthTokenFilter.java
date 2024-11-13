package com.example.Shopping.Cart.security.jwt;

import com.example.Shopping.Cart.security.user.ShopUserDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
//được sử dụng để lọc và xác thực các yêu cầu HTTP thông qua JWT (JSON Web Token). Nó mở rộng lớp OncePerRequestFilter, có nghĩa là filter này sẽ chỉ chạy một lần cho mỗi yêu cầu.
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils; //Được sử dụng để xử lý các thao tác liên quan đến JWT như tạo, xác thực, và lấy thông tin từ token.
    @Autowired
    private ShopUserDetailsService userDetailsService; //Dùng để tải thông tin chi tiết của người dùng từ cơ sở dữ liệu dựa trên tên đăng nhập (username), thường để kiểm tra người dùng và phân quyền.

    //Đây là phương thức chính của filter, được gọi cho mỗi yêu cầu HTTP.
    // Nó chịu trách nhiệm kiểm tra tính hợp lệ của JWT và xác thực người dùng nếu token hợp lệ.
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, //Yêu cầu HTTP từ client.
            @NonNull HttpServletResponse response, // Phản hồi HTTP trả về cho client.
            @NonNull FilterChain filterChain //Chuỗi các filter khác trong hệ thống, để chuyển yêu cầu đến các filter tiếp theo sau khi hoàn thành việc xử lý tại đây.
    ) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request); //Sử dụng phương thức parseJwt(request) để lấy JWT từ header Authorization của yêu cầu HTTP.
            if (StringUtils.hasText(jwt) && jwtUtils.validateToken(jwt)) {
                String username = jwtUtils.getUsernameFromToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); //chứa thông tin người dùng và các quyền (authorities).
                SecurityContextHolder.getContext().setAuthentication(auth); //Đặt đối tượng xác thực này vào SecurityContextHolder, nơi Spring Security giữ thông tin về người dùng đã đăng nhập.
            }
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(e.getMessage() +" : Invalid or expired token, you may login and try again!");
            return;
        }catch (Exception e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(e.getMessage());
            return;

        }
        filterChain.doFilter(request, response);
    }

    //Phương thức này chịu trách nhiệm tách JWT từ yêu cầu HTTP.
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return  null;
    }
}
