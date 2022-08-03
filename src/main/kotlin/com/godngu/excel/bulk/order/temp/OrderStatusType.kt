package com.godngu.excel.bulk.order.temp

enum class OrderStatusType(override val description: String, val step: Int) : EnumDocumentType {
    ALL("전체", 0),
    INIT("주문시도", 1),
    COMPLETED("주문완료", 2),
    PRODUCING("배송준비중", 3),
    DELIVERING("배송중", 4),
    DELIVERED("배송완료", 5),
    CONFIRMED("구매확정", 6),
}
