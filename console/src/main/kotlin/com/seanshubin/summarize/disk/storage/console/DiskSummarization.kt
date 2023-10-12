package com.seanshubin.summarize.disk.storage.console

import java.io.PrintWriter
import java.nio.file.Path
import java.nio.file.Paths

object DiskSummarization {
    @JvmStatic
    fun main(args: Array<String>) {
        val path = pathFromArgs(args)
        val summary = summarizePath(path)
        val writer = System.console()?.writer() ?: PrintWriter(System.out)
        display(summary, writer)
        writer.flush()
        writer.close()
    }

}
fun pathFromArgs(args: Array<String>): Path = Paths.get(args.getOrNull(0) ?: ".")

fun summarizePath(path: Path): List<FileSummary> = listOf()

fun display(summary: List<FileSummary>, out: PrintWriter) {
    out.println("Hello world")
}
data class FileSummary(val path: Path)