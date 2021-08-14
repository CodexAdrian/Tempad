#version 150

in vec2 texCoord;
in vec2 lightmap;
in vec4 color;

out vec4 fragColor;

float easeOut(float x) {
    float x2 = x * x;
    float x4 = x2 * x2;
    float x8 = x4 * x4;
    float x16 = x8 * x8;
    float x25 = x16 * x4 * x;
    return clamp(x25, 0, 1);
}

vec3 adjustColor(vec3 color, float light) {
    return  color * (1.25 - light) * 4.8;
}

void main() {
    float light = length(lightmap/255) * .5;
    vec3 center = vec3(1, .4, .1) * .03;
    vec3 edge = vec3(1, .4, .1) * .3;
    vec3 adjustedEdge = adjustColor(edge, light);
    float x = abs(texCoord.s - 0.5) * 2;
    float y = abs(texCoord.t - 0.5) * 2;
    vec3 xVec = mix(center, adjustedEdge, easeOut(x));
    vec3 yVec = mix(center, adjustedEdge, easeOut(y));
    fragColor = vec4((xVec + yVec) * 0.5, light + .5);
}

