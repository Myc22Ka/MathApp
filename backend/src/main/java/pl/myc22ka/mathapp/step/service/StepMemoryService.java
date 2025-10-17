package pl.myc22ka.mathapp.step.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import pl.myc22ka.mathapp.utils.resolver.dto.ContextRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for storing and managing temporary step execution context.
 * Uses a {@link ThreadLocal} map to keep a separate memory per thread.
 *
 * @author Myc22Ka
 * @version 1.0.0
 * @since 17.10.2025
 */
@Service
public class StepMemoryService {

    private final ThreadLocal<Map<String, ContextRecord>> programMemory = ThreadLocal.withInitial(HashMap::new);

    /**
     * Returns the current thread's memory map.
     *
     * @return the memory map
     */
    public Map<String, ContextRecord> getMemory() {
        return programMemory.get();
    }


    /**
     * Stores a single context record in the memory.
     *
     * @param key   the memory key
     * @param value the context record
     */
    public void put(String key, ContextRecord value) {
        programMemory.get().put(key, value);
    }

    /**
     * Stores a list of context records in the memory.
     *
     * @param values the list of context records
     */
    public void putAll(@NotNull List<ContextRecord> values) {
        for (ContextRecord v : values) {
            put(v.key().templateString(), v);
        }
    }

    /**
     * Clears the current thread's memory.
     */
    public void clear() {
        programMemory.remove();
    }
}
