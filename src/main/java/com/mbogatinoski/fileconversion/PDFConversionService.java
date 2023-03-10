package com.mbogatinoski.fileconversion;

import com.aspose.pdf.Document;
import com.aspose.pdf.SaveFormat;
import com.mbogatinoski.fileconversion.events.FileConversionEvent;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

@Service
public class PDFConversionService implements FileConversionService{
    private final KafkaTemplate<String, FileConversionEvent> kafkaTemplate;
    private static final String TOPIC = "file-conversion-events";

    @Autowired
    public PDFConversionService(KafkaTemplate<String, FileConversionEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public PDFConversionService() {
        this.kafkaTemplate = null;
    }


    public byte[] convertToTXT(MultipartFile file) throws IOException {
        InputStream input = file.getInputStream();
        PDDocument pdf = PDDocument.load(input);
        PDFTextStripper textStripper = new PDFTextStripper();
        String text = textStripper.getText(pdf);
        byte[] fileBytes = text.getBytes();
        pdf.close();

        // Create the event
        FileConversionEvent event = new FileConversionEvent();
        event.setEventTimestamp(LocalDateTime.now());
        event.setFileName(file.getOriginalFilename());
        event.setFileSize(file.getSize());
        event.setSourceFormat("pdf");
        event.setTargetFormat("txt");

        kafkaTemplate.send(TOPIC, event);

        return fileBytes;
    }

    public byte[] convertToDOCX(MultipartFile file) throws Exception {
        byte[] convertedBytes = null;
        try {
            InputStream input = file.getInputStream();
            Document pdfDocument = new Document(input);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            pdfDocument.save(outputStream, SaveFormat.DocX);
            convertedBytes = outputStream.toByteArray();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return convertedBytes;
    }

    @Override
    public byte[] convertToPDF(MultipartFile file) throws Exception {
        return null;
    }

    public byte[] convertToHTML(MultipartFile file) throws Exception {
        InputStream input = file.getInputStream();
        PDDocument pdf = PDDocument.load(input);
        PDFTextStripper textStripper = new PDFTextStripper();
        String text = textStripper.getText(pdf);
        org.jsoup.nodes.Document doc = Jsoup.parse(text);
        byte[] fileBytes = doc.toString().getBytes();
        pdf.close();
        return fileBytes;
    }
}
