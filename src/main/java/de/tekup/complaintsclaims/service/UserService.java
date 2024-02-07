package de.tekup.complaintsclaims.service;

import de.tekup.complaintsclaims.dto.request.UserRequest;
import de.tekup.complaintsclaims.dto.response.UserResponse;
import de.tekup.complaintsclaims.entity.User;

import java.util.List;

public interface UserService {

    List<UserResponse> findUsers();

    UserResponse saveUser(UserRequest userRequest);

    UserResponse findUserByUsername(String username);

    User getUserFromToken(String token);
}
