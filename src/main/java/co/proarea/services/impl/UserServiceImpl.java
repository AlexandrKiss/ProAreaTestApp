package co.proarea.services.impl;

import co.proarea.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import co.proarea.models.Role;
import co.proarea.models.Status;
import co.proarea.models.User;
import co.proarea.repositories.RoleRepository;
import co.proarea.repositories.UserRepository;
import co.proarea.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void addRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    @Transactional
    public User register(User user) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        if (userRepository.findAll().size() == 0) {
            roleUser = roleRepository.findByName("ROLE_ADMIN");
            userRoles.add(roleUser);
        } else {
            String email = user.getEmail();
            if (userRepository.findByEmail(email) != null) {
                log.warn("IN register - a User email {} already exists", email);
                throw new IllegalArgumentException();
            }
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(Status.ACTIVE);

        User registeredUser = userRepository.save(user);

        log.info("IN register - user: {} successfully registered", registeredUser);
        return registeredUser;
    }

    @Override
    @Transactional
    public User update(UserDTO userDTO){
        User user = userRepository.findById(userDTO.getId()).orElse(null);
        if (user == null) {
            log.warn("IN update - no User found by ID: {}", userDTO.getId());
            throw new NullPointerException();
        }
        log.info("IN update - User: {} successfully updated", user);
        userRepository.save(user);
        return user;
    }

    @Transactional(readOnly = true)
    public List<User> getAllUser() {
        List<User> result = userRepository.findAll();
        log.info("IN getAll - {} users found", result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        User result = userRepository.findByUsername(username);
        log.info("IN findByUsername - user: {} found by username: {}", result, username);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        User result = userRepository.findById(id).orElse(null);
        if (result == null) {
            log.warn("IN getUserById - no user found by id: {}", id);
            throw new NullPointerException();
        }
        log.info("IN getUserById - user: {}", result);
        return result;
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        log.info("IN delete - user with id: {} successfully deleted", id);
    }

    @Override
    @Transactional
    public User setStatus(Long id, Status status) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            log.warn("IN setStatus - no User found by ID: {}", id);
            throw new NullPointerException();
        }
        for (Role role: user.getRoles()) {
            if (role.getName().equals("ROLE_ADMIN")) {
                log.warn("IN setStatus - Administrator status cannot be changed");
                throw new IllegalArgumentException();
            }
        }
        user.setStatus(status);
        log.info("IN setStatus - User: {} status successfully changed", user);
        userRepository.save(user);
        return user;
    }
}
