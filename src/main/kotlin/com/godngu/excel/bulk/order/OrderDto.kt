package com.godngu.excel.bulk.order

import com.godngu.excel.bulk.excel.ExcelHeader
import com.godngu.excel.bulk.excel.ExcelWriteModel

class OrderDto(
    @ExcelHeader(name = "번호")
    override val seq: Int,

    @ExcelHeader(name = "주문 번호")
    val orderNo: Long,

    @ExcelHeader(name = "결제 금액")
    val totalPaymentPrice: Long,

    @ExcelHeader(name = "대표주문번호")
    val groupOrderNo: Long,

    @ExcelHeader(name = "주문유형")
    val orderType: String,

    @ExcelHeader(name = "파트너이름")
    val partnerName: String,

    @ExcelHeader(name = "파트너ID")
    val partnerId: String,

    @ExcelHeader(name = "풀필먼트ID")
    val fulfillmentOwnerId: String,

    @ExcelHeader(name = "주문상품명")
    val orderProductName: String,

    @ExcelHeader(name = "주문상태")
    val orderStatus: String,

    @ExcelHeader(name = "전체취소여부")
    val allRefunded: String,

    @ExcelHeader(name = "회원번호")
    val ordererMemberNo: Long,

    @ExcelHeader(name = "주문자ID")
    val ordererMemberId: String,

    @ExcelHeader(name = "주문자명")
    val ordererName: String,

    @ExcelHeader(name = "주문자핸드폰")
    val ordererPhoneNumber: String,

    @ExcelHeader(name = "주문일시")
    val orderDate: String,

    @ExcelHeader(name = "결제일시")
    val paymentDate: String,

    @ExcelHeader(name = "수령자명")
    val receiverName: String,

    @ExcelHeader(name = "수령자핸드폰")
    val receiverPhoneNumber: String,

    @ExcelHeader(name = "(신)우편번호")
    val zipcode: String,

    @ExcelHeader(name = "(신)도로명주소")
    val address: String,

    @ExcelHeader(name = "상세주소")
    val addressDetail: String,

    @ExcelHeader(name = "배송요청사항")
    val memo: String?,

    @ExcelHeader(name = "배송유형")
    val deliveryPolicy: String,

    @ExcelHeader(name = "배송사")
    val courierType: String?,

    @ExcelHeader(name = "수령장소")
    val pickupType: String,

    @ExcelHeader(name = "수령장소상세")
    val pickupDetail: String?,

    @ExcelHeader(name = "센터코드")
    val clusterCenterCode: String,

    @ExcelHeader(name = "권역정보")
    val regionGroup: String,

    @ExcelHeader(name = "결제사")
    val paymentGatewayId: String,

    @ExcelHeader(name = "결제방식")
    val applicationType: String,

    @ExcelHeader(name = "총 상품 금액")
    val totalDealProductPriceBalance: Long,

    @ExcelHeader(name = "상품할인")
    val totalDealProductDiscountPriceBalance: Long,

    @ExcelHeader(name = "쿠폰할인")
    val totalCouponDiscountPriceBalance: Long,

    @ExcelHeader(name = "사용적립금")
    val totalUsedPointBalance: Long,

    @ExcelHeader(name = "배송비")
    val deliveryPriceBalance: Long,

    @ExcelHeader(name = "사용쿠폰명")
    val couponName: String?,

    @ExcelHeader(name = "회원등급")
    val memberGroup: String,

    @ExcelHeader(name = "첫구매여부")
    val isFirstPurchased: String,

    @ExcelHeader(name = "스페셜케어")
    val isSpecialCare: String
) : ExcelWriteModel
