package com.godngu.excel.bulk.excel

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
annotation class ExcelHeader(

    /**
     * 엑셀에 표시될 헤더 이름
     */
    val name: String
)
