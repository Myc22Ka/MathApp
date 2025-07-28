package pl.myc22ka.mathapp.api.tests;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TestService {

    private final TestRepository testRepository;

    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public List<Test> getAllTests() {
        return testRepository.findAll();
    }
}
