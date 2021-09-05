#version 150


in vec3 Position;

out vec2 texCoord;

uniform mat4 ModelViewMat, ProjMat;

void main() {
    texCoord = Position.xy;
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
}