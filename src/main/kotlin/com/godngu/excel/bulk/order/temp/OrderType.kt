package com.godngu.excel.bulk.order.temp

enum class OrderType(override val description: String) : EnumDocumentType {
    ALL("전체"),
    NORMAL("일반주문"),
    GIFT("선물주문"),
    MASS("대량주문"),
    COPIED("재주문"),
}
