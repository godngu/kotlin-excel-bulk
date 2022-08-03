package com.godngu.excel.bulk.order.temp

enum class MissingProductionStatusType(override val description: String) : EnumDocumentType {
    ALL("전체"),
    MISSING("미출"),
    CANCEL("미출복원")
}
