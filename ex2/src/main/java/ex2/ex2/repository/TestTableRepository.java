package ex2.ex2.repository;

import ex2.ex2.entitly.TestTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestTableRepository extends JpaRepository<TestTable, Integer> {

}
