package com.example

import java.io.File

class Logger(filePath: File?) {
    private val file = filePath?.printWriter()

    fun log(string: String) {
        println(string)
        file?.let {
            it.println(string)
            it.flush()
        }
    }
}