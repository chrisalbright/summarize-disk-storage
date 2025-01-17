package com.seanshubin.summarize.disk.storage.prototype

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Clock
import java.time.Duration

object PrototypeApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val baseName = args.getOrNull(0) ?: "."
        val base = Paths.get(baseName)
        timeTaken {
            summarizeDiskStorage(base)
        }
    }

    private fun summarizeDiskStorage(path: Path) {
        val paths = if (Files.isDirectory(path)) {
            listFiles(path)
        } else {
            listOf(path)
        }
        summarizeDiskStoragePaths(paths)
    }

    private fun summarizeDiskStoragePaths(paths: List<Path>) {
        paths
            .map(::loadSummary)
            .sortedDescending()
            .map(::formatSummary)
            .forEach(::println)
    }

    private fun loadSummary(path: Path): Summary =
        if (Files.isDirectory(path)) {
            loadSummaryFromDir(path)
        } else {
            loadSummaryFromFile(path)
        }

    private fun loadSummaryFromDir(dir: Path): Summary {
        val summaries = listFiles(dir).map(::loadSummary)
        return Summary.combineDir(dir, summaries)
    }

    private fun loadSummaryFromFile(file: Path): Summary {
        val size = Files.size(file)
        return Summary(file, size, 1, 0)
    }

    private fun timeTaken(f: () -> Unit) {
        val clock = Clock.systemUTC()
        val startTime = clock.instant()
        f()
        val endTime = clock.instant()
        val duration = Duration.between(startTime, endTime)
        val durationString = formatDuration(duration)
        println(durationString)
    }

    private fun formatDuration(duration: Duration): String {
        val hours = String.format("%02d", duration.toHoursPart())
        val minutes = String.format("%02d", duration.toMinutesPart())
        val seconds = String.format("%02d", duration.toSecondsPart())
        return "$hours:$minutes:$seconds"
    }

    private fun formatSummary(summary: Summary): String =
        "${summary.size} ${summary.path} files:${summary.files} dirs:${summary.dirs}"

    private fun listFiles(dir: Path): List<Path> =
        Files.list(dir).use { it.toList() }

    data class Summary(
        val path: Path,
        val size: Long,
        val files: Int,
        val dirs: Int
    ) : Comparable<Summary> {
        override fun compareTo(other: Summary): Int = size.compareTo(other.size)

        companion object {
            fun combineDir(path: Path, summaries: List<Summary>): Summary {
                var newFileQuantity = 0
                var newDirQuantity = 1
                var newSize = 0L
                summaries.forEach { summary ->
                    newFileQuantity += summary.files
                    newDirQuantity += summary.dirs
                    newSize += summary.size
                }
                return Summary(path, newSize, newFileQuantity, newDirQuantity)
            }
        }
    }
}
