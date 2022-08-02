package com.godngu.excel.bulk.order

import com.godngu.excel.bulk.excel.AbstractExcelJdbcProducer
import com.godngu.excel.bulk.excel.ExcelWriteModel
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import java.util.concurrent.atomic.AtomicInteger

class OrderExcelProducer(
    override val jdbcTemplate: JdbcTemplate,
    override val rowMapper: RowMapper<ExcelWriteModel>,
) : AbstractExcelJdbcProducer() {

    override fun totalCount(): Int {
        return jdbcTemplate.queryForObject(orderSqlTotalCount, Int::class.java) ?: 0
    }

    override fun call(): AtomicInteger {
        val count = AtomicInteger()

        val stream = jdbcTemplate.queryForStream(orderSql, rowMapper)
        stream.forEach {
            count.getAndIncrement()
            queue.put(it)
        }

        return count
    }
}


val orderSql = """
        select  -- count(orders.order_no)
                orders.order_no,
                sum(order_deal_product.payment_price) as total_payment_price
        from    (
                select  distinct orders.order_no as order_no
                from    orders
                where   (orders.order_status in ('COMPLETED' , 'PRODUCING' , 'DELIVERING' , 'DELIVERED' , 'CONFIRMED'))
                    and (orders.created_at between '2022-07-25T00:00' and '2022-07-25T23:59')
                ) as order_nos
                inner join orders
                    on order_nos.order_no = orders.order_no
                inner join receiver
                    on receiver.order_no = orders.order_no
                inner join delivery_price
                    on delivery_price.order_no = orders.order_no
                inner join order_deal_product as order_deal_product
                    on order_deal_product.order_no = orders.order_no
                inner join group_order
                    on group_order.group_order_no = orders.group_order_no
                inner join orderer
                    on orderer.group_order_no = group_order.group_order_no
                inner join user_agent
                    on user_agent.group_order_no = orders.group_order_no
                left outer join orderer_member_meta
                    on group_order.group_order_no = orderer_member_meta.group_order_no
                left outer join invoice
                    on invoice.order_no = orders.order_no
                left outer join missing_production_log
                    on missing_production_log.order_no = orders.order_no
                left outer join coupon
                    on coupon.order_no = orders.order_no
        group by order_deal_product.order_no
""".trimIndent()

val orderSqlTotalCount = """
    select  count(orders.order_no)
    from    (
            select  distinct orders.order_no as order_no
            from    orders
            where   (orders.order_status in ('COMPLETED' , 'PRODUCING' , 'DELIVERING' , 'DELIVERED' , 'CONFIRMED'))
                and (orders.created_at between '2022-07-25T00:00' and '2022-07-25T23:59')
            ) as order_nos
            inner join orders
                on order_nos.order_no = orders.order_no
            inner join receiver
                on receiver.order_no = orders.order_no
            inner join delivery_price
                on delivery_price.order_no = orders.order_no
            inner join group_order
                on group_order.group_order_no = orders.group_order_no
            inner join orderer
                on orderer.group_order_no = group_order.group_order_no
            inner join user_agent
                on user_agent.group_order_no = orders.group_order_no
            left outer join orderer_member_meta
                on group_order.group_order_no = orderer_member_meta.group_order_no
            left outer join invoice
                on invoice.order_no = orders.order_no
            left outer join missing_production_log
                on missing_production_log.order_no = orders.order_no
            left outer join coupon
                on coupon.order_no = orders.order_no
""".trimIndent()
