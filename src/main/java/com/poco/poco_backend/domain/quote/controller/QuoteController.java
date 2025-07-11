package com.poco.poco_backend.domain.quote.controller;

import com.poco.poco_backend.domain.quote.service.QuoteService;
import com.poco.poco_backend.global.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Quote", description = "명언 관련 API by 박현빈")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quotes")
public class QuoteController {

    private final QuoteService quoteService;

    @Operation(summary = "오늘의 명언 조회")
    @GetMapping("/today")
    public CustomResponse<String> getTodayQuote() {
        return CustomResponse.onSuccess(quoteService.getRandomQuote());
    }
}
