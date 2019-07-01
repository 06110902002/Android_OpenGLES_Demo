package com.example.opengldemo1.data;

import android.opengl.GLES20;

import com.example.opengldemo1.program.TextureShaderProgram;

public class Table {

    private static final int POSITION_COMPONENT_COUNT = 2;

    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;

    private static final int STRIDE = (POSITION_COMPONENT_COUNT +
            TEXTURE_COORDINATES_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;

    private VertexArray vertexArray;

    private static final float[] VERTEX_DATA = {
            //Order of coordinates: X,Y,S,T 即坐标与纹理
            // Triangle Fan
            0f,    0f, 0.5f, 0.5f,
            -0.5f, -0.8f,   0f, 0.9f,
            0.5f, -0.8f,   1f, 0.9f,
            0.5f,  0.8f,   1f, 0.1f,
            -0.5f,  0.8f,   0f, 0.1f,
            -0.5f, -0.8f,   0f, 0.9f

    };

    public Table(){
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(TextureShaderProgram textureProgram){

        //从着色器获取每个属性的位置，并通过getPositionAttributeLocation绑定
        //到被引用的着色器属性上
        vertexArray.setVertexAttribPointer(
                0,
                textureProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);
        //从着色器获取每个属性的位置，并通过getTextureCoordinatesAttributeLocation
        // 把纹理坐标数据绑定到着色器属性上
        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                textureProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);

    }

    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);
    }
}
