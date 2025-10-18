package com.example.lab3.gles;

import android.opengl.GLES32;
import android.opengl.Matrix;
import android.os.SystemClock;

public class PyramidRotationWorkMode extends WorkMode {

    protected int numVertexChessBoard = 0;
    protected int numVertexPyramid = 0;
    protected int numSquaresInChessboard, numTrianglesInPyramid;

    private final float[] arrayColors = new float[] {
            0.2f, 0.2f, 1.0f,   // dark chess square (neon blue)
            0.8f, 0.8f, 1.0f,   // light chess square (light neon blue)

            1.0f, 0.6f, 1.0f,   // light pyramid side
            1.0f, 0.2f, 1.0f,   // medium pyramid side
            0.8f, 0.0f, 0.8f,   // dark pyramid side
    };

    protected float alphaAnglePrev = 0, viewDistancePrev = 0;
    protected float xTouchDown = 0, yTouchDown = 0;

    public PyramidRotationWorkMode() {
        super();
        createScene();
        betaViewAngle = 65;
        alphaViewAngle = 20;
        viewDistance = 7;
    }

    @Override
    protected void createScene() {
        numSquaresInChessboard = 10 * 10;
        numTrianglesInPyramid = 4;

        numVertexChessBoard = numSquaresInChessboard * 4;
        numVertexPyramid = numTrianglesInPyramid * 3;
        arrayVertex = new float[numVertexChessBoard * 6 + numVertexPyramid * 6];

        int posChessboard = 0, posPyramid = numVertexChessBoard * 6;

        float squareWidth = 0.9f * 2 / 10,
                xlChessboard = -0.9f,
                ybChessboard = -0.9f,
                zChessboard = 0.0f;
        drawChessboard(posChessboard, xlChessboard, ybChessboard, zChessboard, squareWidth);

        float pyramidWidth = squareWidth * 3,
                pyramidHeight = squareWidth * 3.5f,
                xlPyramid = xlChessboard + squareWidth * 3.5f,
                ybPyramid = ybChessboard + squareWidth * 3.5f,
                zbPyramid = zChessboard + 0.01f;
        drawPyramid(posPyramid, xlPyramid, ybPyramid, zbPyramid, pyramidWidth, pyramidHeight);
    }

