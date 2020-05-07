package co.proarea.controllers;

import co.proarea.dto.UserDTO;
import co.proarea.models.User;
import co.proarea.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/api/v1/user/")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @PostMapping(value = "register")
//    public UserDTO registerUser(@RequestBody User user){
//        try {
//            return UserDTO.fromUser(userService.register(user));
//        } catch (IllegalArgumentException iae) {
//            throw new ResponseStatusException(
//                    HttpStatus.FOUND, "User name '" + user.getUsername() + "'" +
//                    " or User email '"+user.getEmail()+"' already exists");
//        }
//    }

    @PostMapping(value = "{id}")
    public UserDTO getUserById(@PathVariable(name = "id") Long id){
        try {
            return UserDTO.fromUser(userService.getUserById(id));
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No User with ID: '" + id + "'");
        }
    }

    @PostMapping(value = "update")
    public UserDTO updateUser(@RequestBody UserDTO userDTO){
        try {
            return UserDTO.fromUser(userService.update(userDTO));
        } catch (IllegalArgumentException iae) {
            throw new ResponseStatusException(
                    HttpStatus.FOUND, "User name '" + userDTO.getUsername() + "'" +
                    " or User email '" + userDTO.getEmail()+"' already exists");
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No User with ID: '" + userDTO.getId() + "'");
        }
    }
}