package com.bwh.jcraft;

import com.bwh.jcraft.math.Mat4;
import com.bwh.jcraft.util.ShaderUtil;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Program {
    private int programID;

    private int vertexArray;
    private int vertexBuffer;

    private int vertexCount;

    private int vShaderID;
    private int fShaderID;


    public Program(String vShader, String fShader) {
        programID = glCreateProgram();
        attachShaders(vShader, fShader);

        final var model = Mat4.identity();
        setModelMatrix(model);
    }

    public void link() {
        glLinkProgram(programID);

        // Check for errors
        if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Unable to link shader program");
        }
    }

    public int attachShader(String fileName, int type) {
        final var shaderSource = ShaderUtil.readFile(fileName);
        final var shaderID = glCreateShader(type);
        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);

        // Check for errors
        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            final var log = glGetShaderInfoLog(shaderID, glGetShaderi(shaderID, GL_INFO_LOG_LENGTH));
            final var err = String.format("Error compiling shader:\n%s", log);
            throw new RuntimeException(err);
        }

        glAttachShader(programID, shaderID);

        return shaderID;
    }

    public void attachShaders(String vShader, String fShader) {
        vShaderID = attachShader(vShader, GL_VERTEX_SHADER);
        fShaderID = attachShader(fShader, GL_FRAGMENT_SHADER);
        link();
    }

    public void bind() {
        glUseProgram(programID);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void dispose() {
        unbind();

        glDetachShader(programID, vShaderID);
        glDetachShader(programID, fShaderID);

        glDeleteShader(vShaderID);
        glDeleteShader(fShaderID);

        glDeleteProgram(programID);
    }

    public void setVertices(float[] vertices) {
        setVertices(vertices, 4);
    }

    public void setVertices(float[] vertices, int stride) {
        vertexCount = vertices.length / stride;

        if (vertexArray == 0) {
            vertexArray = glGenVertexArrays();
        }

        glBindVertexArray(vertexArray);

        final var buffer = BufferUtils.createFloatBuffer(vertices.length);
        buffer.put(vertices).flip();

        if (vertexBuffer == 0) {
            vertexBuffer = glGenBuffers();
        }

        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
        glBindVertexArray(0);
    }

    public void render() {
        bind();

        glBindVertexArray(vertexArray);
        glEnableVertexAttribArray(0);

        glDrawArrays(GL_TRIANGLES, 0, vertexCount);

        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        unbind();
    }

    public void setModelMatrix(Mat4 mat) {
        final var ptr = mat.toArray();
        final var buffer = BufferUtils.createFloatBuffer(ptr.length);
        buffer.put(ptr);
        glUniformMatrix4fv(1, false, buffer);
    }
}
