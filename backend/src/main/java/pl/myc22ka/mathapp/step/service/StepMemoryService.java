package pl.myc22ka.mathapp.step.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import pl.myc22ka.mathapp.ai.prompt.dto.ContextRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StepMemoryService {

    private final ThreadLocal<Map<String, ContextRecord>> programMemory = ThreadLocal.withInitial(HashMap::new);

    public Map<String, ContextRecord> getMemory() {
        return programMemory.get();
    }

    public void put(String key, ContextRecord value) {
        programMemory.get().put(key, value);
    }

    public void putAll(@NotNull List<ContextRecord> values) {
        for (ContextRecord v : values) {
            put(v.key().templateString(), v);
        }
    }

    public void clear() {
        programMemory.remove();
    }
}
