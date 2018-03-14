package com.example.producer1;

import com.example.Sensor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.schema.avro.AvroSchemaMessageConverter;
import org.springframework.cloud.stream.schema.client.EnableSchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.util.MimeType;

import java.util.*;

@SpringBootApplication
@EnableSchemaRegistryClient
@EnableBinding(Source.class)
public class Producer1Application {

	@Bean
	public MessageConverter userMessageConverter() {
		return new AvroSchemaMessageConverter(MimeType.valueOf("application/*+avro"));
	}

	private Random random = new Random();

	public static void main(String[] args) {
		SpringApplication.run(Producer1Application.class, args);
	}

	@Bean
	@InboundChannelAdapter(channel = "output", poller = @Poller(fixedDelay = "100000"))
	public MessageSource<Sensor> sendTestData() {
		return () -> new GenericMessage<Sensor>(randomSensor());
	}

	private Sensor randomSensor() {
		Sensor sensor = new Sensor();
		sensor.setId(UUID.randomUUID().toString());
		sensor.setAcceleration(random.nextFloat() * 10);
		sensor.setVelocity(random.nextFloat() * 100);
		sensor.setTemperature(random.nextFloat() * 50);
		return sensor;
	}
}
