package com.mbogatinoski.fileconversion;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

@RestController
public class FileConversionController {

    private final FileConversionService fileConversionService;

    public FileConversionController(FileConversionService fileConversionService) {
        this.fileConversionService = fileConversionService;
    }
    @GetMapping("/")
    public String index() {
        return "<form method='post' enctype='multipart/form-data' action='/convert'>" +
                "  <input type='file' name='file' id='file'>" +
                "  <br>" +
                "  <label for='targetFormat'>Target Format</label>" +
                "  <select name='targetFormat' id='targetFormat'>" +
                "    <option value='txt'>TXT</option>" +
                "    <option value='doc'>DOC</option>" +
                "    <option value='docx'>DOCX</option>" +
                "    <option value='html'>HTML</option>" +
                "  </select>" +
                "  <br><br>" +
                "  <input type='submit' value='Convert'>" +
                "</form>";
    }
    @PostMapping("/convert")
    public String convert(@RequestParam("file") MultipartFile file, @RequestParam("targetFormat") String targetFormat) {
        try {
            switch (targetFormat) {
                case "txt":
                    fileConversionService.convertPDFToTXT(file);
                    return "The file has been converted to txt and downloaded!";
                case "docx":
                    fileConversionService.convertPDFToDOCX(file);
                    return "The file has been converted to docx and downloaded!";
                case "html":
                    //fileConversionService.convertPDFToHTML(file);
                    return "The file has been converted to html and downloaded!";
                default:
                    throw new IllegalArgumentException("Invalid target format: " + targetFormat);
            }
        } catch (IOException | SAXException | TikaException e) {
            return "An error occurred while converting the file: " + e.getMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
