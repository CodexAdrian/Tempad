#version 150

in vec2 uv;

out vec4 fragColor;

uniform float GameTime;

void main() {
    fragColor = vec4(240.0/255.0, 140.0/255.0, 0, .6);
}