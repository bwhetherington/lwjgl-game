#version 330 core

layout(location = 0) in vec4 vPosition;
layout(location = 1) in mat4 modelMatrix;

void main() {
    gl_Position = modelMatrix * vPosition;
}