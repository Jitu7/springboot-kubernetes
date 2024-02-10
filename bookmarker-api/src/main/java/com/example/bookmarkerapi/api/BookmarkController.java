package com.example.bookmarkerapi.api;

import com.example.bookmarkerapi.domain.BookmarkDTO;
import com.example.bookmarkerapi.domain.BookmarkService;
import com.example.bookmarkerapi.domain.BookmarksDTO;
import com.example.bookmarkerapi.domain.CreateBookmarkRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService service;

    @GetMapping
    public BookmarksDTO getBookmarks(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                     @RequestParam(name = "query", defaultValue = "") String query) {
        if (query == null || query.trim().isEmpty()) {
            return service.getBookmarks(page);
        }
        return service.searchBookmarks(query, page);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookmarkDTO createBookmark(@RequestBody @Valid CreateBookmarkRequest request) {
        return service.createBookmark(request);
    }

}
