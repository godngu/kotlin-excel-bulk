package com.godngu.excel.bulk.excel

import mu.KotlinLogging
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
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.system.measureTimeMillis

class ExcelStreamWriter<out T : ExcelWriteModel>(
    private val producer: AbstractExcelJdbcProducer,
    type: KClass<out T>,
    private val password: String? = null
) {
    private val properties: List<KProperty1<out T, *>> = ExcelWriteModelReflectionUtils.getSortedProperties(type)
    private val workbook = SXSSFWorkbook(1000)
    private val sheet: Sheet = workbook.createSheet("sheet1")

    private val logger = KotlinLogging.logger {}

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
        val totalCount = producer.totalCount()
        logger.info("[엑셀 데이터 totalCount: {$totalCount}]")

        val elapsed = measureTimeMillis {
            val queue = LinkedBlockingQueue<ExcelWriteModel>(100)

            val producerExecutor = Executors.newSingleThreadExecutor()
            producer.queue = queue
            val producerFuture = producerExecutor.submit(producer)

            val consumerExecutor = Executors.newSingleThreadExecutor()
            val excelConsumer = ExcelBodyConsumer(
                queue = queue,
                sheet = sheet,
                cellStyle = (workbook.createCellStyle() as XSSFCellStyle).applyBorderThin(),
                properties = properties,
            )
            val consumerFuture = consumerExecutor.submit(excelConsumer)

            // consumer 에게 DB 조회 producer 종료 알림
            val producerCount = producerFuture.get()
            logger.info("[엑셀 Producer Count: {$producerCount}]")
            excelConsumer.producerComplete()

            val consumerCount = consumerFuture.get()
            logger.info("[엑셀 Consumer Count: {$consumerCount}]")
        }
        logger.info("[엑셀 생성 걸린 시간: {$elapsed} 밀리초]")
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
