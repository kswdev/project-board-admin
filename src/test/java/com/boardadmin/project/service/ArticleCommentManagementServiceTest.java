package com.boardadmin.project.service;

import com.boardadmin.project.domain.constant.RoleType;
import com.boardadmin.project.dto.ArticleCommentDto;
import com.boardadmin.project.dto.ArticleDto;
import com.boardadmin.project.dto.UserAccountDto;
import com.boardadmin.project.dto.properties.ProjectProperties;
import com.boardadmin.project.dto.response.ArticleCommentClientResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class ArticleCommentManagementServiceTest {

    @Disabled("실제 API 호출 결과 관찰용")
    @DisplayName("실제 API 호출 테스트")
    @SpringBootTest
    @Nested
    class RealApiTest {
        private final ArticleCommentManagementService sut;

        @Autowired
        public RealApiTest(ArticleCommentManagementService sut) {
            this.sut = sut;
        }

        @DisplayName("게시글 API를 호출하면, 게시글을 가져온다.")
        @Test
        void given_when_then() {
            // Given

            // When
            List<ArticleCommentDto> result = sut.getArticleComments();

            // Then
            System.out.println(result.stream().findFirst());
            assertThat(result).isNotNull();
        }
    }


    @DisplayName("API mocking 테스트")
    @EnableConfigurationProperties(ProjectProperties.class)
    @AutoConfigureWebClient(registerRestTemplate = true)
    @RestClientTest(ArticleCommentManagementService.class)
    @Nested
    class RestTemplateTest {

        private final ArticleCommentManagementService sut;
        private final ProjectProperties projectProperties;
        private final MockRestServiceServer server;
        private final ObjectMapper objectMapper;

        @Autowired
        public RestTemplateTest(
                ArticleCommentManagementService sut,
                ProjectProperties projectProperties,
                MockRestServiceServer server,
                ObjectMapper objectMapper
        ) {
            this.sut = sut;
            this.projectProperties = projectProperties;
            this.server = server;
            this.objectMapper = objectMapper;
        }

        @DisplayName("게시글 목록 API를 호출하면, 게시글을 가져온다.")
        void givenNothing_whenCallingArticlesApi_thenReturnsArticleList() throws JsonProcessingException {
            // Given
            ArticleCommentDto expectedArticle = createArticleCommentDto("글");
            ArticleCommentClientResponse expectedResponse = ArticleCommentClientResponse.of(List.of(expectedArticle));
            server
                    .expect(requestTo(projectProperties.board().url() + "/api/articles?size=10000"))
                    .andRespond(withSuccess(
                            objectMapper.writeValueAsString(expectedResponse),
                            MediaType.APPLICATION_JSON
                    ));
            // When
            List<ArticleCommentDto> result = sut.getArticleComments();
            // Then
            assertThat(result).first()
                    .hasFieldOrPropertyWithValue("id", expectedArticle.id())
                    .hasFieldOrPropertyWithValue("content", expectedArticle.content())
                    .hasFieldOrPropertyWithValue("userAccount.nickname", expectedArticle.userAccountDto().nickname());
            server.verify();
        }

        @DisplayName("게시글 목록 API를 호출하면, 게시글을 가져온다.")
        void givenArticldId_whenCallingArticlesApi_thenReturnsArticle() throws JsonProcessingException {
            // Given
            Long articleId = 1L;
            ArticleCommentDto expectedArticle = createArticleCommentDto("글");
            server
                    .expect(requestTo(projectProperties.board().url() + "/api/articles/" + articleId))
                    .andRespond(withSuccess(
                            objectMapper.writeValueAsString(expectedArticle),
                            MediaType.APPLICATION_JSON
                    ));
            // When
            List<ArticleCommentDto> result = sut.getArticleComments();
            // Then
            assertThat(result).first()
                    .hasFieldOrPropertyWithValue("id", expectedArticle.id())
                    .hasFieldOrPropertyWithValue("content", expectedArticle.content())
                    .hasFieldOrPropertyWithValue("userAccount.nickname", expectedArticle.userAccountDto().nickname());
            server.verify();
        }

        @DisplayName("게시글 ID와 함께 게시글 삭제 API를 호출하면, 게시글을 삭제한다.")
        @Test
        void givenArticleId_whenCallingDeleteArticleApi_thenDeleteAnArticle() {
            // Given
            Long articleId = 1L;
            server
                    .expect(requestTo(projectProperties.board().url() + "api/articles/" + articleId))
                    .andExpect(method(HttpMethod.DELETE))
                    .andRespond(withSuccess());
            // When
            sut.deleteArticleComment(articleId);

            // Then
            server.verify();
        }

        private ArticleCommentDto createArticleCommentDto(String content) {
            return ArticleCommentDto.of(
                    1L,
                    1L,
                    createUserAccountDto(),
                    null,
                    content,
                    LocalDateTime.now(),
                    "Uno",
                    LocalDateTime.now(),
                    "Uno"
            );
        }

        private UserAccountDto createUserAccountDto() {
            return UserAccountDto.of(
                    "unoTest",
                    "uno-test@email.com",
                    "uno-test",
                    "test memo"
            );
        }
    }
}