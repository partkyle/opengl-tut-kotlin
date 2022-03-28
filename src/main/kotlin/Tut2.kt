import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL30.*

class Tut2 {
    private val shaders = Shaders()

    private val shader = """
        #version 330 core

        // Input vertex data, different for all executions of this shader.
        layout(location = 0) in vec3 vertexPosition_modelspace;
        
        void main(){
        
            gl_Position.xyz = vertexPosition_modelspace;
            gl_Position.w = 1.0;
        
        }
    """.trimIndent()

    private val fragment = """
        #version 330 core

        // Output data
        out vec3 color;

        void main()
        {

        	// Output color = red 
        	color = vec3(1,0,0);

        }

    """.trimIndent()


    fun run() {
        if (!glfwInit()) {
            error("glfwInit")
        }

        glfwWindowHint(GLFW_SAMPLES, 4)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)

        val window = glfwCreateWindow(1024, 768, "", 0, 0)
        if (window == 0L) {
            error("no window")
        }

        glfwMakeContextCurrent(window)

        GL.createCapabilities()

        glfwSetInputMode(window, GLFW_STICKY_KEYS, GL_TRUE)

        glClearColor(0.0f, 0.0f, 1.0f, 0.0f)

        // triangles

        val vertexArrayId = glGenVertexArrays()
        glBindVertexArray(vertexArrayId)

        val programId = shaders.loadShaders(shader, fragment)

        val gVertexBufferData = floatArrayOf(
            -1.0f, -1.0f, 0.0f,
            1.0f, -1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
        )

        val vertexBuffer = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer)
        glBufferData(GL_ARRAY_BUFFER, gVertexBufferData, GL_STATIC_DRAW)

        do {
            glClear(GL_COLOR_BUFFER_BIT)

            glUseProgram(programId)

            glEnableVertexAttribArray(0)
            glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer)
            glVertexAttribPointer(
                0,
                3,
                GL_FLOAT,
                false,
                0,
                0
            )

            glDrawArrays(GL_TRIANGLES, 0, 3)

            glDisableVertexAttribArray(0)

            glfwSwapBuffers(window)
            glfwPollEvents()
        } while (glfwGetKey(window, GLFW_KEY_ESCAPE) != GLFW_PRESS && !glfwWindowShouldClose(window))

        glfwTerminate()
    }
}