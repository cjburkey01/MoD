package com.cjburkey.mod;

import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFWErrorCallback.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class MoD implements Runnable {
    
    private long window;
    
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> Debug.exception(e));
        new MoD().run();
    }
    
    public void run() {
        if (!glfwInit()) {
            throw new RuntimeException("Failed to initialize GLFW");
        }
        Debug.info("Initialized GLFW {}", glfwGetVersionString());
    
        createPrint(System.err).set();
        
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        
        window = glfwCreateWindow(300, 300, "MoD! 0.0.1", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }
        
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        Debug.info("Initialized OpenGL {}", glGetString(GL_VERSION));
    
        glfwSwapInterval(1);
        glfwSetFramebufferSizeCallback(window, (window, width, height) -> glViewport(0, 0, width, height));
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        
        glfwShowWindow(window);
        Debug.info("Initialized GLFW Window");
        
        while (true) {
            update();
            render();
            
            if (glfwWindowShouldClose(window)) {
                break;
            }
        }
        
        glfwHideWindow(window);
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
        Debug.info("Terminated GLFW");
    }
    
    private void update() {
        glfwPollEvents();
        // TODO: UPDATES
    }
    
    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        // TODO: RENDERS
        glfwSwapBuffers(window);
    }
    
}