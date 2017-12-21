package com.example.tbichan.syaroescape.opengl.shader;

/**
 * Created by 5515012o on 2017/12/13.
 */

public class ShaderSource {

    // テクスチャ用シェーダー
    public static final String VERTEX_SHADER_CODE_TEX =
                    "uniform mat4 wMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec2 a_texCoord;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                        "gl_Position = wMatrix * vPosition;" +
                        "v_texCoord = a_texCoord;" +
                    "}";

    public static final String FRAGMENT_SHADER_CODE_TEX =
                    "precision mediump float;" +
                    "varying vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "uniform float Opacity;" +
                    "uniform float Brightness;" +
                    "void main() {" +
                        "lowp vec4 textureColor = texture2D(s_texture, v_texCoord);" +
                        "gl_FragColor = vec4((textureColor.rgb + vec3(Brightness)), textureColor.w*Opacity);" +
                    "}";

    // 最もシンプルなシェーダー
    public static final String VERTEX_SHADER_CODE_COLOR =
                    "uniform mat4 wMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                       "gl_Position = wMatrix * vPosition;" +
                    "}";

    public static final String FRAGMENT_SHADER_CODE_COLOR =
                    "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                        "gl_FragColor =vColor;" +
                    "}";

    // 法線ベクトルを考えたシェーダー
    public static final String VERTEX_SHADER_CODE_NORMAL =
                    "uniform mat4 wMatrix;" +
                    "uniform mat4 normalMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec3 vertexNormal;" +
                    //"uniform vec3 lightDirection;" +
                    //"uniform vec3 lightDiffuseColor;" +
                    //"uniform vec3 objectDiffuseColor;" +
                    "varying lowp vec4 colorVarying;" +
                    "void main() {" +
                      "gl_Position = wMatrix * vPosition;" +
                      "vec3 normal = normalize(vec3(normalMatrix*vec4(vertexNormal, 1.0)));" +
                      //"normal = vec3(0.0, 0.0, 1.0);" +
                      "vec3 lightDirection = vec3(0.0, 0.0, 1.0);" +
                      "float ndotl = max(0.0, dot(normal, normalize(lightDirection)));" +
                      "vec3 lightDiffuseColor = vec3(1.0, 1.0, 1.0);" +
                      "vec3 objectDiffuseColor = vec3(0.5, 0.5, 0.5);" +
                      "vec3 color = ndotl * lightDiffuseColor * objectDiffuseColor;" +
                      //"color = vec3(0.5, 0.5, 0.5);" +
                      "colorVarying = vec4(color, 1.0);" +
                    "}";

    public static final String FRAGMENT_SHADER_CODE_NORMAL =
            "precision mediump float;" +
                    "varying lowp vec4 colorVarying;" +
                    "void main() {" +
                    "gl_FragColor =colorVarying;" +
                    "}";

    private ShaderSource(){

    }


}
