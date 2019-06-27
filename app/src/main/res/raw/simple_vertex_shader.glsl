attribute vec4 a_Position; //顶点为4维 坐标属性
attribute vec4 a_Color;     //顶点颜色 属性
varying vec4 v_Color;

void main(){

    gl_Position = a_Position;
    v_Color = a_Color;
    gl_PointSize = 10.0;    //指定点的粗细

}

