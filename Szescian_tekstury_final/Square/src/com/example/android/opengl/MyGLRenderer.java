/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

/**
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceCreated}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onDrawFrame}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";
   // private Triangle mTriangle;

   // private Triangle mPodstawa1;
   // private Triangle mPodstawa2;
   // private Triangle mSciana1;
   // private Triangle mSciana2;
   // private Triangle mSciana3;
   // private Triangle mSciana4;

    private Square mScianka1;
    private Square mScianka2;
    private Square mScianka3;
    private Square mScianka4;
    private Square mScianka5;
    private Square mScianka6;

    private Square   mSquare;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];

    private float mAngle;

    public Context kontekst;

    public MyGLRenderer (Context context){
        kontekst = context;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
       // GLES20.glFrontFace(GLES20.GL_CCW);

        float kolor1[] = { 1.0f, 0.0f, 0.0f, 0.0f };

        float kolor2[] = {0.0f, 1.0f, 0.0f, 0.0f};
        float kolor3[] = {0.5f, 0.5f, 0.0f, 0.0f};
        float kolor4[] = {0.0f, 0.0f, 1.0f, 0.0f};
        float kolor5[] = {0.5f, 0.0f, 1.0f, 0.0f};
        float kolor6[] = {1.0f, 0.0f, 1.0f, 0.0f};

        float wierzcholki1[] = {

                0.0f, 0.5f, 0.0f, //gorny-podstawa
                0.0f, -0.5f, 0.0f, //dolna-podstawa
                -0.5f, 0.0f, 0.0f};

        float wierzcholki2[] = {

                0.0f, 0.5f, 0.0f, //gorny-podstawa
                0.0f, -0.5f, 0.0f, //dolna-podstawa
                0.5f, 0.0f, 0.0f //lewy-podstawa
        };

        float wierzcholki3[] = {
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                -0.5f, 0.0f, 0.0f
        };

        float wierzcholki4[] = {
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.5f, 0.0f, 0.0f
        };

        float wierzcholki5[] = {
                0.0f, -0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                -0.5f, 0.0f, 0.0f
        };

        float wierzcholki6[] = {
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.5f, 0.0f, 0.0f
        };

      //  mPodstawa1 = new Triangle(kolor1, wierzcholki1);
       // mPodstawa2 = new Triangle(kolor1, wierzcholki2);


      //  mSciana1 = new Triangle(kolor2, wierzcholki3);
      //  mSciana2 = new Triangle(kolor3, wierzcholki4);
      //  mSciana3 = new Triangle(kolor4, wierzcholki5);
      //  mSciana4 = new Triangle(kolor5, wierzcholki6);

        //short kolejnosc1[] = {0, 1, 2, 0, 2, 3};
        short kolejnosc1[] = {3, 2, 0, 2, 1, 0}; //front
        //short kolejnosc2[] = {0, 1, 5, 0, 5, 4}; //right
        short kolejnosc2[] = {0, 1, 4, 1, 5, 4}; //right
        //short kolejnosc3[] = {7, 6, 2, 7, 2, 3}; //left
        short kolejnosc3[] = {7, 6, 3, 6, 2, 3}; // left
        //short kolejnosc4[] = {7, 6, 5, 7, 5, 4}; //back
        short kolejnosc4[] = {4, 5, 7, 5, 6, 7}; //back
        //short kolejnosc5[] = {4, 0, 3, 4, 3, 7}; //top
        short kolejnosc5[] = {7, 3, 4, 3, 0, 4}; //top
        //short kolejnosc6[] = {6, 2, 1, 6, 1, 5}; //bottom
        short kolejnosc6[] = {2, 6, 1, 6, 5, 1}; //bottom


        mSquare   = new Square(kontekst, kolor1,kolejnosc1, 3);
        mScianka2 = new Square(kontekst, kolor2,kolejnosc2, 3);
        mScianka3 = new Square(kontekst, kolor3,kolejnosc3, 3);
        mScianka4 = new Square(kontekst, kolor4,kolejnosc4, 3);
        mScianka5 = new Square(kontekst, kolor5,kolejnosc5, 3);
        mScianka6 = new Square(kontekst, kolor6,kolejnosc6, 3);

    }

    @Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);


        Matrix.setRotateM(mRotationMatrix, 0, mAngle,  1.0f, 1.0f, 0);
        //Matrix.setRotateM(mRotationMatrix, 0, mAngle,  1.0f, 1.0f ,0); //

        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0); //
        // Draw square
       //-// mSquare.draw(mMVPMatrix);
        mSquare.draw(scratch);
        mScianka2.draw(scratch);
        mScianka3.draw(scratch);
        mScianka4.draw(scratch);
        mScianka5.draw(scratch);
        mScianka6.draw(scratch);

        // Create a rotation for the triangle

        // Use the following code to generate constant rotation.
        // Leave this code out when using TouchEvents.
        // long time = SystemClock.uptimeMillis() % 4000L;
        // float angle = 0.090f * ((int) time);

       //--// Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, 1.0f);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
       //--// Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        // Draw triangle
       // mPodstawa1.draw(scratch);
       // mPodstawa2.draw(scratch);
       // mSciana1.draw(scratch);
      //  mSciana2.draw(scratch);
      //  mSciana3.draw(scratch);
       // mSciana4.draw(scratch);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

    }

    /**
     * Utility method for compiling a OpenGL shader.
     *
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
    * Utility method for debugging OpenGL calls. Provide the name of the call
    * just after making it:
    *
    * <pre>
    * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
    * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
    *
    * If the operation is not successful, the check throws an error.
    *
    * @param glOperation - Name of the OpenGL call to check.
    */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    /**
     * Returns the rotation angle of the triangle shape (mTriangle).
     *
     * @return - A float representing the rotation angle.
     */
    public float getAngle() {
        return mAngle;
    }

    /**
     * Sets the rotation angle of the triangle shape (mTriangle).
     */
    public void setAngle(float angle) {
        mAngle = angle;
    }

}