package com.example.lab4.gles;

public class Shaders {

    public static final String vertexShaderCode =
            "#version 300 es\n" +
            "in vec3 vPosition;\n" +
            "in vec3 vNormal;\n" +

            "uniform mat4 uModelMatrix;\n" +
            "uniform mat4 uViewMatrix;\n" +
            "uniform mat4 uProjMatrix;\n" +

            "out vec3 currentPos;\n" +
            "out vec3 currentNormal;\n" +

            "void main() {\n" +
            "   gl_Position = uProjMatrix * uViewMatrix * uModelMatrix * vec4(vPosition, 1.0f);\n" +
            "   currentPos = mat3(uModelMatrix) * vPosition;\n" +
            "   currentNormal = mat3(uModelMatrix) * vNormal;\n" +
            "   gl_PointSize = 15.0f;\n" +
            "}\n";

    // ambient + diffuse
    public static final String fragmentShaderCode1 =
            "#version 300 es\n" +
            "precision mediump float;\n" +
            "in vec3 currentPos;\n" +
            "in vec3 currentNormal;\n" +

            "uniform vec3 vColor;\n" +
            "uniform vec3 vLightColor;\n" +
            "uniform vec3 vLightPos;\n" +
            "uniform vec3 vEyePos;\n" +

            "out vec4 resultColor;\n" +

            "void main() {\n" +
            "   if (vColor != vLightColor) {\n" +
            "       vec3 norm = normalize(currentNormal);\n" +
            "       vec3 lightDir = normalize(vLightPos - currentPos);\n" +
            "       float diffuse = max(dot(norm, lightDir), 0.0f);\n" +

            "       float distance = length(vLightPos - currentPos);\n" +
            "       float attenuation = 1.0f/(1.0f + distance*distance);\n" +

            "       vec3 vClr = 0.3f * vColor\n" +
            "               + 0.7f * diffuse * vLightColor * vColor;\n" +
            "       resultColor = vec4(attenuation * vClr, 1.0f);\n" +
            "   } else {\n" +
            "       resultColor = vec4(vLightColor, 1.0f);\n" +
            "   }\n" +
            "}\n";

    // ambient + specular
    public static final String fragmentShaderCode2 =
            "#version 300 es\n" +
            "precision mediump float;\n" +
            "in vec3 currentPos;\n" +
            "in vec3 currentNormal;\n" +

            "uniform vec3 vColor;\n" +
            "uniform vec3 vLightColor;\n" +
            "uniform vec3 vLightPos;\n" +
            "uniform vec3 vEyePos;\n" +

            "out vec4 resultColor;\n" +

            "void main() {\n" +
            "   if (vColor != vLightColor) {\n" +
            "       vec3 norm = normalize(currentNormal);\n" +
            "       vec3 lightDir = normalize(vLightPos - currentPos);\n" +

            "       vec3 reflectDir = normalize(reflect(-lightDir, norm));\n" +
            "       vec3 eyeDir = normalize(vEyePos - currentPos);\n" +
            "       float spec = max(dot(eyeDir, reflectDir), 0.0f);\n" +
            "       spec = pow(spec, 200.0f);\n" +

            "       vec3 vClr = 0.3f * vColor\n" +
            "           + 0.7f * spec * vLightColor;\n" +
            "       resultColor = vec4(vClr, 1.0f);\n" +
            "   } else {\n" +
            "       resultColor = vec4(vLightColor, 1.0f);\n" +
            "   }\n" +
            "}\n";

    // ambient + diffuse + specular + attenuation
    public static final String fragmentShaderCode3 =
            "#version 300 es\n" +
            "precision mediump float;\n" +
            "in vec3 currentPos;\n" +
            "in vec3 currentNormal;\n" +

            "uniform vec3 vColor;\n" +
            "uniform vec3 vLightColor;\n" +
            "uniform vec3 vLightPos;\n" +
            "uniform vec3 vEyePos;\n" +

            "out vec4 resultColor;\n" +

            "void main() {\n" +
            "   if (vColor != vLightColor) {\n" +
            "       vec3 norm = normalize(currentNormal);\n" +
            "       vec3 lightDir = normalize(vLightPos - currentPos);\n" +
            "       float diffuse = max(dot(norm, lightDir), 0.0f);\n" +

            "       float distance = length(vLightPos - currentPos);\n" +
            "       float attenuation = 1.0f/(1.0f + distance*distance);\n" +

            "       vec3 reflectDir = normalize(reflect(-lightDir, norm));\n" +
            "       vec3 eyeDir = normalize(vEyePos - currentPos);\n" +
            "       float spec = max(dot(eyeDir, reflectDir), 0.0f);\n" +
            "       spec = pow(spec, 200.0f);\n" +

            "       vec3 vClr = 0.25f * vColor\n" +
            "           + 0.4f * diffuse * vLightColor * vColor\n" +
            "           + 0.3f * spec * vLightColor;\n" +
            "       resultColor = vec4(attenuation * vClr, 1.0f);\n" +
            "   } else {\n" +
            "       resultColor = vec4(vLightColor, 1.0f);\n" +
            "   }\n" +
            "}\n";

    // ambient + diffuse + attenuation
    public static final String fragmentShaderCode4 =
            "#version 300 es\n" +
            "precision mediump float;\n" +
            "in vec3 currentPos;\n" +
            "in vec3 currentNormal;\n" +

            "uniform vec3 vColor;\n" +
            "uniform vec3 vLightColor;\n" +
            "uniform vec3 vLightPos;\n" +
            "uniform vec3 vEyePos;\n" +

            "out vec4 resultColor;\n" +

            "void main() {\n" +
            "   if (vColor != vLightColor) {\n" +
            "       vec3 norm = normalize(currentNormal);\n" +
            "       vec3 lightDir = normalize(vLightPos - currentPos);\n" +
            "       float diffuse = max(dot(norm, lightDir), 0.0f);\n" +

            "       float distance = length(vLightPos - currentPos);\n" +
            "       float attenuation = 1.0f/(1.0f + distance*distance);\n" +

            "       vec3 vClr = 0.3f * vColor\n" +
            "           + 0.7f * diffuse * vLightColor * vColor;\n" +
            "       resultColor = vec4(attenuation * vClr, 1.0f);\n" +
            "   } else {\n" +
            "       resultColor = vec4(vLightColor, 1.0f);\n" +
            "   }\n" +
            "}\n";
}
