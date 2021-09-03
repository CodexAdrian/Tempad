#version 150

out vec4 fragColor;

void main() {
    /*
    int kernelSize = 3;
    vec3 sum = vec3(0.0, 0.0, 0.0);
    int bound = (kernelSize - 1)/2;
    for (int x = -bound; x <= bound; ++x) {
        for (int y = -bound; y <= bound; ++y) {
            vec2 offset = vec2(_MainTex_TexelSize.x * x, _MainTex_TexelSize.y * y);
            sum += texture2D(_MainTex, i.uv + offset);
        }
    }
    sum /= (kernelSize * kernelSize);
    */
    fragColor = vec4(vec3(1, 0, 0), 1);
}
