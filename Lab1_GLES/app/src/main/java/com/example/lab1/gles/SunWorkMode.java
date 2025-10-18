package com.example.lab1.gles;

import android.opengl.GLES32;

public class SunWorkMode extends WorkMode {

    protected int numVertexTriangle = 0, numVertexBeam = 0, numVertexSun = 0;
    private float coefX = 1.0f, coefY = 1.0f;

    public SunWorkMode(int width, int height) {
        super();
        if (width < height) {
            coefY = (float)width / (float)height;
        } else
            coefX = (float)height / (float)width;
        createScene();
    }

    @Override
    protected void createScene() {
        int N = 16;
        numVertexTriangle = 3;
        numVertexBeam = N * 2;
        numVertexSun = N + 1;
        arrayVertex = new float[numVertexTriangle * 3 + numVertexBeam * 3 + numVertexSun * 3];

        drawTriangle(
                0.8f, -0.6f,
                0.1f, -0.6f,
                0.45f, -0.1f);

        int posBeam = numVertexTriangle * 3,
                posSun = posBeam + numVertexBeam * 3;

        drawSun(posBeam, posSun, N, 0.0f, 0.4f, 0.5f, 0.25f);
    }

    /** Малювання трикутника */
    private void drawTriangle(float x1, float y1,
                              float x2, float y2,
                              float x3, float y3) {
        arrayVertex[0] = x1 * coefX;
        arrayVertex[1] = y1 * coefY;
        arrayVertex[3] = x2 * coefX;
        arrayVertex[4] = y2 * coefY;
        arrayVertex[6] = x3 * coefX;
        arrayVertex[7] = y3 * coefY;
        arrayVertex[2] = arrayVertex[5] = arrayVertex[8] = 0.0f;
    }

    /** Малювання сонечка */
    private void drawSun(int posBeam, int posSun, int N, float xcSun, float ycSun, float radiusBeam, float radiusSun) {
        float xSunFirst = 0, ySunFirst = 0;
        for (int i = 0; i < N; i++) {
            float a = (2 * (float)Math.PI) / N * i;
            float xBeam = xcSun + coefX * radiusBeam * (float)Math.cos(a);
            float yBeam = ycSun + coefY * radiusBeam * (float)Math.sin(a);
            arrayVertex[posBeam++] = xcSun;
            arrayVertex[posBeam++] = ycSun;
            arrayVertex[posBeam++] = 0.0f;
            arrayVertex[posBeam++] = xBeam;
            arrayVertex[posBeam++] = yBeam;
            arrayVertex[posBeam++] = 0.0f;
            float xSun = xcSun + coefX * radiusSun * (float)Math.cos(a);
            float ySun = ycSun + coefY * radiusSun * (float)Math.sin(a);
            if (i == 0) {
                xSunFirst = xSun;
                ySunFirst = ySun;
            }
            arrayVertex[posSun++] = xSun;
            arrayVertex[posSun++] = ySun;
            arrayVertex[posSun++] = 0.0f;
        }
        arrayVertex[posSun++] = xSunFirst;
        arrayVertex[posSun++] = ySunFirst;
        arrayVertex[posSun] = 0.0f;
    }

    @Override
    public void createShaderProgram() {
        compileAndAttachShaders(Shaders.vertexShaderCode, Shaders.fragmentShaderCode);
        vertexArrayBind(arrayVertex, "vPosition");
    }

    @Override
    public void useProgramForDrawing() {
        GLES32.glBindVertexArray(VAO_id);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, 0, numVertexTriangle);
        GLES32.glLineWidth(5);
        GLES32.glDrawArrays(GLES32.GL_LINES, numVertexTriangle, numVertexBeam);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN, numVertexTriangle + numVertexBeam, numVertexSun);
        GLES32.glBindVertexArray(0);
    }
}
