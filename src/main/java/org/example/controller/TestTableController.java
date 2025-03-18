package org.example.controller;

import org.example.entity.TestTable;
import org.example.service.TestTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestTableController {

    @Autowired
    private TestTableService testTableService;

    @PostMapping
    public ResponseEntity<TestTable> createTestTable(@RequestBody TestTable testTable) {
        return ResponseEntity.ok(testTableService.create(testTable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestTable> getTestTableById(@PathVariable Integer id) {
        TestTable testTable = testTableService.getById(id);
        return testTable != null ? ResponseEntity.ok(testTable) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<TestTable>> getAllTestTables() {
        return ResponseEntity.ok(testTableService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTestTable(@PathVariable Integer id, @RequestBody TestTable testTable) {
        testTable.setId(id);
        return testTableService.update(testTable) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestTable(@PathVariable Integer id) {
        return testTableService.delete(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
} 