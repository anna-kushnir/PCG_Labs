package com.example.lab4.gles.mode;

import android.opengl.GLES32;
import android.opengl.Matrix;

import com.example.lab4.gles.GraphicPrimitives;
import com.example.lab4.gles.Shaders;

public class DiffuseLightingMode extends WorkMode {

    protected int numVertexRectangle = 0;

    private final float[] rectangleColor = new float[] { 0.5f, 0.0f, 0.3f };
    private final float[] lightColor = new float[] { 0.6f, 1.0f, 1.0f };

    public DiffuseLightingMode() {
        super();
        createScene();
        betaViewAngle = 65;
        alphaViewAngle = 0;

        cameraCoords = new float[]{ 0.0f, -5.0f, 2.5f };
        lightCoords = new float[]{ 0.0f, 0.0f, 0.41f };
    }

    @Override
    protected void createScene() {
        numVertexRectangle = 4;
        arrayVertex = new float[6 + numVertexRectangle * 6];

        int pos = GraphicPrimitives.addVertexXYZn(arrayVertex, 0, 0, 0, 0, 1, 1, 1);

        float squareWidth = 0.9f * 2,
                xl = -0.9f,
                yb = -0.9f,
                z = 0.0f;
        GraphicPrimitives.addSquareXYZn(arrayVertex, pos, xl, yb, z, squareWidth);
    }

    @Override
    public void createShaderProgram() {
        compileAndAttachShaders(Shaders.vertexShaderCode, Shaders.fragmentShaderCode1);
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
        // Малювання квадрату
        GLES32.glUniform3fv(vMatrixHandle, 1, rectangleColor, 0);
        GLES32.glBindVertexArray(VAO_id);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN, 1, 4);

        // Малювання джерела світла
        Matrix.translateM(modelMatrix, 0, lightCoords[0], lightCoords[1], lightCoords[2]);
        GLES32.glUniformMatrix4fv(uMatrixHandle, 1, false, modelMatrix, 0);
        GLES32.glUniform3fv(vMatrixHandle, 1, lightColor, 0);
        GLES32.glDrawArrays(GLES32.GL_POINTS, 0, 1);

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
