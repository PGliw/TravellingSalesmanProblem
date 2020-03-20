package main.problem

import java.io.File
import java.io.FileWriter

interface ILogger {
    fun log(text: String)
    fun logLine(line: String) = log("$line\n")
    fun close() = run { }
}

class ConsoleLogger : ILogger {
    override fun log(text: String) {
        print(text)
    }
}

class CsvFileLogger(private val filepath: String) : ILogger {

    private val fileWriter by lazy {
        FileWriter(filepath, true)
    }

    override fun log(text: String) {
        fileWriter.write(text)
    }

    override fun close(){
        fileWriter.close()
    }
}