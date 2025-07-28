package pl.myc22ka.mathapp.api.tests;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
}
