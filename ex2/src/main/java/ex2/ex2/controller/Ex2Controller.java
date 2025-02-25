package ex2.ex2.controller;


import ex2.ex2.TestTableService;
import ex2.ex2.entitly.TestTable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class Ex2Controller {

    private final TestTableService testTableService;

    public Ex2Controller(TestTableService testTableService) {
        this.testTableService = testTableService;
    }

    @GetMapping("/testTable")
    public List<TestTable> getTestTable() {
        return testTableService.getAllTestTables();
    }
}
