package com.godngu.excel.bulk.order.temp

enum class ClusterCenterCodeType(override val description: String) : EnumDocumentType {
    ALL("전체"),
    CC01("장지"),
    CC02("김포"),
    NONE("미배송");

    fun displayName(): String {
        return when (this) {
            CC01 -> "송파"
            NONE -> "없음"
            else -> this.description
        }
    }
}
