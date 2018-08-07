package com.cjburkey.mod.core;

import com.cjburkey.mod.entity.EntityWorld;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWErrorCallback.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public final class MoD implements Runnable {
    
    public static final MoD instance = new MoD();
    private static boolean running = false;
    private static final String title = "MoD! 0.0.1 | FPS: %s";
    
    private long lastFrameTime = System.nanoTime();
    private double deltaTimeD = 0.0d;
    private double gameTime = 0.0d;
    private long gameTicks = 0L;
    private long gameRenders = 0L;
    
    private long window;
    private EntityWorld world = new EntityWorld();
    
    private MoD() {
    }
    
    public void run() {
        if (running) {
            return;
        }
        running = true;
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
        
        glEnable(GL_TEXTURE);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        Debug.info("Initialized OpenGL {}", glGetString(GL_VERSION));
        
        glfwSwapInterval(1);
        glfwSetFramebufferSizeCallback(window, (window, w, h) -> glViewport(0, 0, w, h));
        glfwSetKeyCallback(window, (win, key, code, action, mods) -> {
            if (action == GLFW_PRESS) {
                Input.onKeyPress(key);
            } else if (action == GLFW_RELEASE) {
                Input.onKeyRelease(key);
            }
        });
        glfwSetMouseButtonCallback(window, (win, btn, action, mods) -> {
            if (action == GLFW_PRESS) {
                Input.onMousePress(btn);
            } else if (action == GLFW_RELEASE) {
                Input.onMouseRelease(btn);
            }
        });
        glfwSetCursorPosCallback(window, (win, x, y) -> Input.onMouseMove((float) x, (float) y));
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        
        glfwShowWindow(window);
        Debug.info("Initialized GLFW Window");
        
        Game.init();
        
        while (running) {
            long currentFrameTime = System.nanoTime();
            deltaTimeD = (currentFrameTime - lastFrameTime) / 1000000000.0d;
            if (deltaTimeD < 1.0d / 200.0d) {
                try {
                    Thread.sleep(1L);
                } catch (Exception e) {
                    Debug.exception(e);
                }
            }
            lastFrameTime = currentFrameTime;
            
            Input.onUpdate();
            glfwPollEvents();
            update();
            render();
            
            if (glfwWindowShouldClose(window)) {
                running = false;
            }
            
            gameTime += deltaTimeD;
        }
        
        glfwHideWindow(window);
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        GLFWErrorCallback c = glfwSetErrorCallback(null);
        if (c != null) {
            c.free();
        }
        Debug.info("Terminated GLFW");
        
        world.onExit();
        Debug.info("Terminated game");
        
        Game.exit();
    }
    
    private void update() {
        world.onUpdate(getDeltaTime());
    
        gameTicks ++;
    }
    
    private void render() {
        glfwSetWindowTitle(window, String.format(title, Debug.format(1.0d / deltaTimeD, 2)));
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        world.onRender(getDeltaTime());
        
        glfwSwapBuffers(window);
        
        gameRenders ++;
    }
    
    public void stop() {
        running = false;
    }
    
    public float getGameTime() {
        return (float) gameTime;
    }
    
    public float getDeltaTime() {
        return (float) deltaTimeD;
    }
    
    public long getGameTicks() {
        return gameTicks;
    }
    
    public long getGameFrames() {
        return gameRenders;
    }
    
    public EntityWorld getWorld() {
        return world;
    }
    
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> Debug.exception(e));
        instance.run();
        Runtime.getRuntime().exit(0);
    }
    
}