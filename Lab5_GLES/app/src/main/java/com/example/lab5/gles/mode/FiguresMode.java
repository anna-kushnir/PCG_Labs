package com.example.lab5.gles.mode;

import android.opengl.GLES32;
import android.opengl.Matrix;

import com.example.lab5.gles.GraphicPrimitives;
import com.example.lab5.gles.Shaders;

public class FiguresMode extends WorkMode {

    protected int numVertexCylinder = 0;

    protected int numVertexInPoleSphere = 0;
    protected int numVertexInRowSphere = 0;
    protected int numRowsBetweenPolesSphere = 0;

    protected int numVertexInRowTorus = 0;
    protected int numRowsTorus = 0;

    protected int numSquaresInChessboard = 0;
    protected int numVertexChessboard = 0;

    private final float[] cylinderColor = new float[] { 0.5f, 1.0f, 1.0f };
//    private final float[] sphereColor = new float[] { 0.5f, 1.0f, 1.0f };
    private final float[] sphereColor = new float[] { 1.0f, 0.0f, 0.5f };
    private final float[] torusColor = new float[] { 1.0f, 0.2f, 1.0f };
    private final float[] darkChessColor = new float[] { 0.2f, 0.2f, 1.0f };    // neon blue
    private final float[] lightChessColor = new float[] { 0.8f, 0.8f, 1.0f };   // light neon blue
    private final float[] lightColor = new float[] { 1.0f, 1.0f, 1.0f };

    public FiguresMode() {
        super();
        createScene();
        betaViewAngle = 75;
        alphaViewAngle = 0;

        cameraCoords = new float[]{ 0.0f, -6.0f, 2.0f };
        lightCoords = new float[]{ -0.3f, -1.2f, 0.5f };
    }

    @Override
    protected void createScene() {
        int n = 72;
        numVertexCylinder = n * 2 + 2;

        int dBSphere = 9, dL = 9;
        numVertexInPoleSphere = 360 / dL + 2;
        numVertexInRowSphere = 360 / dL * 2 + 2;
        numRowsBetweenPolesSphere = 180 / dBSphere - 2;
        int numVertexSphere = 2 * numVertexInPoleSphere + numRowsBetweenPolesSphere * numVertexInRowSphere;

        int dBTorus = 18;
        numVertexInRowTorus = 360 / dL * 2 + 2;
        numRowsTorus = 360 / dBTorus;
        int numVertexTorus = numRowsTorus * numVertexInRowTorus;

        numSquaresInChessboard = 10 * 10;
        numVertexChessboard = numSquaresInChessboard * 4;

        arrayVertex = new float[6 * (1 + 2 * numVertexCylinder + numVertexSphere + numVertexTorus + numVertexChessboard)];

        int pos = GraphicPrimitives.addVertexXYZn(arrayVertex, 0, 0, 0, 0, 1, 1, 1);

        float outerCylinderRadius = 0.2f,
                cylinderHeight = 1.5f,
                z = -0.25f;
        pos = GraphicPrimitives.addCylinderStrip(arrayVertex, pos, n, z, outerCylinderRadius, cylinderHeight, 1);

        float sphereRadius = 0.5f,
                xc = 0, yc = 0, zc = 0.5f;
        pos = GraphicPrimitives.addSphere(arrayVertex, pos, 360/dL, 180/dBSphere, sphereRadius, xc, yc, zc);

        float R = 1.0f, r = 0.15f;
        xc = 0;
        yc = 0;
        zc = 0.5f;
        pos = GraphicPrimitives.addTorus(arrayVertex, pos, 360/dL, 360/dBTorus, R, r, xc, yc, zc);

        float squareWidthChessboard = 1.15f * 2 / 10, xlChessboard = -1.15f, ybChessboard = -1.15f;
        z = -0.25f;
        GraphicPrimitives.addChessboard(arrayVertex, pos, xlChessboard, ybChessboard, z, squareWidthChessboard);
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
        int pos = 1;

        // Малювання циліндра
        GLES32.glUniform3fv(vMatrixHandle, 1, cylinderColor, 0);
        GLES32.glBindVertexArray(VAO_id);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_STRIP, pos, numVertexCylinder);
        pos += numVertexCylinder;

        // Малювання сфери
        GLES32.glUniform3fv(vMatrixHandle, 1, sphereColor, 0);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN, pos, numVertexInPoleSphere);
        pos += numVertexInPoleSphere;
        for (int i = 0; i < numRowsBetweenPolesSphere; i++) {
            GLES32.glDrawArrays(GLES32.GL_TRIANGLE_STRIP, pos, numVertexInRowSphere);
            pos += numVertexInRowSphere;
        }
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN, pos, numVertexInPoleSphere);
        pos += numVertexInPoleSphere;

        // Малювання тора
        GLES32.glUniform3fv(vMatrixHandle, 1, torusColor, 0);
        for (int i = 0; i < numRowsTorus; i++) {
            GLES32.glDrawArrays(GLES32.GL_TRIANGLE_STRIP, pos, numVertexInRowTorus);
            pos += numVertexInRowTorus;
        }

        // Малювання темних квадратів шахової дошки
        GLES32.glUniform3fv(vMatrixHandle, 1, darkChessColor, 0);
        for (int i = 0; i < numSquaresInChessboard / 2; i++) {
            GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN, pos, 4);
            pos += 4;
        }

        // Малювання світлих квадратів шахової дошки
        GLES32.glUniform3fv(vMatrixHandle, 1, lightChessColor, 0);
        for (int i = numSquaresInChessboard / 2; i < numSquaresInChessboard; i++) {
            GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN, pos, 4);
            pos += 4;
        }

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
                lightCoords[2] -= step;
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
