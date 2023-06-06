package com.mbogatinoski.fileconversion;

import com.mbogatinoski.fileconversion.converters.DOCXConversionService;
import com.mbogatinoski.fileconversion.converters.PDFConversionService;
import com.mbogatinoski.fileconversion.emails.EmailSender;
import com.mbogatinoski.fileconversion.events.FileConversionEvent;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
public class FileConversionController {

    @Autowired
    private KafkaTemplate<String, FileConversionEvent> kafkaTemplate;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @PostMapping("/convert")
    @CrossOrigin(origins = "http://localhost:3000")
    @ResponseBody
    public void convert(@RequestParam("file") MultipartFile file, @RequestParam("targetFormat") String targetFormat) throws IOException {
        String fileType = determineFileType(file);
        try {
            switch (fileType) {
                case "pdf" -> {
                    PDFConversionService pdfService = new PDFConversionService();
                    switch (targetFormat) {
                        case "txt" -> {
                            byte[] convertedBytes = pdfService.convertToTXT(file);
                            ServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
                            response.setContentType("text/plain");
                            ((HttpServletResponse) response).setHeader("Content-disposition", "attachment; filename=converted.txt");
                            response.getOutputStream().write(convertedBytes);
                            response.getOutputStream().flush();
                        }
                        case "docx" -> {
                            byte[] convertedBytes = pdfService.convertToDOCX(file);
                            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                            HttpServletResponse response = requestAttributes.getResponse();
                            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                            response.setHeader("Content-disposition", "attachment; filename=converted.docx");
                            response.getOutputStream().write(convertedBytes);
                            response.getOutputStream().flush();
                        }
                        case "html" -> {
                            byte[] convertedBytes = pdfService.convertToHTML(file);
                            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                            HttpServletResponse response = requestAttributes.getResponse();
                            response.setContentType("text/html");
                            response.setHeader("Content-disposition", "attachment; filename=converted.html");
                            response.getOutputStream().write(convertedBytes);
                            response.getOutputStream().flush();
                        }
                        default -> throw new IllegalArgumentException("Invalid target format: " + targetFormat);
                    }
                }
                case "docx" -> {
                    DOCXConversionService docService = new DOCXConversionService();
                    switch (targetFormat) {
                        case "pdf" -> {
                            byte[] convertedBytes = docService.convertToPDF(file);
                            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                            HttpServletResponse response = requestAttributes.getResponse();
                            response.setContentType("application/pdf");
                            response.setHeader("Content-disposition", "attachment; filename=converted.pdf");
                            response.getOutputStream().write(convertedBytes);
                            response.getOutputStream().flush();
                        }
                        case "txt" -> {
                            byte[] convertedBytes = docService.convertToTXT(file);
                            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
                            response.setContentType("text/plain");
                            response.setHeader("Content-disposition", "attachment; filename=converted.txt");
                            response.getOutputStream().write(convertedBytes);
                            response.getOutputStream().flush();
                        }
                        case "html" -> {
                            byte[] convertedBytes = docService.convertToHTML(file);
                            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                            HttpServletResponse response = requestAttributes.getResponse();
                            response.setContentType("text/html");
                            response.setHeader("Content-disposition", "attachment; filename=converted.html");
                            response.getOutputStream().write(convertedBytes);
                            response.getOutputStream().flush();
                        }
                        default -> throw new IllegalArgumentException("Invalid target format: " + targetFormat);
                    }
                }
                default -> throw new IllegalArgumentException("Invalid file type: " + fileType);
            }
            // create and populate the conversion event
            FileConversionEvent conversionEvent = new FileConversionEvent(fileType, targetFormat, file.getSize(), LocalDateTime.now());

            // send the conversion event to Kafka
            kafkaTemplate.send("file-conversion", conversionEvent);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while converting the file: " + e.getMessage());
        }
    }

    @PostMapping("/convertAndEmail")
    public void convertAndEmail(@RequestParam("file") MultipartFile file, @RequestParam("targetFormat") String targetFormat, @RequestParam("recipientEmail") String recipientEmail) throws IOException {
        EmailSender emailSender = new EmailSender();
        String fileType = determineFileType(file);
        try {
            switch (fileType) {
                case "pdf" -> {
                    PDFConversionService pdfService = new PDFConversionService();
                    switch (targetFormat) {
                        case "txt" -> {
                            byte[] convertedBytes = pdfService.convertToTXT(file);
                            emailSender.sendEmail(recipientEmail, convertedBytes, "application/txt", "converted.txt");
                        }
                        case "docx" -> {
                            byte[] convertedBytes = pdfService.convertToTXT(file);
                            emailSender.sendEmail(recipientEmail, convertedBytes, "application/docx", "converted.docx");
                        }
                        case "html" -> {
                            byte[] convertedBytes = pdfService.convertToTXT(file);
                            emailSender.sendEmail(recipientEmail, convertedBytes, "application/html", "converted.html");
                        }
                        default -> throw new IllegalArgumentException("Invalid target format: " + targetFormat);
                    }
                }
                case "docx" -> {
                    DOCXConversionService docService = new DOCXConversionService();
                    switch (targetFormat) {
                        case "pdf" -> {
                            byte[] convertedBytes = docService.convertToTXT(file);
                            emailSender.sendEmail(recipientEmail, convertedBytes, "application/pdf", "converted.pdf");
                        }
                        case "txt" -> {
                            byte[] convertedBytes = docService.convertToTXT(file);
                            emailSender.sendEmail(recipientEmail, convertedBytes, "application/txt", "converted.txt");
                        }
                        case "html" -> {
                            byte[] convertedBytes = docService.convertToTXT(file);
                            emailSender.sendEmail(recipientEmail, convertedBytes, "application/html", "converted.html");
                        }
                        default -> throw new IllegalArgumentException("Invalid target format: " + targetFormat);
                    }
                }
                default -> throw new IllegalArgumentException("Invalid file type: " + fileType);
            }
           // create and populate the conversion event
            FileConversionEvent conversionEvent = new FileConversionEvent(fileType, targetFormat, file.getSize(), LocalDateTime.now());

            // send the conversion event to Kafka
            kafkaTemplate.send("file-conversion", conversionEvent);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while converting the file: " + e.getMessage());
        }
    }
    private String determineFileType(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String[] parts = fileName.split("\\.");
        String extension = parts[parts.length - 1];
        return switch (extension.toLowerCase()) {
            case "pdf" -> "pdf";
            case "doc", "docx" -> "docx";
            default -> throw new IllegalArgumentException("Unsupported file type: " + extension);
        };
    }
}
