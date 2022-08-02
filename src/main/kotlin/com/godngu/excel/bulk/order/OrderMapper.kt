package com.godngu.excel.bulk.order

import com.godngu.excel.bulk.excel.ExcelWriteModel
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class OrderMapper : RowMapper<ExcelWriteModel> {

    override fun mapRow(rs: ResultSet, rowNum: Int): ExcelWriteModel {
        return OrderDto(
            seq = rowNum + 1,
            orderNo = rs.getLong("order_no"),
            totalPaymentPrice = rs.getLong("total_payment_price"),
        )
    }
}
