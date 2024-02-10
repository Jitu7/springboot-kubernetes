package com.example.bookmarkerapi.domain;

import jakarta.validation.constraints.NotEmpty;

public record CreateBookmarkRequest(
        @NotEmpty(message = "Tittle should not be empty") String title,
        @NotEmpty(message = "Url should not be empty") String url) {
}
