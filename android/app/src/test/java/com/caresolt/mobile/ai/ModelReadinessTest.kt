package com.caresolt.mobile.ai

import java.io.File
import kotlin.io.path.createTempDirectory
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ModelReadinessTest {
    @Test
    fun missingModelIsNotReady() {
        val directory = createTempDirectory("caresolt-ai-test").toFile()
        try {
            assertFalse(ModelReadiness.isReady(File(directory, "model.litertlm")))
        } finally {
            directory.deleteRecursively()
        }
    }

    @Test
    fun emptyModelFileIsNotReady() {
        val directory = createTempDir(prefix = "caresolt-ai-test")
        val model = File(directory, "model.litertlm")
        try {
            model.createNewFile()
            assertFalse(ModelReadiness.isReady(model))
        } finally {
            directory.deleteRecursively()
        }
    }

    @Test
    fun nonEmptyModelFileIsReady() {
        val directory = createTempDir(prefix = "caresolt-ai-test")
        val model = File(directory, "model.litertlm")
        try {
            model.writeBytes(byteArrayOf(1, 2, 3))
            assertTrue(ModelReadiness.isReady(model))
        } finally {
            directory.deleteRecursively()
        }
    }
}
