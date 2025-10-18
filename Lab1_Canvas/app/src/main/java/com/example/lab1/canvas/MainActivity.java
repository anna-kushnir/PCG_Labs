package com.example.lab1.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new myGraphicsView(this));
    }
    public static class myGraphicsView extends View {
        myGraphicsView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.argb(255, 192, 192, 192));
            Paint p = new Paint();
            p.setARGB(255, 255, 0, 127);
            p.setStyle(Paint.Style.FILL_AND_STROKE);
            p.setStrokeWidth(5);

            int N = 16;
            float xcSun = 500;
            float ycSun = 700;
            float radiusSun = 150;
            float radiusBeam = 300;
            drawSun(canvas, p, N, xcSun, ycSun, radiusSun, radiusBeam);

            float heightTriangle = 400;
            float widthTriangle = 400;
            drawTriangle(canvas, p,
                    xcSun + 40,
                    ycSun + radiusBeam + 80,
                    heightTriangle,
                    widthTriangle);
        }

        /** Малювання сонечка */
        private static void drawSun(Canvas canvas, Paint p, int N, float xcSun, float ycSun, float radiusSun, float radiusBeam) {
            Path pathSun = new Path();
            float xSunFirst = xcSun, ySunFirst = ycSun;

            for (int i = 0; i < N; i++) {
                float a = (2 * (float)Math.PI) / N * i;
                float xBeam = xcSun + radiusBeam * (float)Math.cos(a);
                float yBeam = ycSun + radiusBeam * (float)Math.sin(a);
                canvas.drawLine(xcSun, ycSun, xBeam, yBeam, p);
                float xSun = xcSun + radiusSun * (float)Math.cos(a);
                float ySun = ycSun + radiusSun * (float)Math.sin(a);
                if (i == 0) {
                    pathSun.moveTo(xSun, ySun);
                    xSunFirst = xSun;
                    ySunFirst = ySun;
                } else {
                    pathSun.lineTo(xSun, ySun);
                }
            }
            pathSun.lineTo(xSunFirst, ySunFirst);
            canvas.drawPath(pathSun, p);
            pathSun.close();
        }

        /** Малювання трикутника */
        @NonNull
        private static void drawTriangle(Canvas canvas, Paint p, float xlTriangle, float ycTriangle, float heightTriangle, float widthTriangle) {
            float ylTriangle = ycTriangle + heightTriangle;
            Path pathTriangle = new Path();
            pathTriangle.moveTo(xlTriangle, ylTriangle);
            pathTriangle.lineTo(xlTriangle + widthTriangle, ylTriangle);
            pathTriangle.lineTo(xlTriangle + widthTriangle / 2, ycTriangle);
            pathTriangle.lineTo(xlTriangle, ylTriangle);
            canvas.drawPath(pathTriangle, p);
            pathTriangle.close();
        }
    }
}