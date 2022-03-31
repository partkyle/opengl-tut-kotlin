package engine

import java.io.File

class ObjParser {

    fun parse(filename: String): Obj<Double> {
        val readText = File(ClassLoader.getSystemResource(filename).file).readText()

        var name = ""
        val vertexes = mutableListOf<Triple<Double, Double, Double>>()
        val normals = mutableListOf<Triple<Double, Double, Double>>()
        val uvs = mutableListOf<Pair<Double, Double>>()
        val vertexIndices = mutableListOf<Int>()
        val normalIndices = mutableListOf<Int>()
        val uvIndices = mutableListOf<Int>()

        for (line in readText.lines()) {
            when {
                line.startsWith("o ") -> name = stripHeader(line)[0]
                line.startsWith("v ") -> vertexes.add(parseTriple(toDouble(stripHeader(line))))
                line.startsWith("vn ") -> normals.add(parseTriple(toDouble(stripHeader(line))))
                line.startsWith("vt ") -> uvs.add(parsePair(toDouble(stripHeader(line))))
                line.startsWith("f ") -> {
                    stripHeader(line).forEach {
                        val parts = it.split("/").map { s -> s.toInt() }
                        vertexIndices.add(parts[0])
                        normalIndices.add(parts[1])
                        uvIndices.add(parts[2])
                    }
                }
            }
        }

        return Obj(
            name,
            vertexes.toTypedArray(),
            normals.toTypedArray(),
            uvs.toTypedArray(),
            vertexIndices.toTypedArray(),
            uvIndices.toTypedArray(),
            normalIndices.toTypedArray()
        )
    }

    private fun parseTriple(parsed: List<Double>): Triple<Double, Double, Double> {
        return Triple(parsed[0], parsed[1], parsed[2])
    }

    private fun parsePair(parts: List<Double>): Pair<Double, Double> {
        return Pair(parts[0], parts[1])
    }

    private fun toDouble(parts: List<String>): List<Double> {
        return parts.map { it.toDouble() }
    }

    private fun stripHeader(line: String): List<String> {
        val parts = line.split(" ")
        return parts.subList(1, parts.size)
    }

}