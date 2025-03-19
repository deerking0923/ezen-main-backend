package com.springboot.board.api.v1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import com.springboot.board.application.service.AnswerService;
import com.springboot.board.common.response.ApiResponse;
import com.springboot.board.common.response.ErrorResponse;
import com.springboot.board.api.v1.dto.request.AnswerCreateRequest;
import com.springboot.board.api.v1.dto.request.AnswerUpdateRequest;
import com.springboot.board.api.v1.dto.response.AnswerResponse;
import jakarta.validation.Valid;

@Tag(name = "Answer", description = "답변 관련 API")
@RestController
@RequestMapping("/api/v1/answers")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @Operation(summary = "답변 생성", description = "새로운 답변을 생성합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.
            ApiResponse(responseCode = "201", description = "생성 성공",
                content = @Content(schema = @Schema(implementation = AnswerResponse.class))),
        @io.swagger.v3.oas.annotations.responses.
            ApiResponse(responseCode = "400", description = "잘못된 요청",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @io.swagger.v3.oas.annotations.responses.
            ApiResponse(responseCode = "404", description = "질문 없음",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AnswerResponse> createAnswer(@Valid @RequestBody AnswerCreateRequest request) {
        return ApiResponse.success(answerService.createAnswer(request));
    }

    @Operation(summary = "답변 수정", description = "특정 ID의 답변을 수정합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.
            ApiResponse(responseCode = "200", description = "수정 성공",
                content = @Content(schema = @Schema(implementation = AnswerResponse.class))),
        @io.swagger.v3.oas.annotations.responses.
            ApiResponse(responseCode = "400", description = "잘못된 요청",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @io.swagger.v3.oas.annotations.responses.
            ApiResponse(responseCode = "404", description = "답변 없음",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ApiResponse<AnswerResponse> updateAnswer(@PathVariable Long id, @Valid @RequestBody AnswerUpdateRequest request) {
        return ApiResponse.success(answerService.updateAnswer(id, request));
    }

    @Operation(summary = "답변 삭제", description = "특정 ID의 답변을 삭제합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.
            ApiResponse(responseCode = "204", description = "삭제 성공"),
        @io.swagger.v3.oas.annotations.responses.
            ApiResponse(responseCode = "404", description = "답변 없음",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAnswer(@PathVariable Long id) {
        answerService.deleteAnswer(id);
    }
}
