package co.proarea.services;

import co.proarea.dto.UserDTO;
import co.proarea.models.Role;
import co.proarea.models.User;

import java.util.List;

public interface UserService {

    void addRole(Role role);

    User register(User user);

    User update(UserDTO userDTO);

    List<User> getAllUser();

    User getByUsername(String username);

    User getUserById(Long id);

    void deleteUser(Long id);
}
