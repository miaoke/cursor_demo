package org.example.service;

import org.example.entity.TestTable;
import java.util.List;

public interface TestTableService {
    TestTable create(TestTable testTable);
    TestTable getById(Integer id);
    List<TestTable> getAll();
    boolean update(TestTable testTable);
    boolean delete(Integer id);
} 