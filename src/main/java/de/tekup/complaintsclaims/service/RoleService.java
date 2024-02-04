package de.tekup.complaintsclaims.service;

import de.tekup.complaintsclaims.dto.request.RoleRequest;
import de.tekup.complaintsclaims.dto.response.RoleResponse;
import de.tekup.complaintsclaims.exception.RoleServiceException;

import java.util.List;

public interface RoleService {
    RoleResponse saveRole(RoleRequest role) throws RoleServiceException;

    List<RoleResponse> findRoles();
}