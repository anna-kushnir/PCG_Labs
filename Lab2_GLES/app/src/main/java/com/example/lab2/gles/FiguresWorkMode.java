package com.example.lab2.gles;

import android.opengl.GLES32;

public class FiguresWorkMode extends WorkMode {

    protected int numVertexRainbow = 0, numVertexPolygon = 0;
    private float coefX = 1.0f, coefY = 1.0f;

    /** Colors: red, orange, yellow, green, light blue, blue, purple, white */
    private final float[] arrayColors = new float[] {
            1.0f, 0.0f, 0.0f,   // red
            1.0f, 0.5f, 0.0f,   // orange
            1.0f, 1.0f, 0.0f,   // yellow
            0.0f, 1.0f, 0.0f,   // green
            0.0f, 1.0f, 1.0f,   // light blue
            0.0f, 0.0f, 1.0f,   // blue
            1.0f, 0.0f, 1.0f,   // purple
            1.0f, 1.0f, 1.0f    // white
    };

    public FiguresWorkMode(int width, int height) {
        super();
        if (width < height) {
            coefY = (float)width / (float)height;
        } else
            coefX = (float)height / (float)width;
        createScene();
    }

    @Override
    protected void createScene() {
        int numTrianglesInRainbow = 12;
        int numTrianglesInPolygon = 7;
        numVertexRainbow = numTrianglesInRainbow + 2;
        numVertexPolygon = numTrianglesInPolygon + 2;
        arrayVertex = new float[numVertexRainbow * 6 + numVertexPolygon * 6];

        int posRainbow = 0,
                posPolygon = posRainbow + numVertexRainbow * 6;

        drawRainbow(posRainbow,
                0.3f, 0.15f,
                -0.9f, -0.9f);

        drawPolygon(posPolygon, numTrianglesInPolygon,
                0.0f, 0.0f, 0.7f);
    }

    /** Малювання райдужної стрічки */
    private void drawRainbow(int posRainbow,
                             float widthTrianglesRainbow, float heightTrianglesRainbow,
                             float xlRainbow, float ybRainbow) {
        float ytRainbow = ybRainbow + heightTrianglesRainbow;
        for (int i = 0; i < 7; i++) {
            arrayVertex[posRainbow++] = xlRainbow;
            arrayVertex[posRainbow++] = ybRainbow;
            arrayVertex[posRainbow++] = 0.0f;
            arrayVertex[posRainbow++] = arrayColors[i * 3];
            arrayVertex[posRainbow++] = arrayColors[i * 3 + 1];
            arrayVertex[posRainbow++] = arrayColors[i * 3 + 2];

            arrayVertex[posRainbow++] = xlRainbow;
            arrayVertex[posRainbow++] = ytRainbow;
            arrayVertex[posRainbow++] = 0.0f;
            arrayVertex[posRainbow++] = arrayColors[i * 3];
            arrayVertex[posRainbow++] = arrayColors[i * 3 + 1];
            arrayVertex[posRainbow++] = arrayColors[i * 3 + 2];

            xlRainbow += widthTrianglesRainbow;
        }
    }

    /** Малювання семикутника */
    private void drawPolygon(int posPolygon, int numTrianglesInPolygon,
                             float xcPolygon, float ycPolygon, float radiusPolygon) {
        arrayVertex[posPolygon++] = xcPolygon;
        arrayVertex[posPolygon++] = ycPolygon;
        arrayVertex[posPolygon++] = 0.0f;
        arrayVertex[posPolygon++] = arrayColors[7 * 3];
        arrayVertex[posPolygon++] = arrayColors[7 * 3 + 1];
        arrayVertex[posPolygon++] = arrayColors[7 * 3 + 2];

        float xPolygonFirst = 0, yPolygonFirst = 0;
        for (int i = 0; i < numTrianglesInPolygon; i++) {
            float a = (2 * (float)Math.PI) / numTrianglesInPolygon * -i + (float)Math.PI / 2;
            float xPolygon = xcPolygon + coefX * radiusPolygon * (float)Math.cos(a);
            float yPolygon = ycPolygon + coefY * radiusPolygon * (float)Math.sin(a);
            if (i == 0) {
                xPolygonFirst = xPolygon;
                yPolygonFirst = yPolygon;
            }
            arrayVertex[posPolygon++] = xPolygon;
            arrayVertex[posPolygon++] = yPolygon;
            arrayVertex[posPolygon++] = 0.0f;
            arrayVertex[posPolygon++] = arrayColors[i * 3];
            arrayVertex[posPolygon++] = arrayColors[i * 3 + 1];
            arrayVertex[posPolygon++] = arrayColors[i * 3 + 2];
        }
        arrayVertex[posPolygon++] = xPolygonFirst;
        arrayVertex[posPolygon++] = yPolygonFirst;
        arrayVertex[posPolygon++] = 0.0f;
        arrayVertex[posPolygon++] = arrayColors[0];
        arrayVertex[posPolygon++] = arrayColors[1];
        arrayVertex[posPolygon] = arrayColors[2];
    }

    @Override
    public void createShaderProgram() {
        compileAndAttachShaders(Shaders.vertexShaderCode, Shaders.fragmentShaderCode);
        vertexArrayBind(arrayVertex, 6,
                "vPosition", 0,
                "vColor", 3 * 4);
    }

    @Override
    public void useProgramForDrawing() {
        int colorHandle = GLES32.glGetUniformLocation(gl_Program, "vLight");
        GLES32.glUniform1f(colorHandle, 1.0f);
        GLES32.glBindVertexArray(VAO_id);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_STRIP, 0, numVertexRainbow);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN, numVertexRainbow, numVertexPolygon);
        GLES32.glBindVertexArray(0);
    }
}
