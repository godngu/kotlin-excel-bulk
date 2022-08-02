package com.godngu.excel.bulk.board

import com.godngu.excel.bulk.excel.AbstractExcelJdbcProducer
import com.godngu.excel.bulk.excel.ExcelWriteModel
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import java.util.concurrent.atomic.AtomicInteger

class ExcelBoardProducer(
    override val jdbcTemplate: JdbcTemplate,
    override val rowMapper: RowMapper<ExcelWriteModel>,
): AbstractExcelJdbcProducer() {

    override fun totalCount(): Int {
        val sql = "select count(*) from board"
        return jdbcTemplate.queryForObject(sql, Int::class.java) ?: 0
    }

    override fun call(): AtomicInteger {
        val count = AtomicInteger()

        val sql = "select rownum as seq, board_id, title, content from board order by board_id desc"
        val stream = jdbcTemplate.queryForStream(sql, rowMapper)
        stream.forEach {
            count.getAndIncrement()
            queue.put(it)
        }

        return count
    }
}
