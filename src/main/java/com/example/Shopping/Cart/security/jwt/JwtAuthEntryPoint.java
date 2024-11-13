package com.example.Shopping.Cart.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
//để xử lý các trường hợp khi người dùng cố gắng truy cập tài nguyên mà không có quyền hoặc không được xác thực hợp lệ.
//Class này triển khai interface AuthenticationEntryPoint, một thành phần trong Spring Security chịu trách nhiệm xử lý các lỗi xác thực (authentication errors).
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    //Đây là phương thức quan trọng duy nhất trong class này và nó được gọi khi có một lỗi xác thực xảy ra (ví dụ: người dùng không cung cấp token hoặc token không hợp lệ).
    @Override
    public void commence(
            HttpServletRequest request, //Yêu cầu HTTP từ client
            HttpServletResponse response, //Phản hồi HTTP gửi về client
            AuthenticationException authException // Ngoại lệ phát sinh khi quá trình xác thực không thành công.
    ) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // Thiết lập loại dữ liệu phản hồi là JSON (application/json) để trả về thông tin lỗi dưới dạng JSON.
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Đặt mã trạng thái phản hồi là 401 Unauthorized, cho biết người dùng chưa được xác thực hoặc không có quyền truy cập tài nguyên.

        //Tạo body phản hồi
        final Map<String, Object> body = new HashMap<>(); //để chứa các thông tin lỗi, trong trường hợp này bao gồm:
        // body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized"); //Mô tả loại lỗi là "Unauthorized" (Chưa được xác thực
        body.put("message", "You may login and try again!"); //Thông báo gợi ý cho người dùng rằng họ cần đăng nhập lại để thử lại.
        /// body.put("path", request.getServletPath());

        //Sử dụng ObjectMapper của Jackson để chuyển đổi map chứa thông tin lỗi thành JSON và ghi vào phản hồi HTTP (response.getOutputStream()).
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
