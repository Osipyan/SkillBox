import main.Main;
import main.api.request.PostRequest;
import main.repository.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {Main.class})
@Sql(value = {"/createUser.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/_createUser.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MockMvcIntegrationTest extends AbstractMockMvcRestTestWithGeneratedData{
    private static final String DEFAULT = "/api/post";

    @Autowired
    private PostRepository postRepository;

    @AfterEach
    public void resetDb() {
        postRepository.deleteAll();
    }

    @Test
    @WithUserDetails(value = "user@yandex.ru")
    public void create() throws Exception {
        PostRequest post = new PostRequest()
                .setTimestamp(new Date().getTime() / 1000)
                .setActive(false)
                .setTags(new String[]{"String"})
                .setText("Текст поста для теста........................................................")
                .setTitle("Заголовок поста для теста");
        RequestBuilder requestBuilder = post(DEFAULT)
                .content(objectMapper.writeValueAsString(post))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true));
    }
}
