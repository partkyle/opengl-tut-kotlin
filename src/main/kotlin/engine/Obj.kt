package engine

class Obj<T>(
    val name: String,
    val vertexes: Array<Triple<T, T, T>>,
    val normals: Array<Triple<T, T, T>>,
    val uv: Array<Pair<T, T>>,
    val vertexIndices: Array<Int>,
    val uvIndices: Array<Int>,
    val normalIndices: Array<Int>,
)

