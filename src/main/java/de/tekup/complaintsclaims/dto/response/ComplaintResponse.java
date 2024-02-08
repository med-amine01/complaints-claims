package de.tekup.complaintsclaims.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintResponse {
    private String username;
    private String userEmail;
    private String complaintContent;
    private String createdAt;
    private String updatedAt;
}
