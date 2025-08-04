package pl.myc22ka.mathapp.ai.prompt.handler;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pl.myc22ka.mathapp.model.expression.TemplatePrefix.SET;

@Component
public class TemplateResolver {

    private static final Pattern TEMPLATE_PATTERN = Pattern.compile("\\$\\{([a-zA-Z]+):}");

    // każdy resolver dostaje prefix (np. "s") i mapę danych kontekstowych
    private final Map<String, BiFunction<String, Map<String, Object>, String>> resolvers = new HashMap<>();

    public TemplateResolver() {
        resolvers.put(SET.getKey(), (key, ctx) -> {
            Object val = ctx.get(SET.getKey());
            return val != null ? val.toString() : "X";
        });

//        resolvers.put("f", (key, ctx) -> ctx.getOrDefault("f", "X").toString());
//        resolvers.put("a", (key, ctx) -> ctx.getOrDefault("a", "X").toString());
    }

    public String resolve(String input, Map<String, Object> context) {
        Matcher matcher = TEMPLATE_PATTERN.matcher(input);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String key = matcher.group(1);
            BiFunction<String, Map<String, Object>, String> resolver = resolvers.get(key);
            String replacement = (resolver != null)
                    ? resolver.apply(key, context)
                    : matcher.group(0); // brak resolvera – zostaw ${s:}
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(sb);
        return sb.toString();
    }
}