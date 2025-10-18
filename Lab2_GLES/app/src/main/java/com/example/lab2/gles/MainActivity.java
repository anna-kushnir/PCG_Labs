package com.example.lab2.gles;

import android.content.Context;
import android.opengl.GLES32;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView gLView;
    private WorkMode wmRef = null;

    protected static int renderHeight = 1;
    protected static int renderWidth = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gLView = new MyGLSurfaceView(this);
        setContentView(gLView);
    }

    public class MyGLSurfaceView extends GLSurfaceView {
        public MyGLSurfaceView(Context context) {
            super(context);
            setEGLContextClientVersion(2);
            setRenderer(new MyGLRenderer());
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }
    }

    public class MyGLRenderer implements GLSurfaceView.Renderer {

        public void onSurfaceCreated(GL10 unused, EGLConfig config) {
            // Встановлення кольору фону
            GLES32.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        }

        public void onSurfaceChanged(GL10 unused, int width, int height) {
            GLES32.glViewport(0, 0, width, height);
            renderWidth = width;
            renderHeight = height;
        }

        public void onDrawFrame(GL10 unused) {
            GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT);
            if (wmRef == null)
                return;
            if (wmRef.getProgramId() == 0)
                wmRef.createShaderProgram();
            if (wmRef.getProgramId() == 0)
                return;

            GLES32.glUseProgram(wmRef.getProgramId());
            wmRef.useProgramForDrawing();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Color Wheel");
        menu.add(0, 2, 0, "Color Wheel Animation");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setTitle(item.getTitle());
        switch (item.getItemId()) {
            case 1:
                modeStart(new FiguresWorkMode(renderWidth, renderHeight), GLSurfaceView.RENDERMODE_WHEN_DIRTY);
                return true;
            case 2:
                modeStart(new ColorAnimationWorkMode(renderWidth, renderHeight), GLSurfaceView.RENDERMODE_CONTINUOUSLY);
                return true;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void modeStart(WorkMode wMode, int renderMode) {
        wmRef = wMode;
        gLView.setRenderMode(renderMode);
        gLView.requestRender();
    }
}