package com.boardadmin.project.dto.response;

import com.boardadmin.project.dto.ArticleDto;
import com.boardadmin.project.dto.UserAccountDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ArticleResponse(
        Long id,
        UserAccountDto userAccount,
        String title,
        String content,
        LocalDateTime createdAt
) {

    public static ArticleResponse of(Long id, UserAccountDto userAccount, String title, String content, LocalDateTime createdAt) {
        return new ArticleResponse(id, userAccount, title, content, createdAt);
    }

    public static ArticleResponse withContent(ArticleDto dto) {
        return ArticleResponse.of(dto.id(), dto.userAccountDto(), dto.title(), dto.content(), dto.createdAt());
    }

    public static ArticleResponse withoutContent(ArticleDto dto) {
        return ArticleResponse.of(dto.id(), dto.userAccountDto(), dto.title(), null, dto.createdAt());
    }

}