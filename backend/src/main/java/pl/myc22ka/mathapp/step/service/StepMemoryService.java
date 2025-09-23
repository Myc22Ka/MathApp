package pl.myc22ka.mathapp.step.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import pl.myc22ka.mathapp.ai.prompt.dto.PrefixValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StepMemoryService {

    private final ThreadLocal<Map<String, PrefixValue>> programMemory = ThreadLocal.withInitial(HashMap::new);

    public Map<String, PrefixValue> getMemory() {
        return programMemory.get();
    }

    public void put(String key, PrefixValue value) {
        programMemory.get().put(key, value);
    }

    public void putAll(@NotNull List<PrefixValue> values) {
        for (PrefixValue v : values) {
            put(v.key(), v);
        }
    }

    public void clear() {
        programMemory.remove();
    }
}
