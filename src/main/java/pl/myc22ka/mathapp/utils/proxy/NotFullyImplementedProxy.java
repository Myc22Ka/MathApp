package pl.myc22ka.mathapp.utils.proxy;

import pl.myc22ka.mathapp.utils.annotations.NotFullyImplemented;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class NotFullyImplementedProxy implements InvocationHandler {
    private final Object target;

    private NotFullyImplementedProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        NotFullyImplemented annotation = method.getAnnotation(NotFullyImplemented.class);

        if (annotation != null) {
            System.err.println("Warning: " + annotation.message() + " - Method: " + method.getName());

            return getDefaultValue(method.getReturnType());
        }

        return method.invoke(target, args);
    }

    private static Object getDefaultValue(Class<?> type) {
        if (type == boolean.class) return false;
        if (type == char.class) return '\u0000';
        if (type == byte.class) return (byte) 0;
        if (type == short.class) return (short) 0;
        if (type == int.class) return 0;
        if (type == long.class) return 0L;
        if (type == float.class) return 0.0f;
        if (type == double.class) return 0.0d;
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T target, Class<T> interfaceType) {
        return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class<?>[] { interfaceType },
                new NotFullyImplementedProxy(target)
        );
    }
}