precision mediump float;

varying vec4 v_Color;   //接收顶点的混合颜色，之后再传给片段着色器

void main(){

    gl_FragColor = v_Color;
}