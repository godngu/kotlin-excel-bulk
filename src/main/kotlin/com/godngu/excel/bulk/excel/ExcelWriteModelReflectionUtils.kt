package com.godngu.excel.bulk.excel

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

object ExcelWriteModelReflectionUtils {

    /**
     * 모델 객체에서 정렬된 프로퍼티 목록 조회
     */
    fun <T : ExcelWriteModel> getSortedProperties(type: KClass<out T>): List<KProperty1<out T, *>> {
        val primaryConstructor = type.primaryConstructor
        requireNotNull(primaryConstructor)

        return primaryConstructor.parameters
            .map { it.name }
            .map { name ->
                val property = type.memberProperties.find { it.name == name }
                requireNotNull(property)
                property
            }
            .filter { it.findAnnotation<ExcelHeader>() != null }
    }

    /**
     * 모델 클래스에 선언된 프로퍼티를 순서대로 꺼내기 위해 생성자 파라미터를 이용합니다.
     */
    fun <T : ExcelWriteModel> getExcelHeaderNames(properties: List<KProperty1<out T, *>>): List<String> {
        return properties
            .map {
                val headerName = it.findAnnotation<ExcelHeader>()?.name
                requireNotNull(headerName)
                headerName
            }
    }
}
