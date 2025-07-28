package pl.myc22ka.mathapp.api.tests;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/tests")
    public List<Test> getTests() {
        return testService.getAllTests();
    }
}
