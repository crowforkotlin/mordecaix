package com.crow.mordecaix.zipline.impl

import com.crow.mordecaix.zipline.services.BaseLogger
import org.slf4j.LoggerFactory

class Logger : BaseLogger {

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