package io.aespinoza;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

public class DocumentTextExtractor {

    /**
     * Extrae el texto de un archivo doc, docx o pdf ubicado en la ruta especificada.
     *
     * @param filePath Ruta del archivo a procesar.
     * @return Cadena JSON con la forma {"errCod": código, "errDes": descripción, "text": "texto extraído"}.
     */
    public static String extractText(String filePath) {
        String text = "";
        int errCod = 0;
        String errDes = "Ok";
        File file = new File(filePath);

        if (!file.exists()) {
            return "{\"errCod\": 1, \"errDes\": \"Archivo no encontrado\", \"text\": \"\"}";
        }

        try {
            if (filePath.toLowerCase().endsWith(".pdf")) {
                PDDocument document = PDDocument.load(file);
                PDFTextStripper pdfStripper = new PDFTextStripper();
                text = pdfStripper.getText(document);
                document.close();
            } else if (filePath.toLowerCase().endsWith(".docx")) {
                FileInputStream fis = new FileInputStream(file);
                XWPFDocument docx = new XWPFDocument(fis);
                XWPFWordExtractor extractor = new XWPFWordExtractor(docx);
                text = extractor.getText();
                extractor.close();
                docx.close();
                fis.close();
            } else {
                return "{\"errCod\": 2, \"errDes\": \"Formato de archivo no soportado\", \"text\": \"\"}";
            }
        } catch (IOException e) {
            errCod = 3;
            errDes = "Error al leer el archivo: " + e.getMessage();
        } catch (Exception e) {
            errCod = 4;
            errDes = "Error inesperado: " + e.getMessage();
        }

        // Escapar caracteres especiales para formar un JSON válido.
        text = text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
        errDes = errDes.replace("\\", "\\\\").replace("\"", "\\\"");

        return "{\"errCod\": " + errCod + ", \"errDes\": \"" + errDes + "\", \"text\": \"" + text + "\"}";
    }

    public static void main(String[] args) {

        String filePath = "C:\\xwai\\aacd103_s5_entregable.docx";
        System.out.println(extractText(filePath));
    }
}