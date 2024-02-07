package de.tekup.complaintsclaims.controller;

import de.tekup.complaintsclaims.dto.response.ApiResponse;
import de.tekup.complaintsclaims.entity.Complaint;
import de.tekup.complaintsclaims.service.impl.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/complaints")
@RequiredArgsConstructor
public class ComplaintController {
    public static final String SUCCESS = "SUCCESS";
    private final ComplaintService complaintService;

    @PostMapping
    public ResponseEntity<ApiResponse<Complaint>> createComplaint(@RequestBody Complaint complaint,
                                                                  @RequestHeader("Authorization") String token) {
        Complaint complaintResponse = complaintService.saveComplaint(complaint, token);
        ApiResponse<Complaint> response = ApiResponse
                .<Complaint>builder()
                .status(SUCCESS)
                .results(complaintResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
