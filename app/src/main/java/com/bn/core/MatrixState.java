package com.bn.core;

import android.opengl.Matrix;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Boboqi on 2017-9-19.
 */

public class MatrixState {
    private static float[] mProjMatrix = new float[16];
    private static float[] mVMatrix = new float[16];
    private static float[] currMatrix;
    public static float[] lightLocation = new float[]{0, 0, 0};
    public static FloatBuffer cameraFB;
    public static FloatBuffer lightPositionFB;
    // 保护变换矩阵的栈
    static float[][] mStack = new float[10][16];
    static int stackTop = -1;

    public static void setInitStack()
    {
        currMatrix = new float[16];
        Matrix.setRotateM(currMatrix, 0, 0, 1, 0, 0);
    }

    public static void pushMatrix()
    {
        stackTop ++;
        for (int i = 0; i < 16; ++i)
        {
            mStack[stackTop][i] = currMatrix[i];
        }
    }

    public static void popMatrix()
    {
        for (int i = 0; i < 16; ++i)
        {
            currMatrix[i] = mStack[stackTop][i];
        }
        stackTop--;
    }

    public static void translate(float x, float y, float z)
    {
        Matrix.translateM(currMatrix, 0, x, y, z);
    }

    public static void rotate(float angle, float x, float y, float z)
    {
        Matrix.rotateM(currMatrix, 0, angle, x, y, z);
    }

    public static void scale(float x, float y, float z)
    {
        Matrix.scaleM(currMatrix, 0, x, y, z);
    }

    // 插入自带矩阵
    public static void matrix(float []self)
    {
        float []result = new float[16];
        Matrix.multiplyMM(result, 0, currMatrix, 0, self, 0);
        currMatrix = result;
    }

    // 设置摄像机
    static ByteBuffer llbb = ByteBuffer.allocateDirect(3 * 4);
    static float[] cameraLocation = new float[3];  // 摄像机位置
    public static void setCamera(float cx, float cy, float cz, float tx, float ty, float tz, float upx, float upy, float upz)
    {
        Matrix.setLookAtM(mVMatrix, 0, cx, cy, cz, tx, ty, tz, upx, upy, upz);
        cameraLocation[0] = cx;
        cameraLocation[1] = cy;
        cameraLocation[2] = cz;
        llbb.clear();
        llbb.order(ByteOrder.nativeOrder());
        cameraFB = llbb.asFloatBuffer();
        cameraFB.put(cameraLocation);
        cameraFB.position(0);
    }
    // 设置透视投影参数
    public static void setProjectFrustum(float left, float right, float bottom, float top, float near, float far)
    {
        Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

}
