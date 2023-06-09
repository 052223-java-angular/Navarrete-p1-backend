package com.revature.movietn.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllReviewsRequest {
    @NotBlank(message = "UserId must not be empty.")
    private String userId;
    @NotBlank(message = "MovieId must not be empty.")
    private String movieId;
}