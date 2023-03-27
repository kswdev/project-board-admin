package com.boardadmin.project.service;

import com.boardadmin.project.dto.ArticleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleManagementService {

    public List<ArticleDto> getArticles() {
        return List.of();
    }

    public ArticleDto getArticle(Long articleId) {
        return null;
    }

    public void deleteArticle(Long articleId) {

    }
}
