package com.godngu.excel.bulk.order

import com.godngu.excel.bulk.excel.AbstractExcelJdbcProducer
import com.godngu.excel.bulk.excel.ExcelWriteModel
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import java.util.concurrent.atomic.AtomicInteger

class OrderSearchExcelProducer(
    override val jdbcTemplate: JdbcTemplate,
    override val rowMapper: RowMapper<ExcelWriteModel>,
    override val whereClause: SqlWhereClause,
) : AbstractExcelJdbcProducer() {

    override fun totalCount(): Int {
        return jdbcTemplate.queryForObject(SearchOrderSql.createTotalCountSql(whereClause), Int::class.java) ?: 0
    }

    override fun call(): AtomicInteger {
        val count = AtomicInteger()

        val stream = jdbcTemplate.queryForStream(SearchOrderSql.createSql(whereClause), rowMapper)
        stream.forEach {
            count.getAndIncrement()
            queue.put(it)
        }

        return count
    }
}
