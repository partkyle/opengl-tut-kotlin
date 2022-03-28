import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*

class Tut1 {
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

        glClearColor(0.0f, 0xde / 255.0f, 0xd0 / 255.0f, 0.0f)

        do {
            glClear(GL_COLOR_BUFFER_BIT)

            glfwSwapBuffers(window)
            glfwPollEvents()
        } while(glfwGetKey(window, GLFW_KEY_ESCAPE ) != GLFW_PRESS &&
            !glfwWindowShouldClose(window))

        glfwTerminate()
    }
}