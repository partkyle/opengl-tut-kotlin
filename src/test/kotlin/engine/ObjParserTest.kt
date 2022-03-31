package engine

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ObjParserTest {
    @Test
    fun test() {
        val testObj = ObjParser()

        val expectedName = "Cube"
        val expectedVertexes = arrayOf(
            Triple(1.000000, 1.000000, -1.000000),
            Triple(1.000000, -1.000000, -1.000000),
        )
        val expectedNormals = arrayOf(
            Triple(-0.0000, 1.0000, -0.0000),
            Triple(-0.0000, -0.0000, -1.0000),
        )
        val expectedUv = arrayOf(
            Pair(0.625000, 0.500000),
            Pair(0.375000, 0.500000),
            Pair(0.125000, 0.750000),
        )
        val expectedVertexIndices = arrayOf(
            5, 3, 1, 3, 8, 4, 7, 6, 8
        )

        val expectedUvIndices = arrayOf(
            5, 3, 1, 3, 13, 4, 11, 8, 12
        )

        val expectedNormalIndices = arrayOf(
            1, 1, 1, 2, 2, 2, 3, 3, 3
        )

        val result = testObj.parse("cube.obj")

        assertEquals(expectedName, result.name)
        assertArrayEquals(expectedVertexes, result.vertexes)
        assertArrayEquals(expectedNormals, result.normals)
        assertArrayEquals(expectedUv, result.uv)
        assertArrayEquals(expectedVertexIndices, result.vertexIndices)
        assertArrayEquals(expectedUvIndices, result.normalIndices)
        assertArrayEquals(expectedNormalIndices, result.uvIndices)
    }
}