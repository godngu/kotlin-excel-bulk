package com.godngu.excel.bulk.order.temp

enum class PartnerType(override val description: String) : EnumDocumentType {
    ALL("전체"),
    KURLY("컬리"), // 1P
    PARTNER("파트너") // 3P
}
