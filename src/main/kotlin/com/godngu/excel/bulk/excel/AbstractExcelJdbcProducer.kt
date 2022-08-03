package com.godngu.excel.bulk.excel

import com.godngu.excel.bulk.order.SqlWhereClause
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import java.util.concurrent.Callable
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicInteger

abstract class AbstractExcelJdbcProducer : Callable<AtomicInteger> {

    abstract val jdbcTemplate: JdbcTemplate

    abstract val rowMapper: RowMapper<ExcelWriteModel>

    abstract val whereClause: SqlWhereClause

    abstract fun totalCount(): Int

    lateinit var queue: LinkedBlockingQueue<ExcelWriteModel>

}
