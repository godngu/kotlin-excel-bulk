package com.godngu.excel.bulk.order

import com.godngu.excel.bulk.order.temp.Hasher.ALGORITHM.SHA512
import com.godngu.excel.bulk.order.TableAlias.GROUP_ORDER
import com.godngu.excel.bulk.order.TableAlias.MISSING_PRODUCTION_LOG
import com.godngu.excel.bulk.order.TableAlias.MISSING_PRODUCTION_UPLOAD
import com.godngu.excel.bulk.order.TableAlias.ORDER
import com.godngu.excel.bulk.order.TableAlias.ORDERER
import com.godngu.excel.bulk.order.TableAlias.ORDERER_MEMBER_META
import com.godngu.excel.bulk.order.TableAlias.ORDER_DEAL_PRODUCT
import com.godngu.excel.bulk.order.temp.BusinessException
import com.godngu.excel.bulk.order.temp.ClusterCenterCodeType
import com.godngu.excel.bulk.order.temp.CourierType
import com.godngu.excel.bulk.order.temp.DeliveryPolicyType
import com.godngu.excel.bulk.order.temp.ErrorCode
import com.godngu.excel.bulk.order.temp.FulfillmentOwnerIdType
import com.godngu.excel.bulk.order.temp.Hasher
import com.godngu.excel.bulk.order.temp.MissingProductionStatusType
import com.godngu.excel.bulk.order.temp.OrderStatusType
import com.godngu.excel.bulk.order.temp.OrderType
import com.godngu.excel.bulk.order.temp.PartnerType
import com.godngu.excel.bulk.order.temp.PaymentGatewayIdType
import com.godngu.excel.bulk.order.temp.SearchCategoryKeywordInput
import com.godngu.excel.bulk.order.temp.SearchCategoryType
import com.godngu.excel.bulk.order.temp.SearchOrderCategoryType
import com.godngu.excel.bulk.order.temp.SearchOrderQuery
import com.godngu.excel.bulk.order.temp.SearchPeriod

object SearchOrderWhereClause {

    private const val EMPTY_STRING = ""

    fun create(condition: SearchOrderQuery): SqlWhereClause {
        return SqlWhereClause(
            StringBuilder()
                .append(categoryAndKeyword(condition.searchInput))
                .append(partnerType(condition.partner))
                .append(orderType(condition.orderType))
                .append(orderStatus(condition.orderStatus))
                .append(paymentGatewayId(condition.paymentGatewayId))
                .append(clusterCenterCode(condition.clusterCenterCodes))
                .append(courierType(condition.courier))
                .append(deliveryPolicyType(condition.deliveryPolicy))
                .append(isFirstPurchased(condition.isFirstPurchased))
                .append(isSpecialCare(condition.isSpecialCare))
                .append(isDeliveryOrder(condition.isDeliveryOrder))
                .append(fulfillmentOwner(condition.fulfillmentOwnerID))
                .append(isDeferredDelivery(condition.isDeferredDelivery))
                .append(hasFailedDelivery(condition.hasFailedDelivery))
                .append(hasMissingProduction(condition.isMissingProduction))
                .append(includesProductWithCategoryIds(condition.categoryIds))
                .append(betweenDate(condition.period))
                .toString()
        )
    }

