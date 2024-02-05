package de.tekup.complaintsclaims.service.impl;

import de.tekup.complaintsclaims.dto.request.RoleRequest;
import de.tekup.complaintsclaims.dto.request.UserRequest;
import de.tekup.complaintsclaims.dto.response.UserResponse;
import de.tekup.complaintsclaims.entity.Role;
import de.tekup.complaintsclaims.entity.User;
import de.tekup.complaintsclaims.enums.Roles;
import de.tekup.complaintsclaims.exception.RoleServiceException;
import de.tekup.complaintsclaims.exception.UserServiceException;
import de.tekup.complaintsclaims.repository.RoleRepository;
import de.tekup.complaintsclaims.repository.UserRepository;
import de.tekup.complaintsclaims.service.SecretKeyService;
import de.tekup.complaintsclaims.service.UserService;
import de.tekup.complaintsclaims.util.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final SecretKeyService secretKeyService;

    @Override
    public List<UserResponse> findUsers() {
        try {
            List<User> users = userRepository.findAll();

            return users
                    .stream()
                    .sorted(Comparator.comparingLong(User::getId).reversed())
                    .map(Mapper::userToUserResponse)
                    .toList();
        } catch (Exception exception) {
            log.error("UserService::findUsers " + exception.getMessage());
            throw new UserServiceException(exception.getMessage());
        }
    }

    @Override
    public UserResponse findUserByUsername(String username) {
        try {
            User user = userRepository.findByName(username)
                    .orElseThrow(() -> new UserServiceException("User not found :: " + username));

            return Mapper.userToUserResponse(user);
        } catch (Exception exception) {
            log.error("UserService::findUserByUsername " + exception.getMessage());
            throw new UserServiceException(exception.getMessage());
        }
    }

    @Override
    public UserResponse saveUser(UserRequest userRequest) {
        try {
            if (userRepository.existsByName(userRequest.getUsername())) {
                throw new UserServiceException("User already exists");
            }

            if (userRepository.existsByEmail(userRequest.getEmail())) {
                throw new UserServiceException("User email already exists");
            }

            // Encrypt password
            userRequest.setUserPassword(passwordEncoder.encode(userRequest.getUserPassword()));

            // Map user
            User user = Mapper.userRequestToUser(userRequest);

            // Set user roles (if not provided, assign USER_ROLE by default)
            user.setRoles(getRoles(userRequest));

            // Creating secret keys
            KeyPair keyPair = secretKeyService.generateSecretKeys();
            String privateKey = secretKeyService.encode(keyPair.getPrivate().getEncoded());
            String publicKey = secretKeyService.encode(keyPair.getPublic().getEncoded());

            user.setPrivateKey(privateKey);
            user.setPublicKey(publicKey);

            return Mapper.userToUserResponse(userRepository.save(user));
        } catch (Exception exception) {
            log.error("UserService::saveUser " + exception.getMessage());
            throw new UserServiceException(exception.getMessage());
        }
    }

    private Set<Role> getRoles(UserRequest userRequest) {
        Set<RoleRequest> userRequestRoles = userRequest.getRoles();

        // Use default role USER_ROLE from roleRepository if no roles are provided
        if (userRequestRoles == null || userRequestRoles.isEmpty()) {
            Role defaultRole = roleRepository.findByRoleName(Roles.ROLE_USER.name());
            if (defaultRole == null) {
                throw new RoleServiceException("Default role ROLE_USER not found");
            }
            return Collections.singleton(defaultRole);
        }

        Set<Role> persistedRoles = new HashSet<>();
        userRequestRoles.forEach(role -> {
            Role r = roleRepository.findByRoleName(role.getRoleName());
            if (r == null) {
                throw new RoleServiceException("Role not found: " + role.getRoleName());
            }
            persistedRoles.add(r);
        });

        return persistedRoles;
    }
}