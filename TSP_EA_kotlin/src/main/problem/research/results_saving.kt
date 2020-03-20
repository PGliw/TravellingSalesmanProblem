package main.problem.research

import java.io.File

fun List<ExaminationResult>.saveToFile(
    filepath: String,
    prefixHeader: String,
    prefixes: List<String>
){
    File(filepath).printWriter().use { out ->
        out.println("$prefixHeader; best; worst; avg; std")
        prefixes.zip(this).forEach {
            out.println("${it.first}; ${it.second.best}; ${it.second.worst}; ${it.second.avg}; ${it.second.std};")
        }
    }
}