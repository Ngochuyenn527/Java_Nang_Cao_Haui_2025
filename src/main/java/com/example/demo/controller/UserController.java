package com.example.demo.controller;

import com.example.demo.config.CurrentUser;
import com.example.demo.config.MyExceptionConfig;
import com.example.demo.config.UserPrincipal;
import com.example.demo.constant.SystemConstant;
import com.example.demo.model.dto.PasswordDTO;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.model.dto.UserRequestDTO;
import com.example.demo.service.EmailService;
import com.example.demo.service.OtpService;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tags;
@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private OtpService otpService;


    @Operation(summary = "API get all users which have status = 1")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsersHasStatus1() {
        return ResponseEntity.ok(userService.getAllUsersHasStatus1());
    }


    @Operation(summary = "API get user by id")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }


    @Operation(summary = "API get user by username")
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByUsernameAndStatus(@PathVariable("username") String username) {
        return ResponseEntity.ok(userService.getUserByUserNameAndStatus(username, 1)); // status = 1: đang hoạt động
    }


    @Operation(summary = "API get user by rolecode")
    @GetMapping("/role/{roleCode}")
    public ResponseEntity<List<UserDTO>> getUsersByRoleCode(@PathVariable String roleCode) {
        List<UserDTO> result = userService.getUsersByRoleCode(roleCode);
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "API create new user")
    @PostMapping
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO newUser) {
        return ResponseEntity.ok(userService.addUser(newUser));
    }


    @Operation(summary = "API update user")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") long id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    }


    @Operation(summary = "API change password by user id")
    @PutMapping("/change-password/{id}")
    public ResponseEntity<String> changePasswordUser(@PathVariable("id") long id, @RequestBody PasswordDTO passwordDTO) {
        try {
            userService.updatePassword(id, passwordDTO);
            return ResponseEntity.ok("Password updated successfully");
        } catch (MyExceptionConfig e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Change password failed: " + e.getMessage());
        }
    }


    @PutMapping("/password/{id}/reset")
    public ResponseEntity<UserDTO> resetPassword(@PathVariable("id") long id) {
        return ResponseEntity.ok(userService.resetPassword(id));
    }


    @Operation(summary = "API delete user by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsers(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Deleted successfully!");
    }
    
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        String otp = otpService.generateOtp(email);
        emailService.sendOtpEmail(email, otp);
        return ResponseEntity.ok("Đã gửi mã OTP");
    }
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequestDTO dto) {
        userService.register(dto);
        return ResponseEntity.ok("Đăng ký thành công");
    }
    
    @Tags({@Tag(name = "user-controller-admin"), @Tag(name = "user-controller")})
    @Operation(summary = "API get current user login")
    @GetMapping("/getCurrentUser")
    public ResponseEntity<?> getCurrentUser(@Parameter(name = "principal", hidden = true)
                                            @CurrentUser UserPrincipal principal) {
        return ResponseEntity.ok(userService.getCurrentUser(principal));
    }
}
