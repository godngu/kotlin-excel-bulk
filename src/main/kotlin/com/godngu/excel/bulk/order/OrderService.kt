package com.godngu.excel.bulk.order

import com.godngu.excel.bulk.excel.ExcelStreamWriter
import com.godngu.excel.bulk.order.temp.BusinessException
import com.godngu.excel.bulk.order.temp.OrderStatusType
import com.godngu.excel.bulk.order.temp.SearchCategoryKeywordInput
import com.godngu.excel.bulk.order.temp.SearchOrderCategoryType
import com.godngu.excel.bulk.order.temp.SearchOrderQuery
import com.godngu.excel.bulk.order.temp.SearchPeriod
import com.godngu.excel.bulk.order.temp.ErrorCode
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class OrderService(
    private val jdbcTemplate: JdbcTemplate,
) {
    fun excelDownload() {
        val file = createFile("src/main/resources/test")
        val condition = createSearchOrderQuery()
        val whereClause = SearchOrderWhereClause.create(condition)
        val producer = OrderSearchExcelProducer(jdbcTemplate, OrderMapper(), whereClause)
        ExcelStreamWriter(producer = producer, type = OrderDto::class).write(FileOutputStream(file))
    }

    private fun createSearchOrderQuery(): SearchOrderQuery {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val searchOrderCategoryType = SearchOrderCategoryType.ALL

        return SearchOrderQuery(
            searchInput = SearchCategoryKeywordInput(
                category = SearchOrderCategoryType.ALL,
                input = null
            ),
            period = SearchPeriod(
                LocalDateTime.parse("2022-07-25T00:00:00", formatter),
                LocalDateTime.parse("2022-07-25T23:59:59", formatter),
                defaultDateRange = 1,
                defaultPeriodUnit = SearchPeriod.PeriodType.DAY,
                defaultTime = SearchPeriod.TimeType.ELEVEN,
                maxRange = searchOrderCategoryType.searchRange,
                maxPeriodUnit = searchOrderCategoryType.searchPeriod
            ),
            orderStatus = getOrderStatus(null),
            partner = null,
            orderType = null,
            paymentGatewayId = null,
            clusterCenterCodes = null,
            hasFailedDelivery = null,
            fulfillmentOwnerID = null,
            deliveryPolicy = null,
            courier = null,
            isFirstPurchased = null,
            isSpecialCare = null,
            isDeliveryOrder = null,
            isMissingProduction = null,
            isDeferredDelivery = null,
            categoryIds = null,
        )
    }

    private fun createFile(filename: String): File {
        return File("${addSuffix(filename)}.xlsx")
    }

    private fun addSuffix(filename: String): String {
        return filename + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))
    }

    private fun getOrderStatus(orderStatus: OrderStatusType?): List<OrderStatusType> {
        return when {
            orderStatus == null || orderStatus == OrderStatusType.ALL -> listOf(
                OrderStatusType.COMPLETED,
                OrderStatusType.PRODUCING,
                OrderStatusType.DELIVERING,
                OrderStatusType.DELIVERED,
                OrderStatusType.CONFIRMED
            )
            else -> listOf(OrderStatusType.values().find { it.name == orderStatus?.name } ?: throw BusinessException(ErrorCode.INVALID_INPUT_VALUE))
        }
    }
}
