package de.tekup.complaintsclaims.service.impl;


import de.tekup.complaintsclaims.entity.Complaint;
import de.tekup.complaintsclaims.entity.User;
import de.tekup.complaintsclaims.exception.ComplaintServiceException;
import de.tekup.complaintsclaims.repository.ComplaintRepository;
import de.tekup.complaintsclaims.service.SecretKeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserServiceImpl userService;
    private final SecretKeyService secretKeyService;

    public Complaint saveComplaint(Complaint complaint, String token) {
        try {
            User user = userService.getUserFromToken(token);

            String encryptedContent = secretKeyService.encrypt(complaint.getContent(), user.getPublicKey());
            complaint.setUser(user);
            complaint.setContent(encryptedContent);

            return complaintRepository.save(complaint);
        } catch (Exception e) {
            throw new ComplaintServiceException(e.getMessage());
        }
    }
}
