#version 150
in vec4 vColor;
in vec2 vUV;

uniform vec4 ColorModulator;

out vec4 fragColor;

void main() {
    vec2 p = vUV * 2.0 - 1.0;
    float d = length(p);
    float glow = smoothstep(1.0, 0.0, d);
    float soft = smoothstep(0.0, 0.8, glow);
    float a = soft * vColor.a;
    vec3 col = vColor.rgb * ColorModulator.rgb;
    fragColor = vec4(col * soft, a);
}