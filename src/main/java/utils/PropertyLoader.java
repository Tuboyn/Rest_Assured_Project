package utils;

import annotations.PropertyValue;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

public class PropertyLoader {
    public static void loadProperties(Object testObject) {
        Class<?> testClass = testObject.getClass();
        Properties properties = new Properties();

        try {
            FileInputStream fileInputStream = new FileInputStream("src/test/resources/property.property");
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        for (Field field : testClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(PropertyValue.class)) {
                PropertyValue annotation = field.getAnnotation(PropertyValue.class);
                String propertyValue = properties.getProperty(annotation.value());

                if (propertyValue != null) {
                    field.setAccessible(true);
                    try {
                        field.set(testObject, propertyValue);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}