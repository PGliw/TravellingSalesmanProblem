package main.problem

import java.io.FileWriter

interface ILogger {
    fun log(text: String)
    fun logLine(line: String) = log("$line\n")
    fun close() = run { }
    fun isDetailed() = false
}

class ConsoleLogger(private val showDetails: Boolean = false) : ILogger {
    override fun log(text: String) {
        print(text)
    }

    override fun isDetailed() = showDetails
}

class FileLogger(
    private val filepath: String,
    private val showDetails: Boolean = false,
    private val rowPrefix: String? = null
    ) : ILogger {

    private val fileWriter by lazy {
        FileWriter(filepath, true)
    }

    override fun log(text: String) {
        val fullLog = (rowPrefix?.plus(";") ?: "") + text
        fileWriter.write(fullLog)
    }

    override fun close() {
        fileWriter.close()
    }

    override fun isDetailed() = showDetails
}