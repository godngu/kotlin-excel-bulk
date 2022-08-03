package com.godngu.excel.bulk.order.temp

enum class CourierType(override val description: String) : EnumDocumentType {
    ALL("전체"),
    FRS("프레시솔루션"),
    CJT("CJ택배"),
    LTT("롯데택배"),
    SWEET_TRACKER("스윗트래커"),
    NONE("미배송");

    fun displayName(): String {
        return when (this) {
            FRS -> "넥스트마일"
            NONE -> "없음"
            else -> description
        }
    }
}
