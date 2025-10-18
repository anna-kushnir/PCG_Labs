package com.example.lab2.gles;

import android.opengl.GLES32;
import android.os.SystemClock;
public class ColorAnimationWorkMode extends FiguresWorkMode {
    public ColorAnimationWorkMode(int width, int height) {
        super(width, height);
    }

    @Override
    public void useProgramForDrawing() {
        long time = SystemClock.uptimeMillis() % 2000L;
        float vLight;
        if (time <= 1000)
            vLight = time;
        else vLight = 1999 - time;
        
        int colorHandle = GLES32.glGetUniformLocation(gl_Program, "vLight");
        GLES32.glUniform1f(colorHandle, 1.0f);
        GLES32.glBindVertexArray(VAO_id);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_STRIP, 0, numVertexRainbow);
        GLES32.glBindVertexArray(0);

        colorHandle = GLES32.glGetUniformLocation(gl_Program, "vLight");
        GLES32.glUniform1f(colorHandle, 0.001f * vLight);
        GLES32.glBindVertexArray(VAO_id);
        GLES32.glDrawArrays(GLES32.GL_TRIANGLE_FAN, numVertexRainbow, numVertexPolygon);
        GLES32.glBindVertexArray(0);
    }
}
