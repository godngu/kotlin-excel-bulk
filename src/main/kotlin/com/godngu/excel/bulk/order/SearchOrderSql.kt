package com.godngu.excel.bulk.order

object SearchOrderSql {

    fun createSql(whereClause: SqlWhereClause): String {
        return """
            select  orders.order_no,
                    sum(order_deal_product.payment_price) as total_payment_price
            from    (
                    select  distinct orders.order_no as order_no
                    from    orders
                            inner join group_order
                                on group_order.group_order_no = orders.group_order_no
                            inner join orderer
                                on orderer.group_order_no = group_order.group_order_no
                            left outer join orderer_member_meta
                                on group_order.group_order_no = orderer_member_meta.group_order_no
                            left outer join invoice
                                on invoice.order_no = orders.order_no
                            left outer join missing_production_log
                                on missing_production_log.order_no = orders.order_no
                    where ${ if (whereClause.isNotEmpty) "1=1 ${whereClause.sql}" else "" }
                    ) as order_nos
                    inner join orders
                        on order_nos.order_no = orders.order_no
                    inner join receiver
                        on receiver.order_no = orders.order_no
                    inner join delivery_price
                        on delivery_price.order_no = orders.order_no
                    /* 뻥튀기 될텐데... */
                    inner join order_deal_product as order_deal_product
                        on order_deal_product.order_no = orders.order_no
                    left outer join coupon
                        on coupon.order_no = orders.order_no
                    inner join group_order
                        on group_order.group_order_no = orders.group_order_no
                    inner join orderer
                        on orderer.group_order_no = group_order.group_order_no
                    left outer join orderer_member_meta
                        on group_order.group_order_no = orderer_member_meta.group_order_no
                    inner join user_agent
                        on user_agent.group_order_no = orders.group_order_no
                    left outer join missing_production_log
                        on missing_production_log.order_no = orders.order_no
            group by order_deal_product.order_no
            order by orders.created_at desc
        """.trimIndent()
    }

    fun createTotalCountSql(whereClause: SqlWhereClause): String {
        return """
            select  count(distinct orders.order_no) as total_count
            from    orders
                    inner join group_order
                        on group_order.group_order_no = orders.group_order_no
                    inner join orderer
                        on orderer.group_order_no = group_order.group_order_no
                    left outer join orderer_member_meta
                        on group_order.group_order_no = orderer_member_meta.group_order_no
                    left outer join invoice
                        on invoice.order_no = orders.order_no
                    left outer join missing_production_log
                        on missing_production_log.order_no = orders.order_no
            where ${ if (whereClause.isNotEmpty) "1=1 ${whereClause.sql}" else "" }
        """.trimIndent()
    }
}
