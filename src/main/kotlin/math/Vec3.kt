package math

import glm.vec._3.Vec3

fun Vec3.map(f: (Float) -> Float): Vec3 {
   return Vec3(
       f(this.x),
       f(this.y),
       f(this.z),
   )
}

operator fun Vec3.minus(that: Vec3): Vec3 {
    return Vec3(this.x - that.x, this.y - that.y, this.z - that.z)
}

operator fun Vec3.plus(that: Vec3): Vec3 {
    return Vec3(this.x + that.x, this.y + that.y, this.z + that.z)
}

operator fun Vec3.times(scalar: Float): Vec3 {
    return Vec3(this.x * scalar, this.y * scalar, this.z * scalar)
}

