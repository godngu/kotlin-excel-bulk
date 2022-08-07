package com.godngu.excel.bulk.order

import com.godngu.excel.bulk.excel.ExcelHeader
import com.godngu.excel.bulk.excel.ExcelWriteModel
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class OrderMapper : RowMapper<ExcelWriteModel> {

    override fun mapRow(rs: ResultSet, rowNum: Int): ExcelWriteModel {
        return OrderDto(
            seq = rowNum + 1,
            orderNo = rs.getLong("order_no"),
            totalPaymentPrice = rs.getLong("total_payment_price"),
            groupOrderNo = rs.getLong("group_order_no"),
            orderType = rs.getString("order_type") ,
            partnerName = rs.getString("order_type"),
            partnerId = rs.getString("order_type"),
            fulfillmentOwnerId = rs.getString("order_type"),
            orderProductName = rs.getString("order_type"),
            orderStatus = rs.getString("order_type"),
            allRefunded = rs.getString("refund_status"),
            ordererMemberNo = rs.getLong("member_no"),
            ordererMemberId = rs.getString("member_id"),
            ordererName = rs.getString("orderer.name"),
            ordererPhoneNumber = rs.getString("orderer.phone_number"),
            orderDate = rs.getString("created_at"),
            paymentDate = rs.getString("payment_completed_at"),
            receiverName = rs.getString("receiver.name"),
            receiverPhoneNumber = rs.getString("receiver.phone_number"),
            zipcode = rs.getString("zipcode"),
            address = rs.getString("address"),
            addressDetail = rs.getString("address_detail"),
            memo = rs.getString("memo"),
            deliveryPolicy = rs.getString("delivery_policy"),
            courierType = rs.getString("courier"),
            pickupType = rs.getString("pickup_type")?: "",
            pickupDetail = rs.getString("pickup_detail"),
            clusterCenterCode = rs.getString("cluster_center_code"),
            regionGroup = rs.getString("region_group_code"),
            paymentGatewayId = rs.getString("payment_gateway_id"),
            applicationType = rs.getString("application_type"),
            totalDealProductPriceBalance = rs.getLong("total_deal_product_price_balance"),
            totalDealProductDiscountPriceBalance = rs.getLong("total_deal_product_discount_price_balance"),
            totalCouponDiscountPriceBalance = rs.getLong("total_coupon_discount_price_balance"),
            totalUsedPointBalance = rs.getLong("total_used_paid_point_balance") + rs.getLong("total_used_free_point_balance"),
            deliveryPriceBalance = rs.getLong("delivery_price_balance"),
            couponName = rs.getString("coupon.name"),
            memberGroup = rs.getString("member_group"),
            isFirstPurchased = rs.getString("is_first_purchased"),
            isSpecialCare = rs.getString("is_special_care"),
        )
    }
}
