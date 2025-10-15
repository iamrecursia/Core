package com.kozitskiy.minispring;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

// Вызываем scan("com.example")
// Контейнер ищет директорию com/example в classpath
// Находит её в target/classes
// Рекурсивно обходит все подпапки (service, repository и тд)
// Для каждого .class-файла формирует полное имя класса
// Загружает класс через Class.forName()
// Возвращает множество Class<?> - теперь контейнер может проверить, есть ли у них аннотация @Component

public class ClassPathScanner {
    public Set<Class<?>> scan(String packagePath){
        //для ClassLoader тк как он работает с путями в формате файловой системы
        String path = packagePath.replace('.','/');
        // Объект, который знает, где находятся скомпилированные .class (target/class)
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Set<Class<?>> classes = new HashSet<>();

        try {
            Enumeration<URL> resources = loader.getResources(path);
            //Походит по каждому найденному ресурсу: file:/project/target/classes/com/example
            while (resources.hasMoreElements()){
                URL resource = resources.nextElement();
                // Намеренно игнорирую JAR-файлы
                if ("file".equals(resource.getProtocol())){
                    //getFile возвращает закодированный путь, но для простых имен окей
                    File dir = new File(resource.getFile());
                    if(dir.exists() && dir.isDirectory()) {
                        scanDirectory(dir, packagePath, classes);
                    }
                }
            }
        }catch (IOException e){
            throw new RuntimeException("Failed to scan package: " + packagePath, e);
        }

        return classes;
    }

    private void scanDirectory(File dir, String packageName, Set<Class<?>> classes){
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files){
            if (file.isDirectory()){
                scanDirectory(file, packageName + "." + file.getName(), classes);
            } else if (file.getName().endsWith(".class")){
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                try {
                    // Загружаем класс в память с помощью Class.forName() - не создает экземпляр, только метаданные о классе
                    classes.add(Class.forName(className));
                }catch (ClassNotFoundException ignored){}
            }
        }
    }
}
