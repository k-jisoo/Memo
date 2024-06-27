package com.sparta.memo;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMemo() throws Exception {
        MemoRequestDto requestDto = new MemoRequestDto("user1", "This is a test memo");

        String jsonRequest = "{ \"username\": \"user1\", \"contents\": \"This is a test memo\" }";

        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        mockMvc.perform(post("/api/memos")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.contents").value("This is a test memo"));
    }

    @Test
    void testGetMemos() throws Exception {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(List.of(
                new MemoResponseDto(1L, "user1", "memo1"),
                new MemoResponseDto(2L, "user2", "memo2")
        ));

        mockMvc.perform(get("/api/memos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[0].contents").value("memo1"))
                .andExpect(jsonPath("$[1].username").value("user2"))
                .andExpect(jsonPath("$[1].contents").value("memo2"));
    }

    @Test
    void testUpdateMemo() throws Exception {
        MemoRequestDto requestDto = new MemoRequestDto("user1", "Updated memo");

        String jsonRequest = "{ \"username\": \"user1\", \"contents\": \"Updated memo\" }";

        when(jdbcTemplate.update(anyString(), any(), any(), anyLong())).thenReturn(1);

        mockMvc.perform(put("/api/memos/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    void testDeleteMemo() throws Exception {
        when(jdbcTemplate.update(anyString(), anyLong())).thenReturn(1);

        mockMvc.perform(delete("/api/memos/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }
}