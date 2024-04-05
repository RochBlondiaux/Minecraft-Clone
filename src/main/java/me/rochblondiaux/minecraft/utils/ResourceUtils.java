package me.rochblondiaux.minecraft.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ResourceUtils {

    public static String readResource(String path) {
        try (InputStream inputStream = ResourceUtils.class.getClassLoader().getResourceAsStream(path)) {
            if (inputStream == null)
                throw new IllegalStateException("Failed to read resource: " + path);

            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read resource: " + path, e);
        }
    }
}
