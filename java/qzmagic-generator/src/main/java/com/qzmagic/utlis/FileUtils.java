package com.qzmagic.utlis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileUtils {
    private FileUtils(){}


    public static String readFileFromClasspath(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        // 使用类加载器获取资源的输入流
        try (InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(filePath);
             InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }
}
