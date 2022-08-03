package com.godngu.excel.bulk.order.temp

import com.godngu.excel.bulk.order.temp.SearchPeriod.PeriodType.DAY
import com.godngu.excel.bulk.order.temp.SearchPeriod.PeriodType.MONTH
import com.godngu.excel.bulk.order.temp.SearchPeriod.PeriodType.YEAR

enum class SearchOrderCategoryType(override val description: String, override val isDigit: Boolean, val searchRange: Int, val searchPeriod: SearchPeriod.PeriodType) :
    EnumDocumentType, SearchCategoryType {
    ALL("전체", false, 1, DAY),
    GROUP_ORDER_NO("대표주문번호", true, 1, YEAR),
    ORDER_NO("개별주문번호", true, 1, YEAR),
    MEMBER_ID("주문자ID", false, 1, MONTH),
    MEMBER_NO("회원번호", true, 1, MONTH),
    PHONE_NUMBER("핸드폰번호", false, 1, MONTH),
    MASTER_CODE("마스터코드", false, 1, DAY),
    DEAL_PRODUCT_NO("딜번호", true, 1, DAY),
    FULFILLMENT_OWNER_ID("퓰필먼트ID", false, 1, DAY),
    PARTNER_ID("파트너ID", false, 1, DAY),
    PARTNER_NAME("파트너이름", false, 1, DAY),
}
