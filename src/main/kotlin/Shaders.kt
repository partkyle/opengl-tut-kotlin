import org.lwjgl.opengl.GL20.*

class Shaders {
    fun loadShaders(shader: String, fragment: String): Int {
        val vertexShaderId = glCreateShader(GL_VERTEX_SHADER)
        val fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER)

        loadShader(vertexShaderId, shader)

        loadShader(fragmentShaderId, fragment)

        val program = glCreateProgram()
        glAttachShader(program, vertexShaderId)
        glAttachShader(program, fragmentShaderId)
        glLinkProgram(program)
        val result3 = glGetProgramInfoLog(program)
        if (result3 != null && result3 != "") error(result3)

        glDetachShader(program, vertexShaderId)
        glDetachShader(program, fragmentShaderId)

        return program
    }

    private fun loadShader(vertexShaderId: Int, shader: String) {
        glShaderSource(vertexShaderId, shader)
        glCompileShader(vertexShaderId)
        val result = glGetShaderInfoLog(vertexShaderId)
        if (result != null && result != "") error(result)
    }
}