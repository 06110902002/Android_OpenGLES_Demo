package com.example.opengldemo1.utils;

import android.opengl.GLES20;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

public class ShaderHepler {

    private static final String TAG = "ShaderHepler";

    public static int compileVertexShader(String shaderCode){

        return compileShader(GL_VERTEX_SHADER,shaderCode);
    }

    public static int compileFragmentShader(String shaderCode){

        return compileShader(GL_FRAGMENT_SHADER,shaderCode);

    }

    private static int compileShader(int type,String shaderCode){
        final int shaderObjectId = glCreateShader(type);

        if(shaderObjectId == 0){
            System.out.println("26------ShaderHepler,could not create new shader");
            return 0;
        }

        //把着色器源代码上传到着色器对象里
        glShaderSource(shaderObjectId,shaderCode);

        //编译着色器
        glCompileShader(shaderObjectId);


        //取出编译状态
        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId,GL_COMPILE_STATUS,compileStatus,0);


        //读取 获取着色器信息的日志
        String logInfo = "43-------:Result of compile source:\n"+shaderCode + "\n:"+
                glGetShaderInfoLog(shaderObjectId);
        System.out.println(logInfo);


        if(compileStatus[0] == 0){
            glDeleteShader(shaderObjectId);
            System.out.println("51---------:Compile of shader failed");
            return 0;
        }


        return shaderObjectId;

//        //根据type创建顶点着色器或者片元着色器
//        int shader = GLES20.glCreateShader(type);
//        //将资源加入到着色器中，并编译
//        GLES20.glShaderSource(shader, shaderCode);
//        GLES20.glCompileShader(shader);
//        return shader;
    }

    /**
     * 将着色器链接进 OpenGl 程序，步骤：
     * 1.新建一个对象并附上着色器
     *
     * @param vertexShaderId
     * @param fragmentShaderId
     * @return
     */
    public static int linkProgram(int vertexShaderId,int fragmentShaderId){

        final int programObjectId = glCreateProgram();
        if(programObjectId == 0){
            System.out.println("73---------:could not create new program");
            return 0;
        }

        //将着色器附着对象上
        glAttachShader(programObjectId,vertexShaderId);
        glAttachShader(programObjectId,fragmentShaderId);

        //链接OpenGL 程序
        glLinkProgram(programObjectId);

        //检查链接状态
        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId,GL_LINK_STATUS,linkStatus,0);

        //打印链接程序的日志信息
        String linkLog = "Result of linking program:\n"+glGetProgramInfoLog(programObjectId);
        System.out.println("92---------:"+linkLog);

        //验证链接状态

        if(linkStatus[0] == 0){
            glDeleteProgram(programObjectId);
            System.out.println("101---------linking of program failed:");

            return 0;
        }

        return programObjectId;
    }

    public static boolean validateProgram(int programObjectId){
        glValidateProgram(programObjectId);
        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId,GL_VALIDATE_STATUS,validateStatus,0);

        String logInfo = "116-------:Result of validating program:"+validateStatus[0]+":\n"+
                ":log:"+glGetProgramInfoLog(programObjectId);
        System.out.println(logInfo);

        return validateStatus[0] != 0;
    }
}
