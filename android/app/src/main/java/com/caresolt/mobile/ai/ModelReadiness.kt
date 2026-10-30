package com.caresolt.mobile.ai

import java.io.File

/**
 * Checks whether a model file is present and non-empty before any runtime load.
 * This deliberately does not download, extract, or execute model content.
 */
object ModelReadiness {
    fun isReady(modelFile: File): Boolean =
        modelFile.exists() && modelFile.isFile && modelFile.length() > 0L
}
