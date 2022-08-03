package com.godngu.excel.bulk.order.temp

enum class FulfillmentOwnerIdType(override val description: String) : EnumDocumentType {
    ALL("전체"),
    KURLY("컬리"),
    OTHERS("컬리외배송"),
    NONE("없음"),
}
