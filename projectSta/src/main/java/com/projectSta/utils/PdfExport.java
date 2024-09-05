package com.projectSta.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.projectSta.domain.Msiswa;
import com.projectSta.domain.Mkelas;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PdfExport {

    public void exportToPDF(List<Msiswa> siswaList, String filePath) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));

        document.open();

        PdfPTable table = new PdfPTable(5);

        addTableHeader(table);
        addRows(table, siswaList);

        document.add(table);
        document.close();
    }

    private void addTableHeader(PdfPTable table) {
        PdfPCell header = new PdfPCell();
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell("NISN");
        table.addCell("Nama Siswa");
        table.addCell("Kelamin");
        table.addCell("Kelas");
        table.addCell("Created Time");
    }

    private void addRows(PdfPTable table, List<Msiswa> siswaList) {
        for (Msiswa siswa : siswaList) {
            table.addCell(siswa.getNisn());
            table.addCell(siswa.getNamasiswa());
            table.addCell(siswa.getJeniskelamin());

            Mkelas kelas = siswa.getMkelasfk();
            table.addCell(kelas != null ? kelas.getNamakelas() : "namakelas"); 

            table.addCell(siswa.getCreatedtime().toString());
        }
    }
}
