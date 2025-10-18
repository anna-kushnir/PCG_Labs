package com.example.lab1.gles;

public class Shaders {

    public static final String vertexShaderCode =
            "#version 300 es\n" +
            "in vec3 vPosition;\n" +
            "void main() {\n" +
            "   gl_Position = vec4(vPosition, 1.0f);\n" +
            "}\n";

    public static final String fragmentShaderCode =
            "#version 300 es\n" +
            "precision mediump float;\n" +
            "out vec4 outColor;\n" +
            "void main() {\n" +
            "   outColor = vec4(1.0f, 0.0f, 0.5f, 1.0f);\n" +
            "}\n";
}
