package com.example.opengldemo1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.opengldemo1.render.AirHockeyRenderer;

public class AirHockeyActivity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    private boolean renderSet = false;
    private AirHockeyRenderer airHockeyRenderer;

    private Button btnLeft;
    private Button btnRight;
    private Button btnNer;
    private Button btnFa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        glSurfaceView = findViewById(R.id.glSurfaceView);
        //glSurfaceView = new GLSurfaceView(this);

        ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        boolean supportEs2 = configurationInfo.reqGlEsVersion >= 0x2000 ||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                        && Build.FINGERPRINT.startsWith("generic")
                ||Build.FINGERPRINT.startsWith("unknown")
                ||Build.MODEL.contains("google_sdk")
                ||Build.MODEL.contains("Emulator")
                ||Build.MODEL.contains("Android SDK built for x86"));

        if(supportEs2){
            Toast.makeText(this,"设备支持Open GL",Toast.LENGTH_SHORT).show();
            glSurfaceView.setEGLContextClientVersion(2);

            //设置渲染器
            airHockeyRenderer = new AirHockeyRenderer(this);
            glSurfaceView.setRenderer(airHockeyRenderer);
            renderSet = true;
        }
        else{
            Toast.makeText(this,"设备不支持Open GL",Toast.LENGTH_SHORT).show();
        }

        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        initControlPanel();

    }

    private void initControlPanel(){
        btnLeft = findViewById(R.id.btn_left);
        btnRight = findViewById(R.id.btn_right);
        btnNer = findViewById(R.id.btn_ner);
        btnFa = findViewById(R.id.btn_fa);

        EventClick click = new EventClick();
        btnLeft.setOnClickListener(click);
        btnRight.setOnClickListener(click);
        btnFa.setOnClickListener(click);
        btnNer.setOnClickListener(click);

    }

    float bottom = 0.1f;
    private class EventClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            switch(view.getId()){
                case R.id.btn_left:
                    bottom += 0.1f;
                    airHockeyRenderer.updateMatriPos(0f,bottom);
                    glSurfaceView.requestRender();

                    break;
                case R.id.btn_right:
                    bottom -= 0.1f;
                    airHockeyRenderer.updateMatriPos(0f,bottom);
                    glSurfaceView.requestRender();
                    break;

                case R.id.btn_fa:
                    break;

                case R.id.btn_ner:
                    break;
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(renderSet){
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(renderSet){
            glSurfaceView.onResume();
        }
    }
}
