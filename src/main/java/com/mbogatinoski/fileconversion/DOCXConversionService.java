package com.mbogatinoski.fileconversion;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Service
public class DOCXConversionService implements FileConversionService {
    @Override
    public byte[] convertToTXT(MultipartFile file) throws Exception {
        InputStream input = file.getInputStream();
        XWPFDocument docx = new XWPFDocument(input);
        XWPFWordExtractor textExtractor = new XWPFWordExtractor(docx);
        String text = textExtractor.getText();
        byte[] fileBytes = text.getBytes();
        docx.close();
        return fileBytes;
    }

    @Override
    public byte[] convertToDOCX(MultipartFile file) throws Exception {
        return null;
    }

    @Override
    public byte[] convertToPDF(MultipartFile file) throws Exception {
        InputStream input = file.getInputStream();
        com.aspose.words.Document doc = new com.aspose.words.Document(input);

        // Save the document to a PDF file
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.save(out, com.aspose.words.SaveFormat.PDF);

        // Send the PDF file to the browser
        byte[] fileBytes = out.toByteArray();
        return fileBytes;
    }

    @Override
    public byte[] convertToHTML(MultipartFile file) throws Exception {
        InputStream input = file.getInputStream();
        com.aspose.words.Document doc = new com.aspose.words.Document(input);

        // Configure the HTML conversion options
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        com.aspose.words.HtmlSaveOptions saveOptions = new com.aspose.words.HtmlSaveOptions();
        saveOptions.setExportImagesAsBase64(true);
        saveOptions.setCssStyleSheetType(com.aspose.words.CssStyleSheetType.EMBEDDED);
        saveOptions.setPrettyFormat(true);
        saveOptions.setExportTextInputFormFieldAsText(true);

        // Save the document to an HTML file
        doc.save(out, saveOptions);

        // Send the HTML file to the browser
        byte[] fileBytes = out.toByteArray();
        return fileBytes;
    }
}
