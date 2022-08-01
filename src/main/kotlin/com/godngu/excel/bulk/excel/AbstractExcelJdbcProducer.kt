package com.godngu.excel.bulk.excel

import org.springframework.jdbc.core.JdbcTemplate
import java.util.concurrent.Callable
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicInteger

abstract class AbstractExcelJdbcProducer : Callable<AtomicInteger> {

    abstract val jdbcTemplate: JdbcTemplate

    lateinit var queue: LinkedBlockingQueue<ExcelWriteModel>

    abstract fun totalCount(): Int
}
