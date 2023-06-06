package com.mbogatinoski.fileconversion.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class FileConversionEventDeserializer implements Deserializer<FileConversionEvent> {

    @Override
    public FileConversionEvent deserialize(String topic, byte[] data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(data, FileConversionEvent.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
