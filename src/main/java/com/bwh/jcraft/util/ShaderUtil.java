package com.bwh.jcraft.util;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class ShaderUtil {
    private ShaderUtil() {}

    public static String readFile(String name) {
        StringBuilder source = new StringBuilder();
        try {
            var reader = new BufferedReader(
                    new InputStreamReader(
                            ShaderUtil.class
                                    .getClassLoader()
                                    .getResourceAsStream(name)));

            String line;
            while ((line = reader.readLine()) != null) {
                source.append(line).append("\n");
            }

            reader.close();
        }
        catch (Exception e) {
            System.err.println("Error loading source code: " + name);
            e.printStackTrace();
        }

        return source.toString();
    }
}
