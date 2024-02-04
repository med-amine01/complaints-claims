package de.tekup.complaintsclaims.service;

import de.tekup.complaintsclaims.entity.Authority;

import java.util.List;
import java.util.Set;

public interface AuthorityService {

    List<String> findNonExistentAuthorities(Set<String> authorityNames);

    Authority createDefaultAuthority();
}