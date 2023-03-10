package com.mbogatinoski.fileconversion;

import org.springframework.web.multipart.MultipartFile;

public interface FileConversionService {
    byte[] convertToTXT(MultipartFile file) throws Exception;
    byte[] convertToDOCX(MultipartFile file) throws Exception;
    byte[] convertToPDF(MultipartFile file) throws Exception;
    byte[] convertToHTML(MultipartFile file) throws Exception;
}
