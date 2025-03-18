package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entity.TestTable;
import org.example.service.TestTableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TestTable控制器测试类
 */
@ExtendWith(MockitoExtension.class)
public class TestTableControllerTest {

    @Mock
    private TestTableService testTableService;

    @InjectMocks
    private TestTableController testTableController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(testTableController).build();
    }

    @Test
    public void testCreateTestTable() throws Exception {
        // 准备测试数据
        TestTable testTable = new TestTable();
        testTable.setName("测试名称");

        // 配置Mock行为
        when(testTableService.create(any(TestTable.class))).thenReturn(testTable);

        // 执行测试
        mockMvc.perform(post("/api/test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTable)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("测试名称"));
    }

    @Test
    public void testGetTestTableById() throws Exception {
        // 准备测试数据
        TestTable testTable = new TestTable();
        testTable.setId(1);
        testTable.setName("测试名称");

        // 配置Mock行为
        when(testTableService.getById(1)).thenReturn(testTable);

        // 执行测试
        mockMvc.perform(get("/api/test/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("测试名称"));
    }

    @Test
    public void testGetAllTestTables() throws Exception {
        // 准备测试数据
        TestTable testTable1 = new TestTable();
        testTable1.setId(1);
        testTable1.setName("测试名称1");

        TestTable testTable2 = new TestTable();
        testTable2.setId(2);
        testTable2.setName("测试名称2");

        List<TestTable> testTables = Arrays.asList(testTable1, testTable2);

        // 配置Mock行为
        when(testTableService.getAll()).thenReturn(testTables);

        // 执行测试
        mockMvc.perform(get("/api/test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("测试名称1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("测试名称2"));
    }

    @Test
    public void testUpdateTestTable() throws Exception {
        // 准备测试数据
        TestTable testTable = new TestTable();
        testTable.setId(1);
        testTable.setName("更新的名称");

        // 配置Mock行为
        when(testTableService.update(any(TestTable.class))).thenReturn(true);

        // 执行测试
        mockMvc.perform(put("/api/test/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTable)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteTestTable() throws Exception {
        // 配置Mock行为
        when(testTableService.delete(1)).thenReturn(true);

        // 执行测试
        mockMvc.perform(delete("/api/test/1"))
                .andExpect(status().isOk());
    }
} 