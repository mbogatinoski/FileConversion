package com.mbogatinoski.fileconversion.events;

import java.time.Instant;
import java.time.LocalDateTime;

public class FileConversionEvent {
    private String fileName;
    private long fileSize;
    private String sourceFormat;
    private String targetFormat;

    private LocalDateTime eventTimestamp;
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSourceFormat() {
        return sourceFormat;
    }

    public void setSourceFormat(String sourceFormat) {
        this.sourceFormat = sourceFormat;
    }

    public String getTargetFormat() {
        return targetFormat;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        fileSize = fileSize;
    }

    public FileConversionEvent() {
    }

    public void setTargetFormat(String targetFormat) {
        this.targetFormat = targetFormat;
    }


    public FileConversionEvent(String fileName, String sourceFormat, String targetFormat, long fileSize) {
        this.fileName = fileName;
        this.sourceFormat = sourceFormat;
        this.targetFormat = targetFormat;
        this.fileSize = fileSize;
        this.eventTimestamp = LocalDateTime.now();
    }

    public void setEventTimestamp(LocalDateTime eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

}