    private fun categoryAndKeyword(searchInput: SearchCategoryKeywordInput): String {
        val category: SearchCategoryType? = searchInput.category
        // TODO 키워드가 없을 수 있는가?
        val keyword = searchInput.keywordInput ?: ""

        val clause = when (category) {
            SearchOrderCategoryType.ALL -> {
                return EMPTY_STRING
            }
            SearchOrderCategoryType.GROUP_ORDER_NO -> {
                "$GROUP_ORDER.group_order_no = ${keyword.toLong()}"
            }
            SearchOrderCategoryType.ORDER_NO -> {
                "$ORDER.order_no = ${keyword.toLong()}"
            }
            SearchOrderCategoryType.MEMBER_ID -> {
                "$ORDERER.member_id_hash = ${keyword.let { Hasher.convert(it, SHA512) }}"
            }
            SearchOrderCategoryType.MEMBER_NO -> {
                "$ORDERER.member_no = ${keyword.toLong()}"
            }
            SearchOrderCategoryType.PHONE_NUMBER -> {
                "$ORDERER.phone_number_hash = ${keyword.let { Hasher.convert(it, SHA512) }}"
            }
            SearchOrderCategoryType.MASTER_CODE -> {
                "$ORDER_DEAL_PRODUCT.master_product_code = $keyword"
            }
            SearchOrderCategoryType.DEAL_PRODUCT_NO -> {
                "$ORDER_DEAL_PRODUCT.deal_product_no = ${keyword.toLong()}"
            }
            SearchOrderCategoryType.FULFILLMENT_OWNER_ID -> {
                "$ORDER.fulfillment_owner_id = $keyword"
            }
            SearchOrderCategoryType.PARTNER_ID -> {
                "$ORDER.partner_id = $keyword"
            }
            SearchOrderCategoryType.PARTNER_NAME -> {
                "$ORDER.partner_name = $keyword"
            }
            else -> {
                throw BusinessException(ErrorCode.INVALID_INPUT_VALUE)
            }
        }
        return andOrEmpty(clause)
    }

    /**
     * 파트너 ID로 주문을 검색하는 조건
     */
    private fun partnerType(partnerType: PartnerType?): String {
        val partnerId = "partner_id"
        val clause = when (partnerType) {
            null -> null
            PartnerType.KURLY -> "$ORDER.$partnerId = ${PartnerType.KURLY}"
            else -> "$ORDER.$partnerId != ${PartnerType.KURLY}"
        }
        return andOrEmpty(clause)
    }

    /**
     * 개별 주문의 유형에 따른 검색 조건
     */
    private fun orderType(orderType: OrderType?): String {
        return orderType?.let { andOrEmpty("$ORDER.order_type = $orderType") } ?: EMPTY_STRING
    }

    /**
     * 개별 주문의 상태에 따른 검색 조건
     */
    private fun orderStatus(orderStatus: List<OrderStatusType>): String {
        if (orderStatus.isEmpty()) return EMPTY_STRING
        val condition = orderStatus.joinToString(", ") { "'$it'" }
        return andOrEmpty("$ORDER.order_status in ($condition)")
    }

    /**
     * 결제 수단에 따른 검색 조건
     */
    private fun paymentGatewayId(paymentGatewayId: PaymentGatewayIdType?): String {
        return paymentGatewayId?.let {
            andOrEmpty("$GROUP_ORDER.payment_gateway_id = $paymentGatewayId")
        } ?: EMPTY_STRING
    }

    /**
     * 클러스터 센터에 따른 검색 조건
     */
    private fun clusterCenterCode(clusterCenterCodes: ClusterCenterCodeType?): String {
        return clusterCenterCodes?.let {
            andOrEmpty("$ORDER.cluster_center_code = $clusterCenterCodes")
        } ?: EMPTY_STRING
    }

    /**
     * 택배사에 따른 검색 조건
     */
    private fun courierType(courier: CourierType?): String {
        return courier?.let {
            andOrEmpty("$ORDER.courier = $courier")
        } ?: EMPTY_STRING
    }

    /**
     * 배송 유형에 따른 검색 조건
     */
    private fun deliveryPolicyType(deliveryPolicy: DeliveryPolicyType?): String {
        return deliveryPolicy?.let {
            andOrEmpty("$ORDER.delivery_policy = $deliveryPolicy")
        } ?: EMPTY_STRING
    }

    /**
     * 첫 주문인지 확인하는 검색 조건
     */
    private fun isFirstPurchased(firstPurchased: Boolean?): String {
        return firstPurchased?.let {
            andOrEmpty("$ORDERER_MEMBER_META.is_first_purchased = $firstPurchased")
        } ?: EMPTY_STRING
    }

