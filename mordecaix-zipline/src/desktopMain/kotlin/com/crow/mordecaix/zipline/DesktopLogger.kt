@file:Suppress("SpellCheckingInspection")

package com.crow.mordecaix.zipline

import org.slf4j.LoggerFactory

class DesktopLogger : BaseLogger {

    private val logger = LoggerFactory.getLogger("mordecaix-logger")

    override fun info(message: String) {
        logger.info(message)
    }

    override fun error(message: String) {
        logger.error(message)
    }

    override fun warn(message: String) {
        logger.warn(message)
    }
}