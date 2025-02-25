package ex2.ex2;

import ex2.ex2.entitly.TestTable;
import ex2.ex2.repository.TestTableRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestTableService {

    private final TestTableRepository testTableRepository;

    public TestTableService(TestTableRepository testTableRepository) {
        this.testTableRepository = testTableRepository;
    }

    // 모든 데이터 조회
    public List<TestTable> getAllTestTables() {
        return testTableRepository.findAll();
    }
}
