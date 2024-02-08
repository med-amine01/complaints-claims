package de.tekup.complaintsclaims.repository;

import de.tekup.complaintsclaims.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findComplaintsByUserName(String username);
}
