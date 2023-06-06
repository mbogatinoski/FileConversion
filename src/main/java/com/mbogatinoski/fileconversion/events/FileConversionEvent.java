package com.mbogatinoski.fileconversion.events;

import java.time.Instant;
import java.time.LocalDateTime;

public class FileConversionEvent {
    private long fileSize;
    private String sourceFormat;
    private String targetFormat;

    private LocalDateTime eventTimestamp;
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
        this.fileSize = fileSize;
    }

    public FileConversionEvent() {
    }

    public void setTargetFormat(String targetFormat) {
        this.targetFormat = targetFormat;
    }


    public FileConversionEvent(String sourceFormat, String targetFormat, long fileSize, LocalDateTime eventTimestamp) {
        this.sourceFormat = sourceFormat;
        this.targetFormat = targetFormat;
        this.fileSize = fileSize;
        this.eventTimestamp = LocalDateTime.now();
    }

    public void setEventTimestamp(LocalDateTime eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

}
