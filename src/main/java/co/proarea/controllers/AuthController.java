package co.proarea.controllers;

import co.proarea.dto.AuthenticationRequestDTO;
import co.proarea.dto.PasswordDTO;
import co.proarea.dto.UserDTO;
import co.proarea.models.User;
import co.proarea.security.jwt.JwtTokenProvider;
import co.proarea.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/auth/")
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    private final JavaMailSender emailSender;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          UserService userService,
                          @Qualifier("getJavaMailSender") JavaMailSender emailSender) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.emailSender = emailSender;
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody AuthenticationRequestDTO requestDto) {
        try {
            String username = requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            User user = userService.getByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }

            String token = jwtTokenProvider.createToken(username, user.getRoles());

            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PostMapping(value = "register")
    public UserDTO registerUser(@RequestBody User user){
        try {
            return UserDTO.fromUser(userService.register(user));
        } catch (IllegalArgumentException iae) {
            throw new ResponseStatusException(
                    HttpStatus.FOUND, "User name '" + user.getUsername() + "'" +
                    " or User email '"+user.getEmail()+"' already exists");
        }
    }

    @PostMapping("email")
    public void sendEmail(@RequestParam("email") String email, HttpServletRequest request) {
        try {
            String token = userService.createEmailToken(userService.getByEmail(email));

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Password recovery");
            message.setText("Hello, Im testing reset password. Your password reset link:\n" +
                    getAppUrl(request)+"/token/?token="+token+"\n"+
                    "Link valid for 1 hour");
            emailSender.send(message);
            log.info("Password reset email sent. To: "+ Arrays.toString(message.getTo()));
        } catch (NullPointerException npe) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "No User with email: '" + email + "'");
        }
    }

    @GetMapping("email/token")
    public boolean validateToken(@RequestParam("token") String token) {
        return userService.validateEmailToken(token) != null; //if true then "redirect:/updatePassword.html"
    }

    @PutMapping("user/updatePassword")
    public boolean updatePassword(@RequestBody PasswordDTO passwordDTO) {
        User user = userService.validateEmailToken(passwordDTO.getToken());
        user.setPassword(passwordDTO.getNewPassword());
        return userService.updatePassword(user);
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getServletPath();
    }
}
