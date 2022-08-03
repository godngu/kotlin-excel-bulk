package com.godngu.excel.bulk.order

data class SqlWhereClause(
    val sql: String
) {
    val isNotEmpty = sql.isNotEmpty()
}
