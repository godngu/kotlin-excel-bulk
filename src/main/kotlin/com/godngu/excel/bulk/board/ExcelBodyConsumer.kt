package com.godngu.excel.bulk.board

import com.godngu.excel.bulk.excel.ExcelWriteModel
import com.godngu.excel.bulk.excel.ExcelWriteModelReflectionUtils
import mu.KotlinLogging
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import java.util.concurrent.Callable
import java.util.concurrent.CountDownLatch
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.typeOf

class ExcelBodyConsumer<out T : ExcelWriteModel>(
    private val queue: LinkedBlockingQueue<ExcelWriteModel>,
    private var producerCompleted: Boolean = false,
    private val sheet: Sheet,
    private val cellStyle: XSSFCellStyle,
    private val properties: List<KProperty1<out T, *>>,
    val latch: CountDownLatch
) : Callable<AtomicInteger> {

    private val logger = KotlinLogging.logger {}

    override fun call(): AtomicInteger {
        val count = AtomicInteger()

        while (true) {
            if (producerCompleted && queue.isEmpty()) {
                logger.info(">>> {${Thread.currentThread().name}} 종료!!")
                break
            }

            val model: ExcelWriteModel = queue.poll(1, TimeUnit.SECONDS) ?: continue

            count.getAndIncrement()
            latch.countDown()

            renderExcelRow(model)
        }

        return count
    }

    private fun renderExcelRow(model: ExcelWriteModel) {
        println(">> SEQ = ${model.seq}")
        val row = sheet.createRow(model.seq)

        properties.forEachIndexed { cellIndex, property ->
            val value = getValue(
                (property as KProperty1<Any, *>).get(model),
                property.returnType
            )
            renderExcelCell(cellIndex, row, value)
        }
    }

    private fun renderExcelCell(cellIndex: Int, row: Row?, value: Any?) {
        val cell = row?.createCell(cellIndex)
        cell?.setCellValue(getValue(value, String::class.createType()))
        cell?.cellStyle = cellStyle
    }

    private fun getValue(value: Any?, returnType: KType): String {
        if (value == null) {
            return ""
        }

        return when (returnType) {
            typeOf<Boolean>() -> {
                if (value as Boolean) "Y" else "N"
            }
            else -> value.toString()
        }
    }

    fun producerComplete() {
        this.producerCompleted = true
    }
}
