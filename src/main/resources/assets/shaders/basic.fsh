#version 330 core

in vec2 tex;

out vec4 fragColor;

uniform sampler2D ourTexture;

void main() {
    fragColor = texture(ourTexture, tex);
}