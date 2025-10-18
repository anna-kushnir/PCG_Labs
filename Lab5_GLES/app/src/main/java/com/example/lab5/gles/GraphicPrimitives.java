package com.example.lab5.gles;

public class GraphicPrimitives {

    public static int addVertexXYZn(float[] arrayVertex, int pos,
                                    float x, float y, float z,
                                    float xn, float yn, float zn) {
        float N = (float)Math.sqrt(xn*xn + yn*yn + zn*zn);
        if (N <= 0) return pos;
        arrayVertex[pos++] = x;
        arrayVertex[pos++] = y;
        arrayVertex[pos++] = z;
        arrayVertex[pos++] = xn / N;
        arrayVertex[pos++] = yn / N;
        arrayVertex[pos++] = zn / N;
        return pos;
    }

    /// For GLES32.GL_TRIANGLES configuration
    public static int addTriangleXYZn(float[] arrayVertex, int pos,
                                      float x1, float y1, float z1,
                                      float x2, float y2, float z2,
                                      float x3, float y3, float z3) {
        float xn = (z2-z1)*(y3-y1) - (y2-y1)*(z3-z1);
        float yn = (x2-x1)*(z3-z1) - (z2-z1)*(x3-x1);
        float zn = (y2-y1)*(x3-x1) - (x2-x1)*(y3-y1);
        pos = addVertexXYZn(arrayVertex, pos, x1, y1, z1, xn, yn, zn);
        pos = addVertexXYZn(arrayVertex, pos, x2, y2, z2, xn, yn, zn);
        pos = addVertexXYZn(arrayVertex, pos, x3, y3, z3, xn, yn, zn);
        return pos;
    }

    /// Only for GLES32.GL_TRIANGLE_FAN configuration
    public static int addRectangleXYZn(float[] arrayVertex, int pos,
                                       float x1, float y1, float z1,
                                       float x2, float y2, float z2,
                                       float x3, float y3, float z3,
                                       float x4, float y4, float z4) {
        float xn = (z2-z1)*(y3-y1) - (y2-y1)*(z3-z1);
        float yn = (x2-x1)*(z3-z1) - (z2-z1)*(x3-x1);
        float zn = (y2-y1)*(x3-x1) - (x2-x1)*(y3-y1);
        pos = addVertexXYZn(arrayVertex, pos, x1, y1, z1, xn, yn, zn);
        pos = addVertexXYZn(arrayVertex, pos, x2, y2, z2, xn, yn, zn);
        pos = addVertexXYZn(arrayVertex, pos, x3, y3, z3, xn, yn, zn);
        pos = addVertexXYZn(arrayVertex, pos, x4, y4, z4, xn, yn, zn);
        return pos;
    }

    /// Only for GLES32.GL_TRIANGLE_FAN configuration
    public static int addSquareXYZn(float[] arrayVertex, int pos,
                                    float xl, float yb, float z,
                                    float width) {
        float xr = xl + width;
        float yt = yb + width;
        float xn = 0.0f;
        float yn = 0.0f;
        float zn = (yt-yb)*(xr-xl);
        pos = addVertexXYZn(arrayVertex, pos, xl, yb, z, xn, yn, zn);
        pos = addVertexXYZn(arrayVertex, pos, xl, yt, z, xn, yn, zn);
        pos = addVertexXYZn(arrayVertex, pos, xr, yt, z, xn, yn, zn);
        pos = addVertexXYZn(arrayVertex, pos, xr, yb, z, xn, yn, zn);
        return pos;
    }

