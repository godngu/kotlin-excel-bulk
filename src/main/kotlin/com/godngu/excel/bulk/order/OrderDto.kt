package com.godngu.excel.bulk.order

import com.godngu.excel.bulk.excel.ExcelHeader
import com.godngu.excel.bulk.excel.ExcelWriteModel

class OrderDto(
    @ExcelHeader(name = "번호")
    override val seq: Int,

    @ExcelHeader(name = "주문 번호")
    val orderNo: Long,

    @ExcelHeader(name = "결제 금액")
    val totalPaymentPrice: Long,
): ExcelWriteModel
