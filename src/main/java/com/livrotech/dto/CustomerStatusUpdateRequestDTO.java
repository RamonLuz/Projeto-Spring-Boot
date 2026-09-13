package com.livrotech.dto;

import com.livrotech.entity.Status;
import jakarta.validation.constraints.NotNull;

public record CustomerStatusUpdateRequestDTO(
        @NotNull(message = "Status is required") Status status
) {
}
