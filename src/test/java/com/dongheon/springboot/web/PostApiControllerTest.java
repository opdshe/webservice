package com.dongheon.springboot.web;

import com.dongheon.springboot.domain.posts.Post;
import com.dongheon.springboot.domain.posts.PostRepository;
import com.dongheon.springboot.web.dto.PostSaveRequestsDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostApiControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostRepository postRepository;

    @After
    public void tearDown(){
        postRepository.deleteAll();
    }

    @Test
    public void Post_등록된다() throws Exception{
        //given
        String title = "string";
        String content = "content";
        PostSaveRequestsDto requestsDto = PostSaveRequestsDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();
        String url = "http://localhost:" + port +"/api/v1/post";

        //when
        ResponseEntity<Long> responseEntity = restTemplate
                .postForEntity(url, requestsDto, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Post> all = postRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
    }
}