import glm.mat._4.Mat4
import glm.vec._3.Vec3
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryStack
import java.nio.FloatBuffer

class Tut3 {
    private val shaders = Shaders()

    private val shader = """
    #version 330 core
    // Input vertex data, different for all executions of this shader.
    
    layout(location = 0) in vec3 pos;
      
    // Values that stay constant for the whole mesh.
    uniform mat4 MVP;
      
    void main(){
      // Output position of the vertex, in clip space : MVP * position
      gl_Position =  MVP * vec4(pos,1);
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

        val width = 1024
        val height = 768
        val title = "Tutorial 3"
        val window = glfwCreateWindow(width, height, title, 0, 0)
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

        val projection = Mat4().perspective(Math.toRadians(45.0).toFloat(), width/height.toFloat(), 0.1f, 100.0f)
        val view = Mat4().lookAt(
            Vec3(4.0, 3.0, 3.0),
            Vec3(0.0,0.0,0.0),
            Vec3(0.0,1.0,0.0),
        )
        val model = Mat4().set(1.0f)
        val mvp = projection * view * model

        val matrixId = glGetUniformLocation(programId, "MVP")

        glUseProgram(programId)

        glUniformMatrix4fv(matrixId, false, mvp.toFa_())

        do {
            glClear(GL_COLOR_BUFFER_BIT)

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