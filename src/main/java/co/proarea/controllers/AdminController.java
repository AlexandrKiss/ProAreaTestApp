package co.proarea.controllers;

import co.proarea.dto.AdminUserDTO;
import co.proarea.dto.ProductUnitDTO;
import co.proarea.services.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping(value = "{id}")
    @ApiOperation(value = "(ROLE_ADMIN)",response = AdminUserDTO.class)
    public AdminUserDTO getUserById(@PathVariable(name = "id") Long id) {
        try {
            return AdminUserDTO.fromUser(userService.getUserById(id));
        } catch (NullPointerException npe) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No User with ID: '" + id + "'");
        }
    }
}
