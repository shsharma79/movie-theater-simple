package com.jpmc.theater;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ObjectMapperConfig {
  public static final String DATETIME_FORMAT = "dd-MM-yyyy HH:mm";
  public static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern(DATETIME_FORMAT);
  public static LocalDateTimeSerializer LOCAL_DATETIME_SERIALIZER =
      new LocalDateTimeSerializer(DATE_TIME_FORMATTER);

  public static LocalDateTimeDeserializer LOCAL_DATETIME_DESERIALIZER =
      new LocalDateTimeDeserializer(DATE_TIME_FORMATTER);

  private static ObjectMapperConfig INSTANCE = null;

  private ObjectMapper mapper = null;

  private ObjectMapperConfig() {}

  public static synchronized ObjectMapperConfig getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ObjectMapperConfig();
      INSTANCE.mapper = initializeObjectMapper();
    }
    return INSTANCE;
  }

  private static ObjectMapper initializeObjectMapper() {
    /*
     * Jackson library doesn't support java.time.* classes by default.
     * We need to register JavaTimeModule explicitly, so that we can
     * serialize and de-serialize java time classes as per the above datetime format
     */
    JavaTimeModule module = new JavaTimeModule();
    module.addSerializer(LOCAL_DATETIME_SERIALIZER);
    module.addDeserializer(LocalDateTime.class, LOCAL_DATETIME_DESERIALIZER);
    return new ObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .registerModule(module)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);
  }

  public ObjectMapper getObjectMapper() {
    return INSTANCE.mapper;
  }
}
