attribute vec4 a_Position; //顶点为4维

void main(){

    gl_Position = a_Position;
    gl_PointSize = 10.0;    //指定点的粗细

}

