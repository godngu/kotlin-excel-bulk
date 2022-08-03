package com.godngu.excel.bulk.order.temp

class SearchOrderQuery(
    val searchInput: SearchCategoryKeywordInput,
    val period: SearchPeriod,
    val partner: PartnerType?,
    val orderType: OrderType?,
    val orderStatus: List<OrderStatusType>,
    val paymentGatewayId: PaymentGatewayIdType?,
    val clusterCenterCodes: ClusterCenterCodeType?,
    val hasFailedDelivery: Boolean?,
    val fulfillmentOwnerID: FulfillmentOwnerIdType?,
    val deliveryPolicy: DeliveryPolicyType?,
    val courier: CourierType?,
    val isFirstPurchased: Boolean?,
    val isSpecialCare: Boolean?,
    val isDeliveryOrder: Boolean?,
    val isMissingProduction: Boolean?,
    val isDeferredDelivery: Boolean?,
    val categoryIds: List<Long>?,

)
