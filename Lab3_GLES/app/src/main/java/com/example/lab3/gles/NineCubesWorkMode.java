package com.example.lab3.gles;

import android.opengl.GLES32;
import android.opengl.Matrix;

public class NineCubesWorkMode extends WorkMode {

    protected int numVertexChessBoard = 0;
    protected int numVertexCubes = 0;
    protected int numSquaresInChessboard, numCubes, numSquaresInCube;

    private final float[] arrayColors = new float[] {
            0.2f, 0.2f, 1.0f,   // dark chess square (neon blue)
            0.8f, 0.8f, 1.0f,   // light chess square (light neon blue)
            // red cubes
            0.4f, 0.0f, 0.0f,   // bottom cube side (darkest)
            0.6f, 0.0f, 0.0f,   // back cube side
            0.8f, 0.0f, 0.0f,   // left cube side
            0.8f, 0.0f, 0.0f,   // right cube side
            1.0f, 0.0f, 0.0f,   // front cube side
            1.0f, 0.2f, 0.2f,   // top cube side (lightest)
            // orange cubes
            0.4f, 0.2f, 0.0f,   // bottom cube side (darkest)
            0.6f, 0.3f, 0.0f,   // back cube side
            0.8f, 0.4f, 0.0f,   // left cube side
            0.8f, 0.4f, 0.0f,   // right cube side
            1.0f, 0.5f, 0.0f,   // front cube side
            1.0f, 0.6f, 0.2f,   // top cube side (lightest)
            // yellow cubes
            0.4f, 0.4f, 0.0f,   // bottom cube side (darkest)
            0.6f, 0.6f, 0.0f,   // back cube side
            0.8f, 0.8f, 0.0f,   // left cube side
            0.8f, 0.8f, 0.0f,   // right cube side
            1.0f, 1.0f, 0.0f,   // front cube side
            1.0f, 1.0f, 0.2f,   // top cube side (lightest)
    };

    protected float xTouchDown = 0, yTouchDown = 0;
    protected float xCamera = 0, yCamera = 0, zCamera = 0;

    public NineCubesWorkMode() {
        super();
        createScene();
        betaViewAngle = 85;
        alphaViewAngle = 0;

        xCamera = 0;
        yCamera = -5;
        zCamera = 1.4f;
    }

    @Override
    protected void createScene() {
        numSquaresInChessboard = 10 * 10;
        numCubes = 9 * 3;
        numSquaresInCube = 6;

        numVertexChessBoard = numSquaresInChessboard * 4;
        numVertexCubes = numCubes * numSquaresInCube * 4;
        arrayVertex = new float[numVertexChessBoard * 6 + numVertexCubes * 6];

        int posChessboard = 0, posCubes = numVertexChessBoard * 6;

        float squareWidth = 0.9f * 2 / 10,
                xlChessboard = -0.9f,
                ybChessboard = -0.9f,
                zChessboard = 0.0f;
        drawChessboard(posChessboard, xlChessboard, ybChessboard, zChessboard, squareWidth);

        float cubesBoardWidth = 0.9f * 2,
                xlCubes = xlChessboard,
                ybCubes = ybChessboard,
                zbCubes = zChessboard + cubesBoardWidth / 5;
        drawCubes(posCubes, xlCubes, ybCubes, zbCubes, cubesBoardWidth);
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

    private void drawCubes(int pos, float xlBoard, float ybBoard, float zbBoard,
                           float boardWidth) {
        float cubeWidth = boardWidth / 5;

        for (int lvl = 0; lvl < 3; lvl++) {
            float zb = zbBoard + lvl * cubeWidth * 2;
            float zt = zb + cubeWidth;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    float xl = xlBoard + i * cubeWidth * 2;
                    float yb = ybBoard + j * cubeWidth * 2;
                    float xr = xl + cubeWidth;
                    float yt = yb + cubeWidth;

                    int colorNum = 2 + j * 6;
                    drawSquare(pos, colorNum++, /// bottom
                            xl, yb, zb,     xl, yt, zb,
                            xr, yt, zb,     xr, yb, zb);
                    pos += 6 * 4;
                    drawSquare(pos, colorNum++, /// back
                            xl, yt, zb,     xl, yt, zt,
                            xr, yt, zt,     xr, yt, zb);
                    pos += 6 * 4;
                    drawSquare(pos, colorNum++, /// left
                            xl, yb, zb,     xl, yb, zt,
                            xl, yt, zt,     xl, yt, zb);
                    pos += 6 * 4;
                    drawSquare(pos, colorNum++, /// right
                            xr, yt, zb,     xr, yt, zt,
                            xr, yb, zt,     xr, yb, zb);
                    pos += 6 * 4;
                    drawSquare(pos, colorNum++, ///front
                            xl, yb, zb,     xl, yb, zt,
                            xr, yb, zt,     xr, yb, zb);
                    pos += 6 * 4;
                    drawSquare(pos, colorNum, /// top
                            xl, yb, zt,     xl, yt, zt,
                            xr, yt, zt,     xr, yb, zt);
                    pos += 6 * 4;
                }
            }
        }
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
        Matrix.rotateM(viewMatrix, 0, -betaViewAngle, 1, 0, 0);
        Matrix.rotateM(viewMatrix, 0, -alphaViewAngle, 0, 0, 1);
        Matrix.translateM(viewMatrix, 0, -xCamera, -yCamera, -zCamera);

        float aspect = (float) width / height;
        Matrix.perspectiveM(projectionMatrix, 0, 60, aspect, 0.1f, 30);

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

        for (int i = 0; i < numCubes; i++) {
            for (int j = 0; j < numSquaresInCube; j++) {
                GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN,
                        numVertexChessBoard + i * numSquaresInCube * 4 + j * 4, 4);
            }
        }

        GLES32.glBindVertexArray(0);
    }

    public boolean onTouchNotUsed() {
        return false;
    }

    public boolean onActionDown(float x, float y, int cx, int cy) {
        xTouchDown = x;
        yTouchDown = y;
        if (yTouchDown > cy * 0.8) {
            // Натискання внизу екрану зліва чи справа → зміна кута нахилу камери
            float step = 5;
            if (xTouchDown < cx * 0.5) {
                betaViewAngle -= step;
            } else {
                betaViewAngle += step;
            }
            return true;
        }
        return false;
    }

    public boolean onActionMove(float x, float y, int cx, int cy) {
        // Горизонтальний рух → обертання камери
        float Kalpha = 0.05f;
        alphaViewAngle += Kalpha * (xTouchDown - x);

        if (yTouchDown < cy * 0.8) {
            // Вертикальний рух → наближення/віддалення камери
            float sensitivity = 0.005f;
            float step = sensitivity * (yTouchDown - y);
            xCamera -= step * (float) Math.sin(Math.toRadians(alphaViewAngle));
            yCamera += step * (float) Math.cos(Math.toRadians(alphaViewAngle));
            zCamera -= step * (float) Math.cos(Math.toRadians(betaViewAngle));
        }
        xTouchDown = x;
        yTouchDown = y;
        return true;
    }
}
