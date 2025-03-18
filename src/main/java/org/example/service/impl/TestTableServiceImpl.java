package org.example.service.impl;

import org.example.entity.TestTable;
import org.example.mapper.TestTableMapper;
import org.example.service.TestTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestTableServiceImpl implements TestTableService {

    @Autowired
    private TestTableMapper testTableMapper;

    @Override
    public TestTable create(TestTable testTable) {
        testTableMapper.insert(testTable);
        return testTable;
    }

    @Override
    public TestTable getById(Integer id) {
        return testTableMapper.selectById(id);
    }

    @Override
    public List<TestTable> getAll() {
        return testTableMapper.selectAll();
    }

    @Override
    public boolean update(TestTable testTable) {
        return testTableMapper.update(testTable) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        return testTableMapper.delete(id) > 0;
    }
} 