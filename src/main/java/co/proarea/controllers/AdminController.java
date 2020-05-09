package co.proarea.controllers;

import co.proarea.dto.AdminUserDTO;
import co.proarea.dto.UserDTO;
import co.proarea.models.Status;
import co.proarea.services.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.RolesAllowed;

@RestController
@RolesAllowed("ROLE_ADMIN")
@RequestMapping(value = "/api/v1/admin/")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "{userName}")
    @ApiOperation(value = "(ROLE_ADMIN)",response = AdminUserDTO.class)
    public AdminUserDTO getUserById(@PathVariable(name = "userName") String userName) {
        try {
            return AdminUserDTO.fromUser(userService.getByUsername(userName));
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No User with Name: '" + userName + "'");
        }
    }

    @PutMapping(value = "status/{userName}")
    @ApiOperation(value = "ACTIVE, DELETED, BANNED (ROLE_ADMIN)",response = UserDTO.class)
    public UserDTO setStatus(@PathVariable(name = "userName") String userName, @RequestParam("status") Status status) {
        try {
            return UserDTO.fromUser(userService.setStatus(userName, status));
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No User with Name: '" + userName + "'");
        } catch (IllegalArgumentException iae) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cannot banned administrator");
        }
    }
}
