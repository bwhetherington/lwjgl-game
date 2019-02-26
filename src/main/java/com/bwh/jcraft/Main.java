package com.bwh.jcraft;

import com.bwh.jcraft.math.Mat4;
import com.bwh.jcraft.util.SharedLibraryLoader;

public class Main extends Game {
    private Program program;
    private double theta = 0;

    @Override
    public void init() {
        program = new Program("shader.vert", "shader.frag");

        final var vertices = new float[]{
                -0.5f, -0.5f, 0, 1,
                0.5f, -0.5f, 0, 1,
                0, 0.7f, 0, 1
        };

        program.setVertices(vertices, 4);
    }

    @Override
    public void update(float dt) {
        theta += 0.0001;
        final var model = Mat4.rotateX(theta);
        program.setModelMatrix(model);
    }

    @Override
    public void render(float dt) {

        program.render();
    }

    public static void main(String[] args) {
        SharedLibraryLoader.load();
        new Main().run();
    }
}
