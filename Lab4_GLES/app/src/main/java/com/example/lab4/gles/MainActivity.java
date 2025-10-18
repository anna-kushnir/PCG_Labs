package com.example.lab4.gles;

import android.content.Context;
import android.opengl.GLES32;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab4.gles.mode.DiffuseLightingMode;
import com.example.lab4.gles.mode.NineCubesMode;
import com.example.lab4.gles.mode.PyramidMode;
import com.example.lab4.gles.mode.SpecularLightingMode;
import com.example.lab4.gles.mode.WorkMode;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView gLView;
    private WorkMode wmRef = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gLView = new MyGLSurfaceView(this);
        setContentView(gLView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Diffuse Lighting");
        menu.add(0, 2, 0, "Specular Lighting");
        menu.add(0, 3, 0, "Pyramid");
        menu.add(0, 4, 0, "Nine Cubes");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setTitle(item.getTitle());
        switch (item.getItemId()) {
            case 1:
                modeStart(new DiffuseLightingMode(), GLSurfaceView.RENDERMODE_WHEN_DIRTY);
                return true;
            case 2:
                modeStart(new SpecularLightingMode(), GLSurfaceView.RENDERMODE_WHEN_DIRTY);
                return true;
            case 3:
                modeStart(new PyramidMode(), GLSurfaceView.RENDERMODE_CONTINUOUSLY);
                return true;
            case 4:
                modeStart(new NineCubesMode(), GLSurfaceView.RENDERMODE_WHEN_DIRTY);
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

    public class MyGLSurfaceView extends GLSurfaceView {
        public MyGLSurfaceView(Context context) {
            super(context);
            setEGLContextClientVersion(2);
            setRenderer(new MyGLRenderer());
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }

        @Override
        public boolean onTouchEvent(MotionEvent e) {
            if (wmRef == null) return false;
            if (wmRef.onTouchNotUsed()) return false;
            int cx = this.getWidth();
            int cy = this.getHeight();
            float xTouch = e.getX();
            float yTouch = e.getY();
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (wmRef.onActionDown(xTouch, yTouch, cx, cy))
                        requestRender();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (wmRef.onActionMove(xTouch, yTouch, cx, cy))
                        requestRender();
                    break;
                default: break;
            }
            return true;
        }
    }

    public class MyGLRenderer implements GLSurfaceView.Renderer {
        private int renderHeight = 1, renderWidth = 1;

        public void onSurfaceCreated(GL10 unused, EGLConfig config) {
            GLES32.glEnable(GLES32.GL_DEPTH_TEST);
            // Встановлення кольору фону (темно м'ятний)
            GLES32.glClearColor(0.0f, 0.4f, 0.4f, 1.0f);
        }

        public void onSurfaceChanged(GL10 unused, int width, int height) {
            GLES32.glViewport(0, 0, width, height);
            renderWidth = width;
            renderHeight = height;
        }

        public void onDrawFrame(GL10 unused) {
            GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT | GLES32.GL_DEPTH_BUFFER_BIT);
            if (wmRef == null) return;
            if (wmRef.getProgramId() < 0) return;
            if (wmRef.getProgramId() == 0)
                wmRef.createShaderProgram();
            if (wmRef.getProgramId() <= 0) return;

            GLES32.glUseProgram(wmRef.getProgramId());
            wmRef.useProgramForDrawing(renderWidth, renderHeight);
        }
    }
}