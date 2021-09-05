#version 150

uniform sampler2D Sampler0;

in vec4 color;
in vec3 dirFromCamera;
in vec2 texCoord, lightmap, oneTexel;

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

vec3 blur() {
    vec3 pointThatWeWantToProject = normalize(dirFromCamera);
    vec3 pointOfViewCameraThing = dirFromCamera;
    vec3 normal = vec3(0, 0, -1);

    vec3 v = normalize(dirFromCamera);
    float d = dot(v, normal);
    vec3 projectedPoint = v + (d * normal);

    int kernelSize = 17;
    vec3 sum = vec3(1);
    int bound = (kernelSize - 1)/2;
    for (int x = -bound; x <= bound; ++x) {
        for (int y = -bound; y <= bound; ++y) {
            vec2 offset = vec2(oneTexel.x * x, oneTexel.y * y);
            sum += texture(Sampler0, projectedPoint.tp + offset).rgb;
        }
    }
    sum /= (kernelSize * kernelSize);
    //return sum;
    return projectedPoint + 0.5F;
}

void main() {
    float light = length(lightmap/255) * .5;
    vec3 center = color.rgb * .03;
    vec3 edge = color.rgb * .3;
    vec3 adjustedEdge = adjustColor(edge, light);
    float x = abs(texCoord.s - 0.5) * 2;
    float y = abs(texCoord.t - 0.5) * 2;
    vec3 xVec = mix(center, adjustedEdge, easeOut(x));
    vec3 yVec = mix(center, adjustedEdge, easeOut(y));
    fragColor = vec4((xVec + yVec) * 0.5, light + .5);
    //fragColor = vec4(blur(), 1);
    //fragColor = vec4(normalize(fragPos - CameraPosition), 1);
    //fragColor = vec4(normalize(CameraPosition), 1);
    //fragColor = texture(Sampler0, blur().st) + vec4((xVec + yVec) * 0.5, light + .5);
}

