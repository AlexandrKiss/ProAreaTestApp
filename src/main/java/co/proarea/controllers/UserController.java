package co.proarea.controllers;

import co.proarea.dto.UserDTO;
import co.proarea.models.User;
import co.proarea.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/api/v1/user/")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "{userName}")
    public UserDTO getUserByUserName(@PathVariable(name = "userName") String userName){
        try {
            return UserDTO.fromUser(userService.getByUsername(userName));
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No User with Name: '" + userName + "'");
        }
    }

    @PostMapping(value = "update")
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {
        try {
            return UserDTO.fromUser(userService.update(userDTO));
        } catch (IllegalArgumentException iae) {
            throw new ResponseStatusException(
                    HttpStatus.FOUND, "User name '" + userDTO.getUsername() + "'" +
                    " or User email '" + userDTO.getEmail()+"' already exists");
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No User with Name: '" + userDTO.getUsername() + "'");
        }
    }
}