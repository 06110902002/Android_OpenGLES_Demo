package com.example.opengldemo1.render;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.example.opengldemo1.R;
import com.example.opengldemo1.utils.ShaderHepler;
import com.example.opengldemo1.utils.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private Context context;
    private static final int POSITION_COMPONET_COUNT = 2;



    //为了方便在native 层读取java层的数据（即Dalvik层的数据）
    //需要将变量进行一次转换
    private static final int BYTES_PER_FLOAT = 4; //java float 有32位精度所以此处定义为4
    private  FloatBuffer vertexData;
    private int program;

    //片段着色器颜色
    private static final String U_COLOR = "u_Color";
    private int uColorLocation;

    private static final String A_COLOR = "a_Color";
    private static final int COLOR_COMPOENT_COUNT = 3;//每个颜色为3维，即R, G, B,在下面的的数组中需要取3个数据;

    //跨距，告诉OpenGL 每个位置之前有多少个字节，即顶点+颜色
    private static final int STRIDE = (POSITION_COMPONET_COUNT + COLOR_COMPOENT_COUNT) * BYTES_PER_FLOAT;

    private int aColorLocation;


    //获取属性的位置
    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;

    private static final String U_MATRIX = "u_Matrix"; //读取shader中矩阵值
    private final float[] projectionMatrix = new float[16];
    //保存矩阵的uniform值
    private int uMatrixLocation;


    float aspectRation;

    public AirHockeyRenderer(Context context){
        this.context = context;

        //桌子为二维结构，所以一个长方形的4个顶点中明天下午回顶点坐标为二维的
        float[] tableVertices = {
                0f,0f,
                0f,14f,
                9f,14f,
                9f,0f
        };

        //桌子长方形使用三角形表示
        float[] tableVerticesWithTriangles = {
                // Order of coordinates X, Y,R,G,B

                //Triangle 1
                0f,        0f,      1f,     1f,   1f,
                -0.5f,  -0.5f,      0.7f, 0.7f, 0.7f,
                0.5f,   -0.5f,      0.7f, 0.7f, 0.7f,
                0.5f,    0.5f,      0.7f, 0.7f, 0.7f,
                -0.5f,   0.5f,      0.7f, 0.7f, 0.7f,
                -0.5f,  -0.5f,      0.7f, 0.7f, 0.7f,

                // 一条线的4个坐标
                -0.5f,0f,           1f,     0f,   0f,
                0.5f,0f,            0f,     0.23f,0.5f,

                //两个木锤 的坐标
                0f,-0.25f,          0f,     0.23f,   1f,
                0f,0.25f,           1f,     0f,   0f

        };

        //转换java 层的float数据给native层使用
        vertexData = ByteBuffer.allocateDirect(
                tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        vertexData.put(tableVerticesWithTriangles);
    }


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        //1.读取着色器代码
        String vertexShaderSource = TextResourceReader.readTextFileFromResource(this.context, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(this.context, R.raw.simple_fragment_shader);
        //2.编译着色器代码
        int vertexShader = ShaderHepler.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHepler.compileFragmentShader(fragmentShaderSource);


        //把着色器一起链接进OpenGL 程序
        program = ShaderHepler.linkProgram(vertexShader,fragmentShader);


        //验证下 链接状态，代码为查看日志使用
        ShaderHepler.validateProgram(program);


        //使用程序
        GLES20.glUseProgram(program);


        //获取unifrom 的位置,有了这个位置之后，就能告诉OpenGL 至哪里去找这个属性对应的数据了
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR);
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);

        //获取矩阵的uniform值
        uMatrixLocation = GLES20.glGetUniformLocation(program,U_MATRIX);

        //下一步告诉OpenGl 到哪里找到 a_Position对应的数据,
        //找到之后，将顶点数据与属性关联起来,从颜色处开始读取
        vertexData.position(0);

        //此处有告诉每个顶点包含多少个分量 ，分量值由POSITION_COMPONET_COUNT指定
        GLES20.glVertexAttribPointer(aPositionLocation,POSITION_COMPONET_COUNT,GLES20.GL_FLOAT,false,STRIDE,vertexData);

        GLES20.glEnableVertexAttribArray(aPositionLocation);

        //移到下一个颜色起始点
        vertexData.position(POSITION_COMPONET_COUNT);
        //颜色分量
        GLES20.glVertexAttribPointer(aColorLocation,COLOR_COMPOENT_COUNT,GLES20.GL_FLOAT,false,STRIDE,vertexData);


        //开启顶点数据
        GLES20.glEnableVertexAttribArray(aColorLocation);


    }


    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0,0,width,height);

        //创建一个正交投影
        aspectRation = width > height ? (float) width / (float) height :
                (float) height / (float) width ;

        if(width > height){
            //横屏模式
            Matrix.orthoM(projectionMatrix,0,-aspectRation,aspectRation,-1f,1f,-1f,1f);
        }else{
            Matrix.orthoM(projectionMatrix,0,-1f,1f,-aspectRation,aspectRation,-1f,1f);

        }
    }

    /**
     * 更新投影矩阵，来控制页面的位移
     * @param top
     * @param bottom
     */
    public void updateMatriPos(float top,float bottom){

        Matrix.orthoM(projectionMatrix,0,-1f,1f,-aspectRation + bottom,aspectRation,-1f,1f);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        //清空 rendering surface
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //传递正交投影矩阵
        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false,projectionMatrix,0);

        //更新着色器中u_Color 值，注意颜色值为4个分量即，r,g,b,a
        //GLES20.glUniform4f(uColorLocation,1.0f,1.0f,1.0f,1.0f);

        //使用三角形扇画三角形，从数据数组开头读取6个顶点，代表画2个三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,6);


        //绘制 分隔线
        //GLES20.glUniform4f(uColorLocation,1.0f,0.0f,0.0f,1.0f);
        GLES20.glDrawArrays(GLES20.GL_LINES,6,2);

        //绘制木锤 1
        //GLES20.glUniform4f(uColorLocation,0.0f,0.0f,1.0f,1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS,8,1);

        //绘制木锤  2
        //GLES20.glUniform4f(uColorLocation,1.0f,0.0f,0.0f,1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS,9,1);


    }
    //第一阶段完成：实现OpenGL 绘制的基本流程（1.初始化GLSurfaceView2.添加渲染器3、加载shaderr并编译shader）


}
