package com.godngu.excel.bulk.board

import com.godngu.excel.bulk.excel.AbstractExcelJdbcProducer
import com.godngu.excel.bulk.excel.ExcelWriteModel
import org.springframework.jdbc.core.JdbcTemplate
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Stream

class ExcelBoardProducer<out T : ExcelWriteModel>(
    override val jdbcTemplate: JdbcTemplate
): AbstractExcelJdbcProducer() {

    override fun totalCount(): Int {
        val sql = "select count(*) from board"
        return jdbcTemplate.queryForObject(sql, Int::class.java) ?: 0
    }

    override fun call(): AtomicInteger {
        val count = AtomicInteger()

        val sql = "select rownum as seq, board_id, title, content from board order by board_id desc"
        val stream: Stream<ExcelWriteModel> = jdbcTemplate.queryForStream(sql, BoardMapper())
        stream.forEach {
            count.getAndIncrement()
            queue.put(it)
        }

        return count
    }
}
