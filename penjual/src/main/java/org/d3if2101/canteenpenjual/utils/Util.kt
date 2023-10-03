package org.d3if2101.canteenpenjual.utils

import java.util.concurrent.Executors


// Fungsi Untuk Suspend Function
private val SINGLE_EXECUTOR = Executors.newSingleThreadExecutor()
fun executeThread(f: () -> Unit) {
    SINGLE_EXECUTOR.execute(f)
}