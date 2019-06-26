package com.example.opengldemo1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.opengldemo1.render.AirHockeyRenderer;

public class AirHockeyActivity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    private boolean renderSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //glSurfaceView = findViewById(R.id.glSurfaceView);
        glSurfaceView = new GLSurfaceView(this);

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
            glSurfaceView.setRenderer(new AirHockeyRenderer(this));
            renderSet = true;
        }
        else{
            Toast.makeText(this,"设备不支持Open GL",Toast.LENGTH_SHORT).show();
        }

        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        setContentView(glSurfaceView);
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
