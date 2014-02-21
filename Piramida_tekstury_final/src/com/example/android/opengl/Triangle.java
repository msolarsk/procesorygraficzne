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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 * A two-dimensional triangle for use as a drawn object in OpenGL ES 2.0.
 */
public class Triangle {

    int kolorTrojkata = 1;

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +

            "attribute vec2 a_TexCoordinate;" + //koordynaty do
            "varying vec2 v_TexCoordinate;" +   //teksturowania

            "void main() {" +
            // the matrix must be included as a modifier of gl_Position
            // Note that the uMVPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            "  gl_Position = uMVPMatrix * vPosition;" +
            "  v_TexCoordinate = a_TexCoordinate;" + //teksturowanie
            "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
            "uniform vec4 vColor;" + //do usuniecia?

            "uniform sampler2D u_Texture;" +  //do
            "varying vec2 v_TexCoordinate;" + //teksturowania

            "void main() {" +
            "  gl_FragColor = vColor;" + //do usuniecia?
            "  gl_FragColor = texture2D(u_Texture, v_TexCoordinate);" + //teksturowanie
            "}";

    private final FloatBuffer vertexBuffer;
    private final FloatBuffer textureBuffer; //Bufor do
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    private int numer_tekstury;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    /*static float triangleCoords[] = {
            // in counterclockwise order:
          //  0.0f,  0.622008459f, 0.0f,   // top
          // -0.5f, -0.311004243f, 0.0f,   // bottom left
          //  0.5f, -0.311004243f, 0.0f    // bottom right

            0.0f, 0.5f, 0.0f, //gorny-podstawa
            0.0f, -0.5f, 0.0f, //dolna-podstawa
           -0.5f, 0.0f, 0.0f, //prawy-podstawa

            0.0f, 0.5f, 0.0f, //gorny-podstawa
            0.0f, -0.5f, 0.0f, //dolna-podstawa
            0.5f, 0.0f, 0.0f //lewy-podstawa

          //  0.0f, 0.0f, 0.5f //gora

    }; */


    final float[] previewTextureCoordinateData = //zmienic nazwe tej zmiennej
            {
                    0.0f, 1.0f,
                    0.0f, 0.0f,
                    1.0f, 1.0f,
                    1.0f, 0.0f
            };

    private int textureDataHandle;       //do
    private int textureUniformHandle;    //robienia
    private int textureCoordinateHandle; //tekstury

    //private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexCount = 3;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 0.0f };
   // float color[];

    private int loadTexture(final Context context, final int resourceId) //funkcja do ladowania tekstury
    {
        final int[] textureHandle = new int[1];

        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0)
        {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling

            // Read in the resource
            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        }

        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Triangle(Context context, float kolor[], float triangleCoords[], int text_nr) {

        //float k1, float k2, float k3, float alfa

        color = kolor;
        numer_tekstury = text_nr;

       // color[0] = k1;
       // color[1] = k2;
       // color[2] = k3;
       // color[3] = alfa;

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        ByteBuffer texCoordinates = ByteBuffer.allocateDirect(previewTextureCoordinateData.length * 4); // alokowanie
        texCoordinates.order(ByteOrder.nativeOrder());                                                  // bufora
        textureBuffer = texCoordinates.asFloatBuffer();                                                 // dla
        textureBuffer.put(previewTextureCoordinateData);                                                // koordynatow
        textureBuffer.position(0);                                                                      // tekstur

        // prepare shaders and OpenGL program
        int vertexShader = MyGLRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        if (numer_tekstury == 0){
        textureDataHandle = loadTexture(context, R.drawable.red_on_white); //ladujemy teksture funkcja
        }
        if (numer_tekstury == 1){
        textureDataHandle = loadTexture(context, R.drawable.blue_on_white);
        }
        if (numer_tekstury == 2){
        textureDataHandle = loadTexture(context, R.drawable.green_on_white);
        }
        if (numer_tekstury == 3){
            textureDataHandle = loadTexture(context, R.drawable.ic_launcher);
        }

////
        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
     */
    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        textureCoordinateHandle = GLES20.glGetAttribLocation(mProgram, "a_TexCoordinate"); //uchwyt do koord tekst.
        GLES20.glVertexAttribPointer(textureCoordinateHandle, 2, GLES20.GL_FLOAT, false, 0, textureBuffer); //tekst.
        GLES20.glEnableVertexAttribArray(textureCoordinateHandle); //jw

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        textureUniformHandle = GLES20.glGetUniformLocation(mProgram, "u_Texture"); // tekstury
        MyGLRenderer.checkGlError("glGetUniformLocation");                         //
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);                                //
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureDataHandle);             //
        GLES20.glUniform1i(textureUniformHandle, 0);                               //

        // Set color for drawing the triangle
        //GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

        //GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
         //       GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

}
