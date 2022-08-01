package com.godngu.excel.bulk.board

import com.godngu.excel.bulk.excel.ExcelWriteModel
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class BoardMapper: RowMapper<ExcelWriteModel> {

    override fun mapRow(rs: ResultSet, rowNum: Int): ExcelWriteModel {
        return BoardDto2(
            seq = rs.getInt("seq"),
            boardId = rs.getLong("board_id"),
            title = rs.getString("title"),
            content = rs.getString("content"),
        )
    }
}
