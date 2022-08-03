package com.godngu.excel.bulk.order.temp

enum class PaymentGatewayIdType(val paymentApiCode: String, override val description: String) : EnumDocumentType {
    ALL("all", "전체"),
    CREDIT_CARD("credit-card", "신용카드"),
    NAVER_PAY("naver-pay", "네이버페이"),
    TOSS("toss", "토스"),
    TOSS_PAYMENTS("toss-payments", "토스페이먼츠"),
    SMILE_PAY("smile-pay", "스마일페이"),
    PAYCO("payco", "페이코"),
    CHAI("chai", "차이"),
    KAKAO_PAY("kakao-pay", "카카오페이"),
    MOBILIANS("mobilians", "모빌리언스"),
    TOSS_PAYMENTS_SUBSCRIPTION("toss-payments-subscription", "토스페이먼츠 정기결제"),
    KURLY("kurly", "컬리 포인트");

    companion object {
        fun fromPaymentApiCode(paymentApiCode: String): PaymentGatewayIdType {
            for (value in values()) {
                if (value.paymentApiCode == paymentApiCode) return value
            }
            throw RuntimeException("정의되지 않은 PG사 ID 입니다")
        }
    }

    fun displayName(): String {
        return when (this) {
            TOSS_PAYMENTS -> "신용카드"
            KURLY -> "전액할인(적립금)"
            else -> this.description
        }
    }
}
