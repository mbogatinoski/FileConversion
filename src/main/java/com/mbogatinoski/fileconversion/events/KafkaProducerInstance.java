package com.mbogatinoski.fileconversion.events;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaProducerInstance {

    private final static String TOPIC_NAME = "file-conversion-events";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";

    private final Producer<String, FileConversionEvent> producer;

    public KafkaProducerInstance() {
        Properties props = new Properties();
        props.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "com.mbogatinoski.fileconversion.FileConversionEventSerializer");

        producer = new KafkaProducer<>(props);
    }

    public void sendEvent(FileConversionEvent event) {
        ProducerRecord<String, FileConversionEvent> record = new ProducerRecord<>(TOPIC_NAME, event.getFileName(), event);
        producer.send(record);
    }

    public void close() {
        producer.close();
    }
}