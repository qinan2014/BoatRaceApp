package com.bn.st.xc;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * Created by Boboqi on 2017-9-19.
 * 绘制圆柱，用光照和颜色
 */

public class CylinderTextureByVertex {
    int mProgram;  // 自定义渲染管线id
    int muMVPMatrixHandle;
    int maPositionHandle;
    int maNormalHandle;
    int muMMatrixHandle;
    int maLightLocationHandle;
    int maCameraHandle;

    int maColorR;
    int maColorG;
    int maColorB;
    int maColorA;

    String mVertexShader;
    String mFragmentShader;

    private static FloatBuffer mVertexBuffer;
    private static FloatBuffer mNormalBuffer;
    static int vCount = 0;

    float r;
    float g;
    float b;

    public CylinderTextureByVertex(int mProgramIn, float radius, float length, float aspan, float lspan, float[] color)
    {
        this.r = color[0];
        this.g = color[1];
        this.b = color[2];
        initShader(mProgramIn);
    }

    public void initShader(int mProgramIn)
    {
        mProgram = mProgramIn;
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        maNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
        maLightLocationHandle = GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        maCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");

        maColorR = GLES20.glGetUniformLocation(mProgram, "colorR");
        maColorG = GLES20.glGetUniformLocation(mProgram, "colorG");
        maColorB = GLES20.glGetUniformLocation(mProgram, "colorB");
        maColorA = GLES20.glGetUniformLocation(mProgram, "colorA");
    }
    // r 圆柱半径，length圆柱长度, aspan 切分角度， lspan 切分长度
    public static void initVertexData(float r, float length, float aspan, float lspan)
    {
        //顶点坐标数据的初始化================begin============================
        //获取切分整图的纹理数组
        ArrayList<Float> alVertix = new ArrayList<Float>();//存放顶点坐标的ArrayList
        for (float tempY = length / 2; tempY > - length / 2; tempY = tempY - lspan)
        {
            for (float hAngle = 360; hAngle > 0; hAngle = hAngle - aspan)
            {
                //纵向横向各到一个角度后计算对应的此点在球面上的四边形顶点坐标
                //并构建两个组成四边形的三角形

                float x1=(float)(r*Math.cos(Math.toRadians(hAngle)));
                float z1=(float)(r*Math.sin(Math.toRadians(hAngle)));
                float y1=tempY;
                float x2=(float)(r*Math.cos(Math.toRadians(hAngle)));
                float z2=(float)(r*Math.sin(Math.toRadians(hAngle)));
                float y2=tempY-lspan;

                float x3=(float)(r*Math.cos(Math.toRadians(hAngle-aspan)));
                float z3=(float)(r*Math.sin(Math.toRadians(hAngle-aspan)));
                float y3=tempY-lspan;

                float x4=(float)(r*Math.cos(Math.toRadians(hAngle-aspan)));
                float z4=(float)(r*Math.sin(Math.toRadians(hAngle-aspan)));
                float y4=tempY;

                //构建第一三角形
                alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
                alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
                alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);
                //构建第二三角形
                alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);
                alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
                alVertix.add(x3);alVertix.add(y3);alVertix.add(z3);
            }
        }
        vCount = alVertix.size() / 3;
        // 将alVertix中的坐标值转存到一个int数组中
        float vertices[] = new float[vCount * 3];
        for (int i = 0; i < alVertix.size(); ++i)
        {
            vertices[i] = alVertix.get(i);
        }
        // 创建顶点坐标数据缓冲
        // vertices.length * 4是因为一个整数4个字节
        ByteBuffer vbb = ByteBuffer.allocate(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder()); // 字节序
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
        // 创建顶点法向量坐标缓冲
        ByteBuffer nbb = ByteBuffer.allocate(vertices.length * 4);
        nbb.order(ByteOrder.nativeOrder());
        mNormalBuffer = nbb.asFloatBuffer();
        mNormalBuffer.put(vertices);
        mNormalBuffer.position(0);
    }
    public void drawSelf(float alpha)
    {
        GLES20.glUseProgram(mProgram);
//        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.);
    }
}
