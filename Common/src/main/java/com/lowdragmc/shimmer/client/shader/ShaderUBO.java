package com.lowdragmc.shimmer.client.shader;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL32C;

/**
 * @author KilaBash
 * @date 2022/5/4
 * @implNote ShaderUBO, Uniform Buffer Object
 */
public class ShaderUBO {
    public final int id;
    private boolean inValid;
    private int blockBinding = -1;

    public ShaderUBO() {
        id = GL32C.glGenBuffers();
    }

    private void close() {
        if(!inValid) {
            GL32C.glDeleteBuffers(id);
            inValid = true;
        }
    }

    public void bindBuffer() {
        GL32C.glBindBuffer(GL32C.GL_UNIFORM_BUFFER, id);
    }

    public void unBindBuffer() {
        GL32C.glBindBuffer(GL32C.GL_UNIFORM_BUFFER, 0);
    }

    public void createBufferData(long size, int mode) {
        bindBuffer();
        GL32C.glBufferData(GL32C.GL_UNIFORM_BUFFER, size, mode);
        unBindBuffer();
    }

    public void createBufferData(FloatBuffer data, int mode) {
        bindBuffer();
        GL32C.glBufferData(GL32C.GL_UNIFORM_BUFFER, data, mode);
        unBindBuffer();
    }

    public void bufferSubData(long offset, FloatBuffer data) {
        bindBuffer();
        GL32C.glBufferSubData(GL32C.GL_UNIFORM_BUFFER, offset, data);
        unBindBuffer();
    }

    public void bufferSubData(long offset, float[] data) {
        bindBuffer();
        GL32C.glBufferSubData(GL32C.GL_UNIFORM_BUFFER, offset, data);
        unBindBuffer();
    }

    public void bufferSubData(long offset, int[] data) {
        bindBuffer();
        GL32C.glBufferSubData(GL32C.GL_UNIFORM_BUFFER, offset, data);
        unBindBuffer();
    }

    public void blockBinding(int blockBinding) {
        this.blockBinding = blockBinding;
        if (blockBinding > -1) {
            GL32C.glBindBufferBase(GL32C.GL_UNIFORM_BUFFER, blockBinding, id);
        }
    }

    public void bindToShader(int program, String bufBlockName) {
        if (blockBinding > -1) {
            int blockIndex = GL32C.glGetUniformBlockIndex(program, bufBlockName);

            if (blockIndex > -1) {
                GL32C.glUniformBlockBinding(program, blockIndex, blockBinding);
            }
        }
    }

}