    private void drawChessboard(int pos, float xlChessboard, float ybChessboard, float z,
                                float squareWidth) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                float xl = xlChessboard + squareWidth * i;
                float xr = xl + squareWidth;
                float yb = ybChessboard + squareWidth * j;
                float yt = yb + squareWidth;
                drawSquare(pos, (i + j) % 2,
                        xl, yb, z,
                        xl, yt, z,
                        xr, yt, z,
                        xr, yb, z);
                pos += 6 * 4;
            }
        }
    }

    private void drawPyramid(int pos, float xl, float yb, float zb,
                             float width, float height) {
        float xr = xl + width;
        float yt = yb + width;
        float xc = (xl + xr) / 2;
        float yc = (yb + yt) / 2;
        float zc = zb + height;
        drawTriangle(pos, 2,
                xl, yb, zb,
                xr, yb, zb,
                xc, yc, zc);
        pos += 6 * 3;
        drawTriangle(pos, 3,
                xr, yb, zb,
                xr, yt, zb,
                xc, yc, zc);
        pos += 6 * 3;
        drawTriangle(pos, 4,
                xr, yt, zb,
                xl, yt, zb,
                xc, yc, zc);
        pos += 6 * 3;
        drawTriangle(pos, 3,
                xl, yt, zb,
                xl, yb, zb,
                xc, yc, zc);
    }

    private void drawSquare(int pos, int colorNum,
                            float x1, float y1, float z1,
                            float x2, float y2, float z2,
                            float x3, float y3, float z3,
                            float x4, float y4, float z4) {
        addVertex(pos, colorNum, x1, y1, z1);
        addVertex(pos + 6, colorNum, x2, y2, z2);
        addVertex(pos + 6 * 2, colorNum, x3, y3, z3);
        addVertex(pos + 6 * 3, colorNum, x4, y4, z4);
    }

    private void drawTriangle(int pos, int colorNum,
                              float x1, float y1, float z1,
                              float x2, float y2, float z2,
                              float x3, float y3, float z3) {
        addVertex(pos, colorNum, x1, y1, z1);
        addVertex(pos + 6, colorNum, x2, y2, z2);
        addVertex(pos + 6 * 2, colorNum, x3, y3, z3);
    }

    private void addVertex(int pos, int colorNum,
                           float x, float y, float z) {
        arrayVertex[pos++] = x;
        arrayVertex[pos++] = y;
        arrayVertex[pos++] = z;
        arrayVertex[pos++] = arrayColors[colorNum * 3];
        arrayVertex[pos++] = arrayColors[colorNum * 3 + 1];
        arrayVertex[pos] = arrayColors[colorNum * 3 + 2];
    }

    @Override
    public void createShaderProgram() {
        compileAndAttachShaders(Shaders.vertexShaderCode, Shaders.fragmentShaderCode);
        vertexArrayBind(arrayVertex, 6,
                "vPosition", 0,
                "vColor", 3 * 4);
    }

    @Override
    public void useProgramForDrawing(int width, int height) {
        Matrix.setIdentityM(modelMatrix, 0);

        Matrix.setIdentityM(viewMatrix, 0);
        Matrix.translateM(viewMatrix, 0, 0, 0, -viewDistance);
        Matrix.rotateM(viewMatrix, 0, -betaViewAngle, 1, 0, 0);
        Matrix.rotateM(viewMatrix, 0, -alphaViewAngle, 0, 0, 1);

        float aspect = (float) width / height;
        Matrix.perspectiveM(projectionMatrix, 0, 45, aspect, 0.1f, 50);

        int vMatrixHandle = GLES32.glGetUniformLocation(gl_Program, "uViewMatrix");
        GLES32.glUniformMatrix4fv(vMatrixHandle, 1, false, viewMatrix, 0);
        vMatrixHandle = GLES32.glGetUniformLocation(gl_Program, "uProjMatrix");
        GLES32.glUniformMatrix4fv(vMatrixHandle, 1, false, projectionMatrix, 0);
        vMatrixHandle = GLES32.glGetUniformLocation(gl_Program, "uModelMatrix");
        GLES32.glUniformMatrix4fv(vMatrixHandle, 1, false, modelMatrix, 0);

        GLES32.glBindVertexArray(VAO_id);
        for (int i = 0; i < numSquaresInChessboard; i++) {
            GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN, i * 4, 4);
        }

        long time = SystemClock.uptimeMillis() % 3600L;
        float pyramidRotationAngle = 0.1f * (float)time;

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.rotateM(modelMatrix, 0, pyramidRotationAngle, 0, 0, -1);
        GLES32.glUniformMatrix4fv(vMatrixHandle, 1, false, modelMatrix, 0);

        GLES32.glDrawArrays(GLES32.GL_TRIANGLES, numVertexChessBoard, numVertexPyramid);

        GLES32.glBindVertexArray(0);
    }

    public boolean onTouchNotUsed() {
        return false;
    }

    public boolean onActionDown(float x, float y, int cx, int cy) {
        xTouchDown = x;
        yTouchDown = y;
        alphaAnglePrev = alphaViewAngle;
        viewDistancePrev = viewDistance;
        return false;
    }

    public boolean onActionMove(float x, float y, int cx, int cy) {
        float Kalpha = 0.1f, Kdist = 0.005f;
        alphaViewAngle = alphaAnglePrev + Kalpha * (xTouchDown - x);
        viewDistance = viewDistancePrev - Kdist * (yTouchDown - y);
        return true;
    }
}