    /// Draws all dark squares first, and then all light squares
    public static int addChessboard(float[] arrayVertex, int pos,
                                    float xlChessboard, float ybChessboard, float z,
                                    float squareWidth) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if ((i + j) % 2 == 0) {
                    float xl = xlChessboard + squareWidth * i;
                    float xr = xl + squareWidth;
                    float yb = ybChessboard + squareWidth * j;
                    float yt = yb + squareWidth;
                    pos = addRectangleXYZn(arrayVertex, pos,
                            xl, yb, z,
                            xl, yt, z,
                            xr, yt, z,
                            xr, yb, z);
                }
            }
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if ((i + j) % 2 == 1) {
                    float xl = xlChessboard + squareWidth * i;
                    float xr = xl + squareWidth;
                    float yb = ybChessboard + squareWidth * j;
                    float yt = yb + squareWidth;
                    pos = addRectangleXYZn(arrayVertex, pos,
                            xl, yb, z,
                            xl, yt, z,
                            xr, yt, z,
                            xr, yb, z);
                }
            }
        }
        return pos;
    }

    public static int addPyramid(float[] arrayVertex, int pos,
                                 float xl, float yb, float zb,
                                 float width, float height) {
        float xr = xl + width;
        float yt = yb + width;
        float xc = (xl + xr) / 2;
        float yc = (yb + yt) / 2;
        float zc = zb + height;
        pos = addTriangleXYZn(arrayVertex, pos,
                xl, yb, zb,
                xc, yc, zc,
                xr, yb, zb);
        pos = addTriangleXYZn(arrayVertex, pos,
                xr, yb, zb,
                xc, yc, zc,
                xr, yt, zb);
        pos = addTriangleXYZn(arrayVertex, pos,
                xr, yt, zb,
                xc, yc, zc,
                xl, yt, zb);
        pos = addTriangleXYZn(arrayVertex, pos,
                xl, yt, zb,
                xc, yc, zc,
                xl, yb, zb);
        return pos;
    }

    public static int addCube(float[] arrayVertex, int pos,
                              float xl, float yb, float zb,
                              float width) {
        float xr = xl + width;
        float yt = yb + width;
        float zt = zb + width;
        pos = addRectangleXYZn(arrayVertex, pos,    /// bottom
                xl, yb, zb,     xr, yb, zb,
                xr, yt, zb,     xl, yt, zb);
        pos = addRectangleXYZn(arrayVertex, pos,    /// back
                xl, yt, zb,     xr, yt, zb,
                xr, yt, zt,     xl, yt, zt);
        pos = addRectangleXYZn(arrayVertex, pos,    /// left
                xl, yb, zb,     xl, yt, zb,
                xl, yt, zt,     xl, yb, zt);
        pos = addRectangleXYZn(arrayVertex, pos,    /// right
                xr, yt, zb,     xr, yb, zb,
                xr, yb, zt,     xr, yt, zt);
        pos = addRectangleXYZn(arrayVertex, pos,    /// front
                xl, yb, zb,     xl, yb, zt,
                xr, yb, zt,     xr, yb, zb);
        pos = addRectangleXYZn(arrayVertex, pos,    /// top
                xl, yb, zt,     xl, yt, zt,
                xr, yt, zt,     xr, yb, zt);
        return pos;
    }

    public static int addCubesNet(float[] arrayVertex, int pos,
                                  float xlBoard, float ybBoard, float zbBoard,
                                  float boardWidth) {
        float cubeWidth = boardWidth / 5;
        for (int lvl = 0; lvl < 3; lvl++) {
            float zb = zbBoard + lvl * cubeWidth * 2;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    float xl = xlBoard + i * cubeWidth * 2;
                    float yb = ybBoard + j * cubeWidth * 2;
                    pos = addCube(arrayVertex, pos, xl, yb, zb, cubeWidth);
                }
            }
        }
        return pos;
    }


    /**
     * @param isOuter equals 1 if cylinder is outer, else -1 (is inner)
     * @param n defines number of parts in which circle is divided
     */
    public static int addCylinderStrip(float[] arrayVertex, int pos, int n,
                                  float z1,
                                  float radius, float height,
                                  int isOuter) {
        float a, x, y,
                z2 = z1 + height;
        float xn, yn;
        for (int i = 0; i <= n; i++) {
            a = 360.0f / n * i;
            x = radius * (float)Math.cos(Math.toRadians(a));
            y = radius * (float)Math.sin(Math.toRadians(a));
            xn = x * isOuter;
            yn = y * isOuter;
            pos = addVertexXYZn(arrayVertex, pos,
                    x, y, z1, xn, yn, 0.0f);
            pos = addVertexXYZn(arrayVertex, pos,
                    x, y, z2, xn, yn, 0.0f);
        }
        return pos;
    }

    public static int addCylinderFan(float[] arrayVertex, int pos, int n,
                                       float z1,
                                       float radius, float height) {
        float a, x1, y1, x2, y2,
                z2 = z1 + height;
        for (int i = 0; i < n; i++) {
            a = 360.0f / n * i;
            x1 = radius * (float)Math.cos(Math.toRadians(a));
            y1 = radius * (float)Math.sin(Math.toRadians(a));
            a = 360.0f / n * (i + 1);
            x2 = radius * (float)Math.cos(Math.toRadians(a));
            y2 = radius * (float)Math.sin(Math.toRadians(a));
            pos = addRectangleXYZn(arrayVertex, pos,
                    x1, y1, z1,
                    x1, y1, z2,
                    x2, y2, z2,
                    x2, y2, z1);
        }
        return pos;
    }

    public static int addSphere(float[] arrayVertex, int pos,
                                int numL, int numB, float radius,
                                float xc, float yc, float zc) {
        int i, j;
        float x0, x1, y0, y1, z0, z1;
        double L, B0, B1;
        /// Південний полюс, від якого будуються трикутники в режимі GL_TRIANGLE_FAN
        x0 = y0 = xc;
        z0 = -radius;
        pos = addVertexXYZn(arrayVertex, pos, x0 + xc, y0 + yc, z0 + zc, x0, y0, z0);
        B0 = Math.toRadians(180f / numB - 90);
        z0 = radius * (float) Math.sin(B0);
        for (i = 0; i <= numL; i++) {
            L = Math.toRadians(360f / numL * i);
            x0 = radius * (float) (Math.cos(B0) * Math.cos(L));
            y0 = radius * (float) (Math.cos(B0) * Math.sin(L));
            pos = addVertexXYZn(arrayVertex, pos, x0 + xc, y0 + yc, z0 + zc, x0, y0, z0);
        }
        /// Рядки квадратів, які будуються в режимі GL_TRIANGLE_STRIP
        for (j = 2; j < numB; j++) {
            B1 = Math.toRadians(180f / numB * j - 90);
            z0 = radius * (float) Math.sin(B0);
            z1 = radius * (float) Math.sin(B1);
            for (i = 0; i <= numL; i++) {
                L = Math.toRadians(360f / numL * i);
                x0 = radius * (float) (Math.cos(B0) * Math.cos(L));
                y0 = radius * (float) (Math.cos(B0) * Math.sin(L));
                x1 = radius * (float) (Math.cos(B1) * Math.cos(L));
                y1 = radius * (float) (Math.cos(B1) * Math.sin(L));
                pos = addVertexXYZn(arrayVertex, pos, x0 + xc, y0 + yc, z0 + zc, x0, y0, z0);
                pos = addVertexXYZn(arrayVertex, pos, x1 + xc, y1 + yc, z1 + zc, x1, y1, z1);
            }
            B0 = B1;
        }
        /// Північний полюс, від якого будуються трикутники в режимі GL_TRIANGLE_FAN
        x0 = y0 = 0;
        z0 = radius;
        pos = addVertexXYZn(arrayVertex, pos, x0 + xc, y0 + yc, z0 + zc, x0, y0, z0);
        B0 = Math.toRadians(90 - 180f / numB);
        z0 = radius * (float) Math.sin(B0);
        for (i = 0; i <= numL; i++) {
            L = Math.toRadians(360f / numL * i);
            x0 = radius * (float) (Math.cos(B0) * Math.cos(L));
            y0 = radius * (float) (Math.cos(B0) * Math.sin(L));
            pos = addVertexXYZn(arrayVertex, pos, x0 + xc, y0 + yc, z0 + zc, x0, y0, z0);
        }
        return pos;
    }

    public static int addSphere2(float[] arrayVertex, int pos,
                                int dL_grad, int dB_grad, float radius) {
        float dL = (float)Math.toRadians(dL_grad);
        float dB = (float)Math.toRadians(dB_grad);
        float x0, x1, y0, y1, z0, z1,
                B0, B1, L;
        float x0Start = 0, y0Start = 0, x1Start = 0, y1Start = 0;

        /// Південний полюс, від якого будуються трикутники в режимі GL_TRIANGLE_FAN
        x0 = y0 = 0;
        z0 = -radius;
        pos = addVertexXYZn(arrayVertex, pos, x0, y0, z0, x0, y0, z0);
        B0 = -(float)Math.PI / 2 + dB;
        z0 = radius * (float) Math.sin(B0);
        for (L = 0; L < 2 * Math.PI; L += dL) {
            x0 = radius * (float) (Math.cos(B0) * Math.cos(L));
            y0 = radius * (float) (Math.cos(B0) * Math.sin(L));
            if (L == 0) {
                x0Start = x0;
                y0Start = y0;
            }
            pos = addVertexXYZn(arrayVertex, pos, x0, y0, z0, x0, y0, z0);
        }
        pos = addVertexXYZn(arrayVertex, pos, x0Start, y0Start, z0, x0Start, y0Start, z0);
        /// Рядки квадратів, які будуються в режимі GL_TRIANGLE_STRIP
        for (B1 = B0 + dB; B1 < (float)Math.PI / 2; B1 += dB) {
            z0 = radius * (float) Math.sin(B0);
            z1 = radius * (float) Math.sin(B1);
            for (L = 0; L < 2 * Math.PI; L += dL) {
                x0 = radius * (float) (Math.cos(B0) * Math.cos(L));
                y0 = radius * (float) (Math.cos(B0) * Math.sin(L));
                x1 = radius * (float) (Math.cos(B1) * Math.cos(L));
                y1 = radius * (float) (Math.cos(B1) * Math.sin(L));
                if (L == 0) {
                    x0Start = x0;
                    y0Start = y0;
                    x1Start = x1;
                    y1Start = y1;
                }
                pos = addVertexXYZn(arrayVertex, pos, x0, y0, z0, x0, y0, z0);
                pos = addVertexXYZn(arrayVertex, pos, x1, y1, z1, x1, y1, z1);
            }
            pos = addVertexXYZn(arrayVertex, pos, x0Start, y0Start, z0, x0Start, y0Start, z0);
            pos = addVertexXYZn(arrayVertex, pos, x1Start, y1Start, z1, x1Start, y1Start, z1);
            B0 = B1;
        }
        /// Північний полюс, від якого будуються трикутники в режимі GL_TRIANGLE_FAN
        x0 = y0 = 0;
        z0 = radius;
        pos = addVertexXYZn(arrayVertex, pos, x0, y0, z0, x0, y0, z0);
        B0 = (float)Math.PI / 2 - dB;
        z0 = radius * (float) Math.sin(B0);
        for (L = 0; L <= 2 * Math.PI; L += dL) {
            x0 = radius * (float) (Math.cos(B0) * Math.cos(L));
            y0 = radius * (float) (Math.cos(B0) * Math.sin(L));
            if (L == 0) {
                x0Start = x0;
                y0Start = y0;
            }
            pos = addVertexXYZn(arrayVertex, pos, x0, y0, z0, x0, y0, z0);
        }
        pos = addVertexXYZn(arrayVertex, pos, x0Start, y0Start, z0, x0Start, y0Start, z0);
        return pos;
    }

    public static int addTorus(float[] arrayVertex, int pos,
                                int numL, int numB,
                               float R, float r,
                               float xc, float yc, float zc) {
        float x0, x1, y0, y1, z0, z1,
                xn0, xn1, yn0, yn1, zn0, zn1;
        double L, B0, B1;
        /// Рядки квадратів, які будуються в режимі GL_TRIANGLE_STRIP
        B0 = 0;
        for (int j = 1; j <= numB; j++) {
            B1 = Math.toRadians(360f / numB * j);
            z0 = r * (float) Math.sin(B0) + zc;
            z1 = r * (float) Math.sin(B1) + zc;
            zn0 = (float) Math.sin(B0);
            zn1 = (float) Math.sin(B1);
            for (int i = 0; i <= numL; i++) {
                L = Math.toRadians(360f / numL * i);
                x0 = (R + r * (float) Math.cos(B0)) * (float) Math.cos(L) + xc;
                y0 = (R + r * (float) Math.cos(B0)) * (float) Math.sin(L) + yc;
                x1 = (R + r * (float) Math.cos(B1)) * (float) Math.cos(L) + xc;
                y1 = (R + r * (float) Math.cos(B1)) * (float) Math.sin(L) + yc;

                xn0 = (float) (Math.cos(B0) * Math.cos(L));
                yn0 = (float) (Math.cos(B0) * Math.sin(L));
                xn1 = (float) (Math.cos(B1) * Math.cos(L));
                yn1 = (float) (Math.cos(B1) * Math.sin(L));
                pos = addVertexXYZn(arrayVertex, pos, x0, y0, z0, xn0, yn0, zn0);
                pos = addVertexXYZn(arrayVertex, pos, x1, y1, z1, xn1, yn1, zn1);
            }
            B0 = B1;
        }
        return pos;
    }
}
