import glm.mat._4.Mat4
import glm.vec._3.Vec3
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glGenVertexArrays
import org.lwjgl.system.MemoryStack.stackPush


class Platform {
    private val shaders = Shaders()

    private var window: Long = 0

    val width = 1024
    val height = 768

    private val shader = """
        #version 330 core
        
        // Input vertex data, different for all executions of this shader.
        layout(location = 0) in vec3 vertexPosition_modelspace;
        layout(location = 1) in vec3 vertexColor;
        
        // Output data ; will be interpolated for each fragment.
        out vec3 fragmentColor;
        // Values that stay constant for the whole mesh.
        uniform mat4 MVP;
        
        void main(){	
        
            // Output position of the vertex, in clip space : MVP * position
            gl_Position =  MVP * vec4(vertexPosition_modelspace,1);
        
            // The color of each vertex will be interpolated
            // to produce the color of each fragment
            fragmentColor = vertexColor;
        }
    """.trimIndent()

    private val fragment = """
        #version 330 core
        
        // Interpolated values from the vertex shaders
        in vec3 fragmentColor;
        
        // Ouput data
        out vec3 color;
        
        void main(){
        
            // Output color = color specified in the vertex shader, 
            // interpolated between all 3 surrounding vertices
            color = fragmentColor;
        
        }
    """.trimIndent()

    private val vertex_buffer_data = floatArrayOf(
        -1.0f, -1.0f, -1.0f, // triangle 1 : begin
        -1.0f, -1.0f, 1.0f,
        -1.0f, 1.0f, 1.0f, // triangle 1 : end
        1.0f, 1.0f, -1.0f, // triangle 2 : begin
        -1.0f, -1.0f, -1.0f,
        -1.0f, 1.0f, -1.0f, // triangle 2 : end
        1.0f, -1.0f, 1.0f,
        -1.0f, -1.0f, -1.0f,
        1.0f, -1.0f, -1.0f,
        1.0f, 1.0f, -1.0f,
        1.0f, -1.0f, -1.0f,
        -1.0f, -1.0f, -1.0f,
        -1.0f, -1.0f, -1.0f,
        -1.0f, 1.0f, 1.0f,
        -1.0f, 1.0f, -1.0f,
        1.0f, -1.0f, 1.0f,
        -1.0f, -1.0f, 1.0f,
        -1.0f, -1.0f, -1.0f,
        -1.0f, 1.0f, 1.0f,
        -1.0f, -1.0f, 1.0f,
        1.0f, -1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        1.0f, -1.0f, -1.0f,
        1.0f, 1.0f, -1.0f,
        1.0f, -1.0f, -1.0f,
        1.0f, 1.0f, 1.0f,
        1.0f, -1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, -1.0f,
        -1.0f, 1.0f, -1.0f,
        1.0f, 1.0f, 1.0f,
        -1.0f, 1.0f, -1.0f,
        -1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        -1.0f, 1.0f, 1.0f,
        1.0f, -1.0f, 1.0f
    )

    private val color_data = floatArrayOf(

        0.583f, 0.771f, 0.014f,
        0.609f, 0.115f, 0.436f,
        0.327f, 0.483f, 0.844f,
        0.822f, 0.569f, 0.201f,
        0.435f, 0.602f, 0.223f,
        0.310f, 0.747f, 0.185f,
        0.597f, 0.770f, 0.761f,
        0.559f, 0.436f, 0.730f,
        0.359f, 0.583f, 0.152f,
        0.483f, 0.596f, 0.789f,
        0.559f, 0.861f, 0.639f,
        0.195f, 0.548f, 0.859f,
        0.014f, 0.184f, 0.576f,
        0.771f, 0.328f, 0.970f,
        0.406f, 0.615f, 0.116f,
        0.676f, 0.977f, 0.133f,
        0.971f, 0.572f, 0.833f,
        0.140f, 0.616f, 0.489f,
        0.997f, 0.513f, 0.064f,
        0.945f, 0.719f, 0.592f,
        0.543f, 0.021f, 0.978f,
        0.279f, 0.317f, 0.505f,
        0.167f, 0.620f, 0.077f,
        0.347f, 0.857f, 0.137f,
        0.055f, 0.953f, 0.042f,
        0.714f, 0.505f, 0.345f,
        0.783f, 0.290f, 0.734f,
        0.722f, 0.645f, 0.174f,
        0.302f, 0.455f, 0.848f,
        0.225f, 0.587f, 0.040f,
        0.517f, 0.713f, 0.338f,
        0.053f, 0.959f, 0.120f,
        0.393f, 0.621f, 0.362f,
        0.673f, 0.211f, 0.457f,
        0.820f, 0.883f, 0.371f,
        0.982f, 0.099f, 0.879f
    )

    fun run() {
        init()
        loop()
    }

    fun init() {
        GLFWErrorCallback.createPrint(System.err).set()


        if (!glfwInit()) {
            throw IllegalStateException("unable to initialize GLFW")
        }

        glfwWindowHint(GLFW_SAMPLES, 4)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)


        window = glfwCreateWindow(width, height, "opengl-tut", 0, 0)
        if (window == 0L) {
            throw RuntimeException("failed to create GLFW window")
        }

        stackPush().use { stack ->
            val pWidth = stack.mallocInt(1) // int*
            val pHeight = stack.mallocInt(1) // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight)

            // Get the resolution of the primary monitor
            val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())

            // Center the window
            glfwSetWindowPos(
                window,
                (vidmode.width() - pWidth[0]) / 2,
                (vidmode.height() - pHeight[0]) / 2
            )
        }

        // Make the OpenGL context current
        glfwMakeContextCurrent(window)
        // Enable v-sync
        glfwSwapInterval(1)

        // Make the window visible
        glfwShowWindow(window)

        GL.createCapabilities()
    }

    fun loop() {
        glClearColor(0f, 0.0f, 1.0f, 0.0f)

        val vertexArrayId = glGenVertexArrays()
        glBindVertexArray(vertexArrayId)

        val programId = shaders.loadShaders(shader, fragment)

        val vertexBuffer = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer)
        glBufferData(GL_ARRAY_BUFFER, vertex_buffer_data, GL_STATIC_DRAW)

        val colorBuffer = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, colorBuffer)
        glBufferData(GL_ARRAY_BUFFER, color_data, GL_STATIC_DRAW)


        val projection = Mat4().perspective(Math.toRadians(45.0).toFloat(), width / height.toFloat(), 0.1f, 100.0f)
        val view = Mat4().lookAt(
            Vec3(4.0, 3.0, 3.0),
            Vec3(0.0, 0.0, 0.0),
            Vec3(0.0, 1.0, 0.0),
        )
        val model = Mat4(1.0f)

        val matrix = glGetUniformLocation(programId, "MVP")

        while (glfwGetKey(window, GLFW_KEY_ESCAPE) != GLFW_PRESS && !glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT)

            glUseProgram(programId)
            val mvp = projection * view * model
            glUniformMatrix4fv(matrix, false, mvp.toFa_())

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

            glEnableVertexAttribArray(1)
            glBindBuffer(GL_ARRAY_BUFFER, colorBuffer)
            glVertexAttribPointer(
                1,
                3,
                GL_FLOAT,
                false,
                0,
                0
            )

            glDrawArrays(GL_TRIANGLES, 0, 12 * 3)

            glDisableVertexAttribArray(0)
            glDisableVertexAttribArray(1)

            glfwSwapBuffers(window)
            glfwPollEvents()
        }

        glfwTerminate()
    }
}

