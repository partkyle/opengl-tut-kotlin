import glm.mat._4.Mat4
import org.lwjgl.opengl.GL20

operator fun Mat4.times(other: Mat4): Mat4 {
    return Mat4(this).mul(other)
}