package com.mbogatinoski.fileconversion;

import com.aspose.pdf.HtmlSaveOptions;
import com.aspose.pdf.SaveFormat;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.apache.tika.exception.TikaException;
import org.fit.pdfdom.PDFToHTML;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import com.aspose.pdf.Document;

import java.awt.image.BufferedImage;
import java.io.*;

@Service
public class FileConversionService {
    public void convertPDFToTXT(MultipartFile file) throws IOException, SAXException, TikaException {
        InputStream input = file.getInputStream();
        PDDocument pdf = PDDocument.load(input);
        PDFTextStripper textStripper = new PDFTextStripper();
        String text = textStripper.getText(pdf);
        byte[] fileBytes = text.getBytes();
        pdf.close();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();
        response.setContentType("text/plain");
        response.setHeader("Content-disposition", "attachment; filename=converted.txt");
        response.getOutputStream().write(fileBytes);
        response.getOutputStream().flush();
    }
    public void convertPDFToDOCX(MultipartFile file) throws Exception {
        try {
            InputStream input = file.getInputStream();
            Document pdfDocument = new Document(input);
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = requestAttributes.getResponse();
            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            response.setHeader("Content-disposition", "attachment; filename=converted.docx");
            pdfDocument.save(response.getOutputStream(), SaveFormat.DocX);
            response.getOutputStream().flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void convertPDFToHTML(MultipartFile file) throws Exception {
        try {
            InputStream input = file.getInputStream();
            PDDocument pdf = PDDocument.load(input);
            PDFTextStripper textStripper = new PDFTextStripper();
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = requestAttributes.getResponse();
            response.setContentType("text/html");
            response.setHeader("Content-disposition", "attachment; filename=converted.html");
            String text = textStripper.getText(pdf);
            org.jsoup.nodes.Document doc = Jsoup.parse(text);
            OutputStream os = response.getOutputStream();
            os.write(doc.toString().getBytes());
            os.flush();
            pdf.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
