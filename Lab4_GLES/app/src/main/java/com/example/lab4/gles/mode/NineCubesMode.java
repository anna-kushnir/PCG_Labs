package com.example.lab4.gles.mode;

import android.opengl.GLES32;
import android.opengl.Matrix;

import com.example.lab4.gles.GraphicPrimitives;
import com.example.lab4.gles.Shaders;

public class NineCubesMode extends WorkMode {
    protected int numVertexCubes = 0;
    protected int numCubes, numSquaresInCube;

    private final float[] redColor = new float[] { 0.9f, 0.0f, 0.9f };
    private final float[] lightColor = new float[] { 1.0f, 1.0f, 1.0f };

    public NineCubesMode() {
        super();
        createScene();
        betaViewAngle = 85;
        alphaViewAngle = 0;

        cameraCoords = new float[]{ 0, -2.1f, 0.9f };
        lightCoords = cameraCoords;
    }

    @Override
    protected void createScene() {
        numCubes = 9 * 3;
        numSquaresInCube = 6;

        numVertexCubes = numCubes * numSquaresInCube * 4;
        arrayVertex = new float[numVertexCubes * 6];

        float cubesBoardWidth = 0.9f * 2,
                xlCubes = -0.9f,
                ybCubes = -0.9f,
                zbCubes = 0.0f;
        GraphicPrimitives.addCubesNet(arrayVertex, 0, xlCubes, ybCubes, zbCubes, cubesBoardWidth);
    }

    @Override
    public void createShaderProgram() {
        compileAndAttachShaders(Shaders.vertexShaderCode, Shaders.fragmentShaderCode4);
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
        Matrix.perspectiveM(projectionMatrix, 0, 90, aspect, 0.1f, 30);

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
        GLES32.glUniform3fv(vMatrixHandle, 1, redColor, 0);

        GLES32.glBindVertexArray(VAO_id);
        for (int i = 0; i < numCubes; i++) {
            for (int j = 0; j < numSquaresInCube; j++) {
                GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN,
                        i * numSquaresInCube * 4 + j * 4, 4);
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
            // Натискання внизу екрану зліва чи справа → зміна нахилу камери та світла
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
        // Горизонтальний рух → обертання камери та джерела світла
        float Kalpha = 0.05f;
        alphaViewAngle += Kalpha * (xTouchDown - x);

        if (yTouchDown < cy * 0.8) {
            // Вертикальний рух → наближення/віддалення
            float sensitivity = 0.005f;
            float step = sensitivity * (yTouchDown - y);
            cameraCoords[0] -= step * (float) Math.sin(Math.toRadians(alphaViewAngle));
            cameraCoords[1] += step * (float) Math.cos(Math.toRadians(alphaViewAngle));
            cameraCoords[2] -= step * (float) Math.cos(Math.toRadians(betaViewAngle));
            lightCoords = cameraCoords;
        }
        xTouchDown = x;
        yTouchDown = y;
        return true;
    }
}
