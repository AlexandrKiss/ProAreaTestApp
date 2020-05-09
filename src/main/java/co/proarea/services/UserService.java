package co.proarea.services;

import co.proarea.dto.UserDTO;
import co.proarea.models.Role;
import co.proarea.models.Status;
import co.proarea.models.User;

import java.util.List;

public interface UserService {

    void addRole(Role role);

    User register(User user);

    User update(UserDTO userDTO);

    List<User> getAllUser();

    User getByUsername(String username);

    User getUserById(Long id);

    User setStatus(String userName, Status status);

    User getByEmail(String email);

    String createEmailToken(User user);

    User validateEmailToken(String token);

    boolean updatePassword(User user);

    void deleteUser(Long id);
}
