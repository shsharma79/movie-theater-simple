package com.jpmc.theater;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;
import uk.org.webcompere.systemstubs.stream.SystemOut;

import java.util.List;
import java.util.function.Consumer;

import static com.jpmc.theater.Theater.DASH_LINE_SEPARATOR;
import static com.jpmc.theater.Theater.PrintFormat.JSON;
import static com.jpmc.theater.Theater.PrintFormat.TEXT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@ExtendWith(SystemStubsExtension.class)
public class TheaterTests {
  private final Theater theater = new Theater(LocalDateProvider.singleton());
  private final Customer john = new Customer("John Doe", "id-12345");
  private final ObjectMapper mapper = ObjectMapperConfig.getInstance().getObjectMapper();
  @SystemStub private SystemOut systemOut;

  @Test
  void totalFeeForCustomer() {
    Reservation reservation = theater.reserve(john, 2, 4);
    assertThat(reservation.totalFee()).isEqualByComparingTo(37.5);
  }

  @Test
  public void whenInvalidSequence_ShouldThrowException() {
    assertThatIllegalArgumentException()
        .isThrownBy(
            () -> {
              theater.reserve(john, 11, 3);
            })
        .withMessage("Sequence 11 is not valid");
  }

  @Test
  public void whenFormatPlainText_ShouldPrintTextSchedule() {
    theater.printSchedule(TEXT);
    Consumer<List<String>> specificValidation =
        s -> {
          assertThat(s.subList(2, s.size() - 1).size()).isEqualTo(theater.getSchedule().size());
        };
    validatePrintScheduleOutput(systemOut.getLines().toList(), specificValidation);
  }

  @Test
  public void whenFormatJson_ShouldPrintJsonTextSchedule() {
    theater.printSchedule(JSON);
    Consumer<List<String>> specificValidation =
        s -> {
          // Removed header and footer and Build a list of Showing objects
          StringBuilder sb = new StringBuilder();
          s.subList(2, s.size() - 1).stream()
              .forEach(
                  line -> {
                    sb.append(line);
                  });
          try {
            List<Showing> showings =
                mapper.readValue(sb.toString(), new TypeReference<List<Showing>>() {});
            assertThat(showings.size()).isEqualTo(theater.getSchedule().size());
          } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
          }
        };
    validatePrintScheduleOutput(systemOut.getLines().toList(), specificValidation);
  }

  private void validatePrintScheduleOutput(
      List<String> outputLines, Consumer<List<String>> specificValidation) {
    testPrintScheduleStart(outputLines);
    specificValidation.accept(outputLines);
    testPrintScheduleEnd(outputLines);
  }

  private void testPrintScheduleStart(List<String> logLines) {
    assertThat(logLines.get(0)).isEqualTo(LocalDateProvider.singleton().currentDate().toString());
    assertThat(logLines.get(1)).isEqualTo(DASH_LINE_SEPARATOR);
  }

  private void testPrintScheduleEnd(List<String> logLines) {
    var lastLogLine = logLines.get(logLines.size() - 1);
    assertThat(lastLogLine).isEqualTo(DASH_LINE_SEPARATOR);
  }
}
