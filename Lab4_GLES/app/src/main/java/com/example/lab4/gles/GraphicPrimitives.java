package com.example.lab4.gles;

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
}
