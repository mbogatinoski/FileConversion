package com.mbogatinoski.fileconversion.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

public class FileConversionEventSerializer implements Serializer<FileConversionEvent> {

    @Override
    public byte[] serialize(String topic, FileConversionEvent data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}

