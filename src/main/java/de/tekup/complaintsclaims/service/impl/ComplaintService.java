package de.tekup.complaintsclaims.service.impl;


import de.tekup.complaintsclaims.dto.response.ComplaintResponse;
import de.tekup.complaintsclaims.entity.Complaint;
import de.tekup.complaintsclaims.entity.User;
import de.tekup.complaintsclaims.enums.Roles;
import de.tekup.complaintsclaims.exception.ComplaintServiceException;
import de.tekup.complaintsclaims.repository.ComplaintRepository;
import de.tekup.complaintsclaims.service.SecretKeyService;
import de.tekup.complaintsclaims.util.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserServiceImpl userService;
    private final SecretKeyService secretKeyService;

    public ComplaintResponse saveComplaint(Complaint complaint, String token) {
        try {
            User user = userService.getUserFromToken(token);
            String content = complaint.getContent();
            String encryptedContent = secretKeyService.encrypt(content, user.getPublicKey());

            complaint.setUser(user);
            complaint.setContent(encryptedContent);

            Complaint savedComplaint = complaintRepository.save(complaint);

            return Mapper.complaintToComplaintResponse(savedComplaint);
        } catch (Exception e) {
            throw new ComplaintServiceException(e.getMessage());
        }
    }

    public List<ComplaintResponse> findAllComplaints() {
        return complaintRepository.findAll().stream().map(Mapper::complaintToComplaintResponse).toList();
    }

    public ComplaintResponse findComplaintById(Long complaintId, String token) {
        Complaint complaint = complaintRepository.findById(complaintId).orElseThrow(
                () -> new ComplaintServiceException("complaint not found"));

        User user = userService.getUserFromToken(token);
        boolean isAllowedToFetch = user.getRoles()
                .stream()
                .anyMatch(r -> r.getRoleName().equals(Roles.ROLE_ADMIN.name())) ||
                Objects.equals(complaint.getUser().getId(), user.getId());

        if (!isAllowedToFetch) {
            throw new ComplaintServiceException("You have no access to display this content with id => " + complaintId);
        }
        String privateKey = complaint.getUser().getPrivateKey();
        String decryptedContent = secretKeyService.decrypt(complaint.getContent(), privateKey);
        complaint.setContent(decryptedContent);

        return Mapper.complaintToComplaintResponse(complaint);
    }

    public List<ComplaintResponse> findComplaintsByUser(String username) {
        List<Complaint> userComplaints = complaintRepository.findComplaintsByUserName(username);

        return userComplaints.stream()
                .map(Mapper::complaintToComplaintResponse)
                .toList();
    }
}
