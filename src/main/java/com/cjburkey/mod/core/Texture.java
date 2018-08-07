package com.cjburkey.mod.core;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Texture {
    
    private int texture;
    
    private Texture(int w, int h, ByteBuffer rawBytes) {
        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, rawBytes);
        glGenerateMipmap(GL_TEXTURE_2D);
        
        glBindTexture(GL_TEXTURE_2D, 0);
    }
    
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texture);
    }
    
    public void cleanup() {
        glDeleteTextures(texture);
    }
    
    public static void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
    
    public static Texture createTexture(byte[] fileBytes) {
        ByteBuffer buff = memAlloc(fileBytes.length);
        buff.put(fileBytes);
        buff.flip();
        
        int[] w = new int[1];
        int[] h = new int[1];
        ByteBuffer imgBytes = stbi_load_from_memory(buff, w, h, new int[1], 0);
        if (w[0] < 1 || h[0] < 1 || imgBytes == null) {
            return null;
        }
        
        imgBytes.flip();
        Texture texture = new Texture(w[0], h[0], imgBytes);
        stbi_image_free(imgBytes);
        return texture;
    }
    
    public static Texture createTexture(String path) {
        return createTexture(IO.readFileBytes(path));
    }
    
}
