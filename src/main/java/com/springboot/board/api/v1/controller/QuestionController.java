package com.springboot.board.api.v1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import com.springboot.board.application.service.QuestionService;
import com.springboot.board.common.response.ApiResponse;
import com.springboot.board.common.response.ErrorResponse;
import com.springboot.board.api.v1.dto.request.QuestionCreateRequest;
import com.springboot.board.api.v1.dto.request.QuestionUpdateRequest;
import com.springboot.board.api.v1.dto.response.QuestionResponse;

import java.util.Map;

import org.springframework.data.domain.Page;
import jakarta.validation.Valid;

@Tag(name = "Question", description = "질문 관련 API")
@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionController {
        private final QuestionService questionService;

        // 으악..으악...으악....
        @Operation(summary = "질문 목록 조회", description = "페이지네이션된 질문 목록을 조회합니다.")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = QuestionResponse.class))),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @GetMapping
        public ApiResponse<Page<QuestionResponse>> getQuestions(
                        @RequestParam(defaultValue = "0") int page) {
                return ApiResponse.success(questionService.getQuestions(page));
        }

        @Operation(summary = "질문 생성", description = "새로운 질문을 생성합니다.")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = QuestionResponse.class))),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public ApiResponse<QuestionResponse> createQuestion(
                        @Valid @RequestBody QuestionCreateRequest request) {
                return ApiResponse.success(questionService.createQuestion(request));
        }

        @Operation(summary = "질문 상세 조회", description = "특정 ID의 질문을 조회합니다.")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = QuestionResponse.class))),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "질문 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @GetMapping("/{id}")
        public ApiResponse<QuestionResponse> getQuestion(@PathVariable Integer id) {
                return ApiResponse.success(questionService.getQuestion(id));
        }

        @Operation(summary = "질문 수정", description = "특정 ID의 질문을 수정합니다.")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = QuestionResponse.class))),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "질문 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @PutMapping("/{id}")
        public ApiResponse<QuestionResponse> updateQuestion(
                        @PathVariable Integer id,
                        @Valid @RequestBody QuestionUpdateRequest request) {
                return ApiResponse.success(questionService.updateQuestion(id, request));
        }

        @Operation(summary = "질문 삭제", description = "특정 ID의 질문을 삭제합니다.")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "삭제 성공"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "질문 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT) // 삭제 성공 시 No Content 상태 코드 반환
        public void deleteQuestion(@PathVariable Integer id) {
                questionService.deleteQuestion(id);
        }

        @Operation(summary = "오늘의 질문", description = "랜덤으로 오늘의 질문을 반환합니다.")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "랜덤 질문 반환 성공", content = @Content(schema = @Schema(implementation = QuestionResponse.class))),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "질문 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @GetMapping("/random")
        public ApiResponse<QuestionResponse> getRandomQuestion() {
                QuestionResponse randomQuestion = questionService.getRandomQuestion();
                return ApiResponse.success(randomQuestion);
        }

        // 비밀번호 확인 로직
        @Operation(summary = "비밀번호 확인", description = "질문의 비밀번호가 일치하는지 확인합니다.")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "비밀번호 확인 성공"),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "질문 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @PostMapping("/{id}/check-password")
        public ApiResponse<Boolean> checkPassword(
                        @PathVariable Integer id,
                        @RequestBody Map<String, String> request) {
                String inputPassword = request.get("password");
                boolean isMatch = questionService.checkPassword(id, inputPassword);
                return ApiResponse.success(isMatch);
        }

}