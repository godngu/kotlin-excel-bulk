package com.godngu.excel.bulk.excel

import com.godngu.excel.bulk.board.ExcelBodyConsumer
import org.apache.poi.poifs.crypt.EncryptionInfo
import org.apache.poi.poifs.crypt.EncryptionMode
import org.apache.poi.poifs.filesystem.POIFSFileSystem
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFColor
import java.io.OutputStream
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.system.measureTimeMillis

class ExcelWriter<out T : ExcelWriteModel>(
    private val producer: AbstractExcelJdbcProducer,
    type: KClass<out T>,
    private val password: String? = null
) {
    private val properties: List<KProperty1<out T, *>> = ExcelWriteModelReflectionUtils.getSortedProperties(type)
    private val workbook = SXSSFWorkbook(1000)
    private val sheet: Sheet = workbook.createSheet("sheet1")
    private val cellStyle = (workbook.createCellStyle() as XSSFCellStyle).applyBorderThin()

    fun write(outputStream: OutputStream) {
        render()
        writeWorkbook(outputStream)
        workbook.close()
        workbook.dispose() // 디스크에 임시 저장된 파일 삭제
        outputStream.close()
    }

    private fun render() {
        renderHeader()
        renderBody()
    }

    private fun writeWorkbook(outputStream: OutputStream) {
        if (applyPassword()) {
            writeWorkbookWithPassword(outputStream)
        } else {
            workbook.write(outputStream)
        }
    }

    private fun applyPassword(): Boolean = !password.isNullOrBlank()

    private fun writeWorkbookWithPassword(outputStream: OutputStream) {
        val fileSystem = POIFSFileSystem()
        val encryptionInfo = EncryptionInfo(EncryptionMode.agile)
        val encryptor = encryptionInfo.encryptor
        encryptor.confirmPassword(password)
        val os = encryptor.getDataStream(fileSystem)
        workbook.write(os)
        os.flush()
        os.close()
        fileSystem.writeFilesystem(outputStream)
        fileSystem.close()
    }

    private fun renderBody() {
        val latch = CountDownLatch(producer.totalCount())

        val elapsed = measureTimeMillis {
            val queue = LinkedBlockingQueue<ExcelWriteModel>(100)

            val dbExecutor = Executors.newSingleThreadExecutor()
            producer.queue = queue
            val producerFuture = dbExecutor.submit(producer)

            val numberOfThread = 2
            val excelExecutor = Executors.newFixedThreadPool(numberOfThread)

            val consumers = ArrayList<ExcelBodyConsumer<ExcelWriteModel>>()
            val consumerFutures = ArrayList<Future<AtomicInteger>>()

            repeat((1..numberOfThread).count()) {
                val excelConsumer = ExcelBodyConsumer(
                    queue = queue,
                    sheet = sheet,
                    cellStyle = cellStyle,
                    properties = properties,
                    latch = latch,
                )
                consumers.add(excelConsumer)
                consumerFutures.add(excelExecutor.submit(excelConsumer))
            }
            producerFuture.get()

            // consumer 에게 DB 조회 producer 종료 알림
            consumers.forEach(ExcelBodyConsumer<ExcelWriteModel>::producerComplete)
            consumerFutures.forEach(Future<AtomicInteger>::get)

            latch.await(10, TimeUnit.MINUTES)
        }
        println("다운로드 걸린 시간: $elapsed 밀리초")
    }

    private fun renderHeader() {
        val cellStyle = (workbook.createCellStyle() as XSSFCellStyle)
            .applyBackgroundColor(223, 235, 246)
            .applyBorderThin()

        val row: Row = sheet.createRow(0)
        ExcelWriteModelReflectionUtils.getExcelHeaderNames(properties)
            .forEachIndexed { cellIndex, name ->
                val cell = row.createCell(cellIndex)
                cell.setCellValue(name)
                cell.cellStyle = cellStyle
            }
    }

    private fun XSSFCellStyle.applyBackgroundColor(red: Int, green: Int, blue: Int): XSSFCellStyle {
        this.setFillForegroundColor(XSSFColor(getColorBytes(red, green, blue), DefaultIndexedColorMap()))
        this.fillPattern = FillPatternType.SOLID_FOREGROUND
        return this
    }

    private fun XSSFCellStyle.applyBorderThin(): XSSFCellStyle {
        this.borderTop = BorderStyle.THIN
        this.borderBottom = BorderStyle.THIN
        this.borderLeft = BorderStyle.THIN
        this.borderRight = BorderStyle.THIN
        return this
    }

    private fun getColorBytes(red: Int, green: Int, blue: Int): ByteArray {
        return byteArrayOfInts(red, green, blue)
    }

    private fun byteArrayOfInts(vararg ints: Int) = ByteArray(ints.size) { pos -> ints[pos].toByte() }
}
