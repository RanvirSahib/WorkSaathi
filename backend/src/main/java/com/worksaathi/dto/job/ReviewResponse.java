package com.worksaathi.dto.job;

import com.worksaathi.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {

    private Long id;
    private Long jobId;
    private Long customerId;
    private String customerName;
    private Long workerId;
    private String workerName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;

    public static ReviewResponse fromEntity(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setJobId(review.getJob().getId());
        response.setCustomerId(review.getCustomer().getId());
        response.setCustomerName(review.getCustomer().getName());
        response.setWorkerId(review.getWorker().getId());
        response.setWorkerName(review.getWorker().getUser().getName());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setCreatedAt(review.getCreatedAt());
        return response;
    }
}
