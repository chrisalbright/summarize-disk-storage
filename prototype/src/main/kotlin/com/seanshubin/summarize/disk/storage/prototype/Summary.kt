package com.seanshubin.summarize.disk.storage.prototype

import java.nio.file.Path

data class Summary(
    val path: Path,
    val fileQuantity: Int,
    val dirQuantity: Int,
    val size: Long
):Comparable<Summary> {
    override fun compareTo(other: Summary): Int =
        this.size.compareTo(other.size)

    companion object {
        fun combineDir(path: Path, summaries: List<Summary>): Summary {
            var newFileQuantity = 0
            var newDirQuantity = 1
            var newSize = 0L
            summaries.forEach { summary ->
                newFileQuantity += summary.fileQuantity
                newDirQuantity += summary.dirQuantity
                newSize += summary.size
            }
            return Summary(path, newFileQuantity, newDirQuantity, newSize)
        }
    }
}
