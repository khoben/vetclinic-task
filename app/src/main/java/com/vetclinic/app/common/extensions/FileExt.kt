package com.vetclinic.app.common.extensions

import java.io.File

/**
 * Deletes everything except root file
 */
fun File.deleteRecursivelyWithoutRoot(): Boolean =
    walkBottomUp()
        .filter { it != this }
        .fold(true) { res, it -> (it.delete() || !it.exists()) && res }