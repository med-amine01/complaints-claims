package de.tekup.complaintsclaims.service.impl;

import de.tekup.complaintsclaims.dto.request.RoleRequest;
import de.tekup.complaintsclaims.dto.response.RoleResponse;
import de.tekup.complaintsclaims.entity.Authority;
import de.tekup.complaintsclaims.entity.Role;
import de.tekup.complaintsclaims.exception.AuthorityServiceException;
import de.tekup.complaintsclaims.exception.RoleServiceException;
import de.tekup.complaintsclaims.repository.RoleRepository;
import de.tekup.complaintsclaims.service.AuthorityService;
import de.tekup.complaintsclaims.service.RoleService;
import de.tekup.complaintsclaims.util.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final AuthorityService authorityService;

    @Override
    public RoleResponse saveRole(RoleRequest roleRequest) throws RoleServiceException {
        try {
            if (roleRepository.existsByRoleName(roleRequest.getRoleName())) {
                throw new RoleServiceException("Role already exists");
            }

            Collection<Authority> authorities = roleRequest.getAuthorities();

            if (authorities == null || authorities.isEmpty()) {
                // Handle default authority case
                roleRequest.setAuthorities(Collections.singletonList(authorityService.createDefaultAuthority()));
            } else {
                Set<String> authorityNames = authorities.stream()
                        .map(Authority::getName)
                        .collect(Collectors.toSet());

                // Check for the existence of all authorities using AuthorityService
                List<String> nonExistentAuthorities = authorityService.findNonExistentAuthorities(authorityNames);
                if (!nonExistentAuthorities.isEmpty()) {
                    throw new AuthorityServiceException("Authorities not found: " + nonExistentAuthorities);
                }
            }

            return Mapper.roleToRoleResponse(roleRepository.save(Mapper.roleRequestToRole(roleRequest)));
        } catch (RoleServiceException exception) {
            log.error("RoleService::saveRole, " + exception.getMessage());
            throw exception;
        } catch (Exception exception) {
            log.error("RoleService::saveRole, " + exception.getMessage());
            throw new RoleServiceException(exception.getMessage());
        }
    }

    @Override
    public List<RoleResponse> findRoles() {
        try {
            return roleRepository
                    .findAll()
                    .stream()
                    .sorted(Comparator.comparingLong(Role::getId).reversed())
                    .map(Mapper::roleToRoleResponse)
                    .toList();

        } catch (Exception exception) {
            log.error("RoleService::findRoles, " + exception.getMessage());
            throw new RoleServiceException(exception.getMessage());
        }
    }

}