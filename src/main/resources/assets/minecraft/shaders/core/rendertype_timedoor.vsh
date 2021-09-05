#version 150


in vec3 Position;
in vec4 Color;
in vec2 UV0, UV2;

out vec4 color;
out vec3 dirFromCamera;
out vec2 texCoord, lightmap, oneTexel;

uniform mat4 ModelViewMat, ProjMat, ViewMat;
uniform vec2 InSize;

void main() {
    color = Color;
    texCoord = UV0;
    lightmap = UV2;
    mat4 model = ModelViewMat * inverse(ViewMat);
    vec4 dunnoPos = model * vec4(Position, 1);
    dirFromCamera = dunnoPos.xyz;
    oneTexel = 1.0 / InSize;
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1);
}