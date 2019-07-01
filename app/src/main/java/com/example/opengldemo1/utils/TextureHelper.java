package com.example.opengldemo1.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import static android.opengl.GLES20.GL_TEXTURE;
import static android.opengl.GLES20.GL_TEXTURE_2D;

public class TextureHelper {

    public static int loadTexture(Context context,int resourceId){
        final int[] textureObjectIds = new int[1];
        GLES20.glGenTextures(1,textureObjectIds,0);
        if(textureObjectIds[0] == 0){
            System.out.println("12------------:Could not generate a new ");
            return 0;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),resourceId);
        if(bitmap == null){
            GLES20.glDeleteTextures(1,textureObjectIds,0);
            return 0;
        }
        GLES20.glBindTexture(GL_TEXTURE_2D,textureObjectIds[0]);

        //指定绘制图像缩小时纹理过滤方式
        GLES20.glTexParameteri(GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR_MIPMAP_LINEAR);

        //指定绘制图像放大时纹理过滤方式
        GLES20.glTexParameteri(GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);


        GLUtils.texImage2D(GL_TEXTURE_2D,0,bitmap,0);

        bitmap.recycle();

        //生成MIP 纹理贴图
        GLES20.glGenerateMipmap(GL_TEXTURE_2D);

        //解除与纹理的绑定
        GLES20.glBindTexture(GL_TEXTURE_2D,0);

        return textureObjectIds[0];

    }
}
