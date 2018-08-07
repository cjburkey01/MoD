package com.cjburkey.mod.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.system.MemoryUtil.*;

public final class IO {
    
    public static String readFileText(String file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(MoD.class.getResourceAsStream(file)));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line);
                output.append('\n');
            }
            return output.toString();
        } catch (Exception e) {
            Debug.exception(e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                Debug.exception(e);
            }
        }
        return null;
    }
    
    public static byte[] readFileBytes(String file) {
        try {
            return Files.readAllBytes(Paths.get(IO.class.getResource(file).toURI()));
        } catch (Exception e) {
            Debug.exception(e);
        }
        return new byte[0];
    }
    
    public static ByteBuffer readFileBuffBytes(String file) {
        byte[] arr = readFileBytes(file);
        ByteBuffer buff = memAlloc(arr.length);
        buff.put(arr);
        buff.flip();
        return buff;
    }
    
}
