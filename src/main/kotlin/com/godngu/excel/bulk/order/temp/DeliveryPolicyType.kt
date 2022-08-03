package com.godngu.excel.bulk.order.temp

enum class DeliveryPolicyType(override val description: String, val isDeliveryOrder: Boolean) : EnumDocumentType {
    ALL("전체", false),
    DAWN("샛별배송", true),
    DAY_PARCEL("낮배송(택배)", true),
    MANUAL_DAY_PARCEL("낮배송(섭외)", true),
    GOURMET_DELIVERY("미식딜리버리", true),
    NORMAL_PARCEL("판매자배송", true),
    INSTALLATION_DELIVERY("설치배송", true),
    ONLINE_TICKET("무배송(온라인 교환권)", false),
    AIRLINE_TICKET("무배송(항공권)", false),
    SELF_PICKUP_WINE("무배송(셀프픽업-와인)", false),
}
