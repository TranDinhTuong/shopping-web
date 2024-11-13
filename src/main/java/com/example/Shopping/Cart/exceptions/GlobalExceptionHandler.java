package com.example.Shopping.Cart.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    //thường xảy ra khi các tham số đầu vào không hợp lệ (chẳng hạn như không thỏa mãn các ràng buộc xác thực).
    @ExceptionHandler(MethodArgumentNotValidException.class )
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        //Nó lấy các lỗi xác thực từ ex.getBindingResult().getFieldErrors() và đưa vào Map<String, String>, trong đó khóa là tên trường bị lỗi, còn giá trị là thông báo lỗi tương ứng.
        for (FieldError error : ex.getBindingResult().getFieldErrors()){
            errors.put(error.getField(), error.getDefaultMessage());
        }
        //Phản hồi lại với mã trạng thái HTTP 400 BAD REQUEST cùng với thông tin lỗi.
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    //Phương thức này xử lý lỗi AccessDeniedException, thường xảy ra khi người dùng không có quyền thực hiện hành động (vi phạm quyền truy cập).
    //Nó trả về một thông báo "Bạn không có quyền thực hiện hành động này" với mã trạng thái HTTP 403 FORBIDDEN.
    @ExceptionHandler(AccessDeniedException.class )
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        String message = "You do not have permission to this action";
        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
    }
}
