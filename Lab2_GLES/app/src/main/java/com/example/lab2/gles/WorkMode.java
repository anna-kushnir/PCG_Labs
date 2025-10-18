package com.example.lab2.gles;

import android.opengl.GLES20;
import android.opengl.GLES32;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class WorkMode {

    protected int gl_Program = 0;
    protected int VAO_id = 0;
    protected int VBO_id = 0;

    protected float[] arrayVertex = null;

    WorkMode() {
        gl_Program = 0;
        VAO_id = VBO_id = 0;
    }

    public int getProgramId() {
        return gl_Program;
    }

    protected int compileShader(int shaderType, String shaderCode) {
        int shader_id = GLES32.glCreateShader(shaderType);
        GLES32.glShaderSource(shader_id, shaderCode);
        GLES32.glCompileShader(shader_id);
        // Перевірка на помилки при компіляції
        int[] res = new int[1];
        GLES32.glGetShaderiv(shader_id, GLES20.GL_COMPILE_STATUS, res, 0);
        if (res[0] != 1) return 0;
        return shader_id;
    }

    /** Створення та приєднання шейдерної програми */
    protected void compileAndAttachShaders(String vShCode, String fShCode) {
        gl_Program = 0;
        int vertex_shader_id = compileShader(GLES32.GL_VERTEX_SHADER, vShCode);
        if (vertex_shader_id == 0) return;
        int fragment_shader_id = compileShader(GLES32.GL_FRAGMENT_SHADER, fShCode);
        if (fragment_shader_id == 0) return;

        gl_Program = GLES32.glCreateProgram();
        GLES32.glAttachShader(gl_Program, vertex_shader_id);
        GLES32.glAttachShader(gl_Program, fragment_shader_id);
        GLES32.glLinkProgram(gl_Program);
        GLES32.glDeleteShader(vertex_shader_id);
        GLES32.glDeleteShader(fragment_shader_id);
    }

    /** Встановлення id для VAO та VBO */
    protected void getId_VAO_VBO() {
        int[] tmp = new int[2];
        GLES32.glGenVertexArrays(1, tmp, 0);
        VAO_id = tmp[0];
        GLES32.glGenBuffers(1, tmp, 0);
        VBO_id = tmp[0];
    }

    protected void vertexArrayBind(float[] vertexCoords, int stride,
                                    String attrib1, int offset1,
                                    String attrib2, int offset2) {
        if (gl_Program <= 0) return;
        // Створення та прив'язування масивів вершин
        ByteBuffer bb = ByteBuffer.allocateDirect(vertexCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());

        FloatBuffer vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertexCoords);
        vertexBuffer.position(0);

        getId_VAO_VBO();

        // Спочатку прив'язування масиву вершин
        GLES32.glBindVertexArray(VAO_id);
        // Потім прив'язування та встановлення буферу вершин
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, VBO_id);
        GLES32.glBufferData(GLES32.GL_ARRAY_BUFFER,
                vertexCoords.length * 4,
                vertexBuffer,
                GLES32.GL_STATIC_DRAW);

        // Визначення вказівника на атрибути
        int handle = GLES32.glGetAttribLocation(gl_Program, attrib1);
        GLES32.glEnableVertexAttribArray(handle);
        GLES32.glVertexAttribPointer(handle, 3,
                GLES32.GL_FLOAT, false, stride*4, offset1);

        handle = GLES32.glGetAttribLocation(gl_Program, attrib2);
        GLES32.glEnableVertexAttribArray(handle);
        GLES32.glVertexAttribPointer(handle, 3,
                GLES32.GL_FLOAT, false, stride*4, offset2);

        GLES32.glEnableVertexAttribArray(0);
        GLES32.glBindBuffer(GLES32.GL_ARRAY_BUFFER, 0);
        GLES32.glBindVertexArray(0);
    }

    public void createShaderProgram() { }
    protected void createScene() { }
    public void useProgramForDrawing() {
    }
}
