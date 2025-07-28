package pl.myc22ka.mathapp.api.tests;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestDataLoader implements CommandLineRunner {

    private final TestRepository testRepository;

    public TestDataLoader(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        testRepository.save(Test.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .build());

        testRepository.save(Test.builder()
                .firstName("Anna")
                .lastName("Nowak")
                .build());
    }
}

