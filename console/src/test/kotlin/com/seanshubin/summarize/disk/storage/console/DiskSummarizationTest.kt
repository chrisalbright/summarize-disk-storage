package com.seanshubin.summarize.disk.storage.console

import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertEquals

class DiskSummarizationTest {

    @Test fun `can get path from args array`(){
        // given
        val testFile = Files.createTempFile("summarization", "")
        val testFilePathName = testFile.toString()
        // when
        val testFileFromArgs = pathFromArgs(arrayOf(testFilePathName))
        // then
        assertEquals(testFile, testFileFromArgs)
    }

    @Test
    fun `defaults to current directory when no argument supplied`(){
        // given
        val cwd = Paths.get(".")
        // when
        val testFileFromArgs = pathFromArgs(arrayOf())
        // then
        assertEquals(cwd, testFileFromArgs)
    }
}