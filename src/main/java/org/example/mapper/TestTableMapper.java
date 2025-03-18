package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.entity.TestTable;
import java.util.List;

@Mapper
public interface TestTableMapper {
    int insert(TestTable testTable);
    
    TestTable selectById(Integer id);
    
    List<TestTable> selectAll();
    
    int update(TestTable testTable);
    
    int delete(Integer id);
} 