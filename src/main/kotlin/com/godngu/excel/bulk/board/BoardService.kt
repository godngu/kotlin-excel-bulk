package com.godngu.excel.bulk.board

import com.godngu.excel.bulk.excel.ExcelWriteModel
import com.godngu.excel.bulk.excel.ExcelWriter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class BoardService(
    private val jdbcTemplate: JdbcTemplate,
) {

    fun excelDownload() {
        val file = createFile("src/main/resources/test")

        val producer = ExcelBoardProducer<ExcelWriteModel>(jdbcTemplate)
        ExcelWriter(producer = producer, type = BoardDto2::class).write(FileOutputStream(file))
    }

    private fun createFile(filename: String): File {
        return File("${addSuffix(filename)}.xlsx")
    }

    private fun addSuffix(filename: String): String {
        return filename + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
    }
}
