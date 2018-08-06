package com.cjburkey.mod;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWErrorCallback.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

public class MoD implements Runnable {
    
    public static final MoD instance = new MoD();
    private static boolean running = false;
    private static final String title = "MoD! 0.0.1 | FPS: %s";
    
    private long currentFrameTime = System.nanoTime();
    private long lastFrameTime = System.nanoTime();
    private double deltaTimeD = 0.0d;
    private double gameTime = 0.0d;
    private long gameTicks = 0L;
    private long gameRenders = 0L;
    
    private long window;
    private int shaderProgram;
    private int vao;
    private int vbo;
    private int ebo;
    
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
        Debug.info("Initialized OpenGL {}", glGetString(GL_VERSION));
    
        glfwSwapInterval(1);
        glfwSetFramebufferSizeCallback(window, (window, w, h) -> glViewport(0, 0, w, h));
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    
        glfwShowWindow(window);
        Debug.info("Initialized GLFW Window");
    
        initShaders();
        initTestMesh();
    
        while (running) {
            currentFrameTime = System.nanoTime();
            deltaTimeD = (currentFrameTime - lastFrameTime) / 1000000000.0d;
            if (deltaTimeD < 1.0d / 200.0d) {
                try {
                    Thread.sleep(1L);
                } catch (Exception e) {
                    Debug.exception(e);
                }
            }
            lastFrameTime = currentFrameTime;
            
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
        glfwSetErrorCallback(null).free();
        Debug.info("Terminated GLFW");
        
        glDeleteProgram(shaderProgram);
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
        Debug.info("Cleaned up OpenGL");
    }
    
    private void initShaders() {
        shaderProgram = glCreateProgram();
        
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, loadFileText("/assets/shaders/basic.vsh"));
        glCompileShader(vertexShader);
        String log = glGetShaderInfoLog(vertexShader);
        if (!(log = log.trim()).isEmpty()) {
            Debug.error("Failed to compile vertex shader: {}", log);
        }
        glAttachShader(shaderProgram, vertexShader);
        
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, loadFileText("/assets/shaders/basic.fsh"));
        glCompileShader(fragmentShader);
        log = glGetShaderInfoLog(fragmentShader);
        if (!(log = log.trim()).isEmpty()) {
            Debug.error("Failed to compile fragment shader: {}", log);
        }
        glAttachShader(shaderProgram, fragmentShader);
        
        glLinkProgram(shaderProgram);
        log = glGetProgramInfoLog(shaderProgram);
        if (!(log = log.trim()).isEmpty()) {
            Debug.error("Failed to link shader program: {}", log);
        }
        
        glValidateProgram(shaderProgram);
        log = glGetProgramInfoLog(shaderProgram);
        if (!(log = log.trim()).isEmpty()) {
            Debug.warn("Failed to validate shader program: {}", log);
        }
        
        glDetachShader(shaderProgram, vertexShader);
        glDetachShader(shaderProgram, fragmentShader);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
        
        Debug.info("Initialized OpenGL shaders");
    }
    
    private String loadFileText(String file) {
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
    
    private void initTestMesh() {
        float[] verts = new float[] {
            0.0f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
        };
        short[] inds = new short[] {
            0, 1, 2,
        };
        
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        ebo = glGenBuffers();
        
        glBindVertexArray(vao);
        
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, verts, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, inds, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        
        glBindVertexArray(0);
        
        Debug.info("Initialized test mesh");
    }
    
    private void update() {
        // TODO: UPDATES
    
        gameTicks ++;
    }
    
    private void render() {
        glfwSetWindowTitle(window, String.format(title, Debug.format(1.0d / deltaTimeD, 2)));
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        renderTestMesh();
        
        glfwSwapBuffers(window);
        
        gameRenders ++;
    }
    
    private void renderTestMesh() {
        glUseProgram(shaderProgram);
        
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        
        glDrawElements(GL_TRIANGLES, 3, GL_UNSIGNED_SHORT, 0);
        
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        
        glUseProgram(0);
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
    
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> Debug.exception(e));
        instance.run();
    }
    
}