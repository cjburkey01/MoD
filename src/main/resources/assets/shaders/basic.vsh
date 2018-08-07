#version 330 core

layout (location = 0) in vec2 pos;
layout (location = 1) in vec2 uv;

out vec2 tex;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

void main() {
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(pos, 0.0, 1.0);
    tex = uv;
}