package com.kozitskiy.minispring;

import com.kozitskiy.minispring.annotations.Autowired;
import com.kozitskiy.minispring.annotations.Component;
import com.kozitskiy.minispring.annotations.Scope;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MiniApplicationContext {
    //Хранилище singleton-бинов
    private final Map<Class<?>, Object> singletonBeans = new ConcurrentHashMap<>();

    // Список всех классов с @Component (нужен для prototype и поиска)
    private final Set<Class<?>> componentClasses;

    //В Spring аналог - new AnnotationConfigApplicationContext("com.example")
    public MiniApplicationContext(String packageName){
       ClassPathScanner scanner = new ClassPathScanner();
       Set<Class<?>> allClasses = scanner.scan(packageName);

       //Фильтруем только @Component
        this.componentClasses = allClasses.stream()
                .filter(cls -> cls.isAnnotationPresent(Component.class))
                .collect(Collectors.toSet());

        instantiateSingletons();
    }

    private void instantiateSingletons() {
        for (Class<?> cls : componentClasses){
            if (isPrototype(cls)) continue;
            try {
                Object instance = cls.getDeclaredConstructor().newInstance();
                singletonBeans.put(cls, instance); // Кладем до внедрения зависимостей
            } catch (Exception e){
                throw new RuntimeException("Cannot instantiate " + cls, e);
            }
        }
        // Внедряем зависимости и инициализируем
        for (Class<?> cls : componentClasses){
            if (isPrototype(cls)) continue;
            Object bean = singletonBeans.get(cls);
            injectDependencies(bean);
            invokeAfterPropertiesSet(bean);
        }
    }

    private void invokeAfterPropertiesSet(Object bean) {
        if (bean instanceof InitializingBean){
            try {
                ((InitializingBean) bean).afterPropertiesSet();
            }catch (Exception e){
                throw new RuntimeException("Initialization failed for: " + bean.getClass(), e);
            }
        }
    }

    private void injectDependencies(Object bean) {
        Class<?> clazz = bean.getClass();
        for (Field field : clazz.getDeclaredFields()){
            if (field.isAnnotationPresent(Autowired.class)){
                Class<?> fieldType = field.getType();
                Object dependency = getBean(fieldType); //Рекурсивный выход
                try {
                    //для рефлексии, тк как private
                    field.setAccessible(true);
                    field.set(bean, dependency);
                }catch (IllegalAccessException e){
                    throw new RuntimeException("DI failed on field " + field, e);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public  <T> T getBean(Class<?> type) {

        // Singleton возвращаем из кэша
        if(singletonBeans.containsKey(type)){
            return (T) singletonBeans.get(type);
        }

        for (Class<?> cls : componentClasses){
            if (cls.equals(type) && isPrototype(cls)){
                try {
                    Object instance = cls.getDeclaredConstructor().newInstance();
                    injectDependencies(instance);
                    invokeAfterPropertiesSet(instance);
                    return (T) instance;
                }catch (Exception e){
                    throw new RuntimeException("Cannot create prototype " + type, e);
                }
            }
        }
        throw new RuntimeException("No bean of type " + type + " found");
    }

    private boolean isPrototype(Class<?> cls) {
        Scope scope = cls.getAnnotation(Scope.class);
        return scope != null && "prototype".equals(scope.value());
    }
}