package com.example.lab4.gles.mode;

import android.opengl.GLES32;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.example.lab4.gles.GraphicPrimitives;
import com.example.lab4.gles.Shaders;

public class PyramidMode extends WorkMode {

    protected int numVertexChessBoard = 0;
    protected int numVertexPyramid = 0;
    protected int numSquaresInChessboard, numTrianglesInPyramid;

    private final float[] darkChessColor = new float[] { 0.2f, 0.2f, 1.0f };    // neon blue
    private final float[] lightChessColor = new float[] { 0.8f, 0.8f, 1.0f };   // light neon blue
    private final float[] pyramidColor = new float[] { 1.0f, 0.2f, 1.0f };
    private final float[] lightColor = new float[] { 1.0f, 1.0f, 1.0f };

    public PyramidMode() {
        super();
        createScene();
        betaViewAngle = 65;
        alphaViewAngle = 10;

        cameraCoords = new float[]{ 0.8f, -5.0f, 2.5f };
        lightCoords = new float[]{ 0.5f, 0.1f, 0.41f };
    }

    @Override
    protected void createScene() {
        numSquaresInChessboard = 10 * 10;
        numTrianglesInPyramid = 4;

        numVertexChessBoard = numSquaresInChessboard * 4;
        numVertexPyramid = numTrianglesInPyramid * 3;
        arrayVertex = new float[6 + numVertexChessBoard * 6 + numVertexPyramid * 6];

        int pos = GraphicPrimitives.addVertexXYZn(arrayVertex, 0, 0, 0, 0, 1, 1, 1);

        float squareWidth = 0.9f * 2 / 10,
                xlChessboard = -0.9f,
                ybChessboard = -0.9f,
                zChessboard = 0.0f;
        pos = GraphicPrimitives.addChessboard(arrayVertex, pos, xlChessboard, ybChessboard, zChessboard, squareWidth);

        float pyramidWidth = squareWidth * 3,
                pyramidHeight = squareWidth * 3,
                xlPyramid = xlChessboard + squareWidth * 3.5f,
                ybPyramid = ybChessboard + squareWidth * 3.5f,
                zbPyramid = zChessboard + 0.01f;
        GraphicPrimitives.addPyramid(arrayVertex, pos, xlPyramid, ybPyramid, zbPyramid, pyramidWidth, pyramidHeight);
    }

    @Override
    public void createShaderProgram() {
        compileAndAttachShaders(Shaders.vertexShaderCode, Shaders.fragmentShaderCode3);
        vertexArrayBind(arrayVertex, 6,
                "vPosition", 0,
                "vNormal", 3 * 4);
    }

    @Override
    public void useProgramForDrawing(int width, int height) {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.setIdentityM(viewMatrix, 0);

        Matrix.rotateM(viewMatrix, 0, -betaViewAngle, 1, 0, 0);
        Matrix.rotateM(viewMatrix, 0, -alphaViewAngle, 0, 0, 1);
        Matrix.translateM(viewMatrix, 0, -cameraCoords[0], -cameraCoords[1], -cameraCoords[2]);

        float aspect = (float) width / height;
        Matrix.perspectiveM(projectionMatrix, 0, 45, aspect, 0.1f, 50);

        int uMatrixHandle = GLES32.glGetUniformLocation(gl_Program, "uViewMatrix");
        GLES32.glUniformMatrix4fv(uMatrixHandle, 1, false, viewMatrix, 0);
        uMatrixHandle = GLES32.glGetUniformLocation(gl_Program, "uProjMatrix");
        GLES32.glUniformMatrix4fv(uMatrixHandle, 1, false, projectionMatrix, 0);
        uMatrixHandle = GLES32.glGetUniformLocation(gl_Program, "uModelMatrix");
        GLES32.glUniformMatrix4fv(uMatrixHandle, 1, false, modelMatrix, 0);

        int vMatrixHandle = GLES32.glGetUniformLocation(gl_Program, "vLightColor");
        GLES32.glUniform3fv(vMatrixHandle, 1, lightColor, 0);
        vMatrixHandle = GLES32.glGetUniformLocation(gl_Program, "vLightPos");
        GLES32.glUniform3fv(vMatrixHandle, 1, lightCoords, 0);
        vMatrixHandle = GLES32.glGetUniformLocation(gl_Program, "vEyePos");
        GLES32.glUniform3fv(vMatrixHandle, 1, cameraCoords, 0);

        vMatrixHandle = GLES32.glGetUniformLocation(gl_Program, "vColor");
        // Малювання темних квадратів шахової дошки
        GLES32.glUniform3fv(vMatrixHandle, 1, darkChessColor, 0);
        GLES32.glBindVertexArray(VAO_id);
        for (int i = 0; i < numSquaresInChessboard / 2; i++) {
            GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN, i * 4 + 1, 4);
        }

        // Малювання світлих квадратів шахової дошки
        GLES32.glUniform3fv(vMatrixHandle, 1, lightChessColor, 0);
        for (int i = numSquaresInChessboard / 2; i < numSquaresInChessboard; i++) {
            GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN, i * 4 + 1, 4);
        }

        // Малювання джерела світла
        Matrix.translateM(modelMatrix, 0, lightCoords[0], lightCoords[1], lightCoords[2]);
        GLES32.glUniformMatrix4fv(uMatrixHandle, 1, false, modelMatrix, 0);
        GLES32.glUniform3fv(vMatrixHandle, 1, lightColor, 0);
        GLES32.glDrawArrays(GLES32.GL_POINTS, 0, 1);

        // Малювання піраміди
        long time = SystemClock.uptimeMillis() % 3600L;
        float pyramidRotationAngle = 0.1f * (float)time;

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.rotateM(modelMatrix, 0, pyramidRotationAngle, 0, 0, -1);
        GLES32.glUniformMatrix4fv(uMatrixHandle, 1, false, modelMatrix, 0);
        GLES32.glUniform3fv(vMatrixHandle, 1, pyramidColor, 0);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, numVertexChessBoard + 1, numVertexPyramid);

        GLES32.glBindVertexArray(0);
    }

    public boolean onTouchNotUsed() {
        return false;
    }

    public boolean onActionDown(float x, float y, int cx, int cy) {
        xTouchDown = x;
        yTouchDown = y;
        if (yTouchDown > cy * 0.8) {
            // Натискання внизу екрану зліва чи справа → зміна Z-координати
            float step = 0.05f;
            if (xTouchDown < cx * 0.5) {
                if (lightCoords[2] - step >= 0.01f) {
                    lightCoords[2] -= step;
                }
            } else {
                lightCoords[2] += step;
            }
            return true;
        }
        return false;
    }

    public boolean onActionMove(float x, float y, int cx, int cy) {
        // Горизонтальний рух → зміна X-координати
        float Kx = 0.002f, Ky = 0.005f;
        lightCoords[0] -= Kx * (xTouchDown - x);

        if (yTouchDown < cy * 0.8) {
            // Вертикальний рух → зміна Y-координати
            lightCoords[1] += Ky * (yTouchDown - y);
        }

        xTouchDown = x;
        yTouchDown = y;
        return true;
    }
}