    /**
     * 스페셜 케어인지 확인하는 검색 조건
     */
    private fun isSpecialCare(specialCare: Boolean?): String {
        return specialCare?.let {
            andOrEmpty("$ORDERER_MEMBER_META.is_special_care = $specialCare")
        } ?: EMPTY_STRING
    }

    /**
     * 미배송 주문인지 확인하는 검색 조건
     */
    private fun isDeliveryOrder(deliveryOrder: Boolean?): String {
        if (deliveryOrder == null) return EMPTY_STRING
        val deliveryPolicyTypes = DeliveryPolicyType.values()
        val deliveryPolicy = "delivery_policy"
        val clause =
            if (deliveryOrder) "$ORDER.$deliveryPolicy in (${deliveryPolicyTypes.filter { it.isDeliveryOrder }})"
            else "$ORDER.$deliveryPolicy in (${deliveryPolicyTypes.filter { !it.isDeliveryOrder }})"
        return andOrEmpty(clause)
    }

    /**
     * 배송 주체를 검색하는 조건
     */
    private fun fulfillmentOwner(fulfillmentOwner: FulfillmentOwnerIdType?): String {
        if (fulfillmentOwner == null) return EMPTY_STRING
        val fulfillmentOwnerId = "fulfillment_owner_id"
        val clause = when (fulfillmentOwner) {
            FulfillmentOwnerIdType.KURLY -> {
                "$ORDER.$fulfillmentOwnerId = ${FulfillmentOwnerIdType.KURLY}"
            }
            FulfillmentOwnerIdType.OTHERS -> {
                "$ORDER.$fulfillmentOwnerId != ${FulfillmentOwnerIdType.KURLY} and $ORDER.$fulfillmentOwnerId is not null "
            }
            else -> {
                "$ORDER.$fulfillmentOwnerId is null"
            }
        }
        return andOrEmpty(clause)
    }

    /**
     * 개별 주문의 상태에 따른 검색 조건
     */
    private fun isDeferredDelivery(deferredDelivery: Boolean?): String {
        return deferredDelivery?.let {
            andOrEmpty("$ORDER.is_deferred_delivery = $deferredDelivery")
        } ?: EMPTY_STRING
    }

    /**
     * TODO 쿼리 다시 살펴보기!!!
     * 배송이 되지 않은 주문을 검색하는 조건
     */
    private fun hasFailedDelivery(hasFailedDelivery: Boolean?): String {
        if (hasFailedDelivery == null || !hasFailedDelivery) return EMPTY_STRING
        return andOrEmpty("")
    }

    /**
     * TODO 쿼리 다시 살펴보기!!!
     * 미출 등록된 주문을 검색하는 조건
     */
    private fun hasMissingProduction(missingProduction: Boolean?): String {
        if (missingProduction == null) return EMPTY_STRING
        val clause = "$MISSING_PRODUCTION_LOG.status = ${MissingProductionStatusType.MISSING} and $MISSING_PRODUCTION_UPLOAD.success = true"
        return andOrEmpty(clause)
    }

    /**
     * TODO 쿼리 다시 살펴보기!!!
     */
    private fun includesProductWithCategoryIds(categoryIds: List<Long>?): String {
        if (categoryIds == null || categoryIds.isEmpty()) return EMPTY_STRING
        return andOrEmpty("")
    }

    /**
     * 검색 기간
     */
    private fun betweenDate(period: SearchPeriod): String {
        return andOrEmpty("$ORDER.created_at between '${period.startDate}' and '${period.endDate}'")
    }

    private fun andOrEmpty(clause: String?): String {
        return if (clause != null) "and $clause \n" else EMPTY_STRING
    }
}

object TableAlias {
    const val ORDER = "orders"
    const val GROUP_ORDER = "group_order"
    const val ORDERER = "orderer"
    const val ORDER_DEAL_PRODUCT = "order_deal_product"
    const val ORDERER_MEMBER_META = "orderer_member_meta"
    const val INVOICE = "invoice"
    const val MISSING_PRODUCTION_LOG = "missing_production_log"
    const val MISSING_PRODUCTION_UPLOAD = "missing_production_upload"
}
