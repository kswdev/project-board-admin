package com.boardadmin.project.service;

import com.boardadmin.project.dto.ArticleDto;
import com.boardadmin.project.dto.properties.ProjectProperties;
import com.boardadmin.project.dto.response.ArticleClientResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleManagementService {

    private final RestTemplate restTemplate;
    private final ProjectProperties projectProperties;
    public List<ArticleDto> getArticles() {
        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.board().url() + "/api/articles")
                .queryParam("size", 10000) //TODO 모든 게시글을 가져오기에는 불안정하다
                .build()
                .toUri();

        ArticleClientResponse response = restTemplate.getForObject(uri, ArticleClientResponse.class);
        log.info("article : {}", response);
        return Optional.ofNullable(response)
                .orElseGet(ArticleClientResponse::empty).articles();
    }

    public ArticleDto getArticle(Long articleId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.board().url() + "/api/articles/" + articleId)
                .build()
                .toUri();

        ArticleDto response = restTemplate.getForObject(uri, ArticleDto.class);
        return Optional.ofNullable(response)
                .orElseThrow(() -> new NoSuchElementException("게시글이 없습니다"));
    }

    public void deleteArticle(Long articleId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.board().url() + "/api/articles/" + articleId)
                .build()
                .toUri();
        restTemplate.delete(uri);
    }
}
