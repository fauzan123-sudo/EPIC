package com.example.epic.ui.fragment.report.pdf

import android.os.Environment
import com.example.epic.data.model.report.ItemPdf
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream

class PdfService {
    val TITLE_FONT = Font(Font.FontFamily.HELVETICA, 16f, Font.BOLD)
    val BODY_FONT = Font(Font.FontFamily.TIMES_ROMAN, 12f, Font.NORMAL)
    private lateinit var pdf: PdfWriter

    private fun createFile(fileName: String): File {
        //Prepare file
        val title = "Data Barang.pdf"
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(path, fileName)
        if (!file.exists()) file.createNewFile()
        return file
    }

    private fun createDocument(): Document {
        //Create Document
        val document = Document()
        document.setMargins(24f, 24f, 32f, 32f)
        document.pageSize = PageSize.A4
        return document
    }

    private fun setupPdfWriter(document: Document, file: File) {
        pdf = PdfWriter.getInstance(document, FileOutputStream(file))
        pdf.setFullCompression()
        //Open the document
        document.open()
    }

    private fun createTable(column: Int, columnWidth: FloatArray): PdfPTable {
        val table = PdfPTable(column)
        table.widthPercentage = 100f
        table.setWidths(columnWidth)
        table.headerRows = 1
        table.defaultCell.verticalAlignment = Element.ALIGN_CENTER
        table.defaultCell.horizontalAlignment = Element.ALIGN_CENTER
        return table
    }

    private fun createCell(content: String): PdfPCell {
        val cell = PdfPCell(Phrase(content))
        cell.horizontalAlignment = Element.ALIGN_CENTER
        cell.verticalAlignment = Element.ALIGN_MIDDLE
        //setup padding
        cell.setPadding(8f)
        cell.isUseAscender = true
        cell.paddingLeft = 4f
        cell.paddingRight = 4f
        cell.paddingTop = 8f
        cell.paddingBottom = 8f
        return cell
    }

    private fun addLineSpace(document: Document, number: Int) {
        for (i in 0 until number) {
            document.add(Paragraph(" "))
        }
    }

    private fun createParagraph(content: String): Paragraph {
        val paragraph = Paragraph(content, BODY_FONT)
        paragraph.firstLineIndent = 25f
        paragraph.alignment = Element.ALIGN_JUSTIFIED
        return paragraph
    }

    fun createUserTable(
        data: List<ItemPdf>,
        fileName: String,
        onFinish: (file: File) -> Unit,
        onError: (Exception) -> Unit
    ) {
        //Define the document
        val file = createFile(fileName)
        val document = createDocument()

        //Setup PDF Writer
        setupPdfWriter(document, file)

        //Add Title
        val title = Paragraph("Laporan Akhir", TITLE_FONT)
//        document.add(Paragraph("Paragraph Title", TITLE_FONT))
        title.alignment = Element.ALIGN_CENTER
        document.add(title)
        //Add Empty Line as necessary
        addLineSpace(document, 1)

        addLineSpace(document, 1)

        //Add table title
//        document.add(Paragraph("Laporan Akhir ", TITLE_FONT))
        addLineSpace(document, 1)

        //Define Table
        val userIdWidth = 0.5f
        val firstNameWidth = 1f
        val middleNameWidth = 1f
        val lastNameWidth = 1f
        val additionalColumnWidth = 1f
        val columnWidth = floatArrayOf(
            userIdWidth,
            firstNameWidth,
            middleNameWidth,
            lastNameWidth,
            additionalColumnWidth,
            additionalColumnWidth,
            additionalColumnWidth
        )
        val table = createTable(7, columnWidth)
        //Table header (first row)
        val tableHeaderContent = listOf(
            "No",
            "Kode",
            "Nama Barang",
            "Sales",
            "Penjualan",
            "Pengembalian Barang",
            "Persediaan"
        )

        //write table header into table
        tableHeaderContent.forEach {
            //define a cell
            val cell = createCell(it)
            //add our cell into our table
            table.addCell(cell)
        }
        //write user data into table
        data.forEach {
            //Write Each User Id
            val idCell = createCell(it.no)
            table.addCell(idCell)
            //Write Each First Name
            val firstNameCell = createCell(it.code)
            table.addCell(firstNameCell)
            //Write Each Middle Name
            val middleNameCell = createCell(it.productName)
            table.addCell(middleNameCell)
            //Write Each Last Name
            val lastNameCell = createCell(it.sales)
            table.addCell(lastNameCell)

            val column5Cell = createCell(it.seller)
            table.addCell(column5Cell)

            val column6Cell = createCell(it.productReturn)
            table.addCell(column6Cell)

            val column7Cell = createCell(it.stock)
            table.addCell(column7Cell)
        }
        document.add(table)
        document.close()

        try {
            pdf.close()
        } catch (ex: Exception) {
            onError(ex)
        } finally {
            onFinish(file)
        }
    }
}