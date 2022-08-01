package com.godngu.excel.bulk.board

import com.godngu.excel.bulk.excel.ExcelHeader
import com.godngu.excel.bulk.excel.ExcelWriteModel

class BoardDto2(
    @ExcelHeader(name = "번호")
    override val seq: Int,

    @ExcelHeader(name = "게시판 번호")
    val boardId: Long,

    @ExcelHeader(name = "제목")
    val title: String,

    @ExcelHeader(name = "내용")
    val content: String,
) : ExcelWriteModel
