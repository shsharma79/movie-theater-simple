package com.jpmc.theater;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Theater {

  public static final String DASH_LINE_SEPARATOR =
      "===================================================";
  private final List<Showing> schedule;
  private final LocalDateProvider provider;
  public Theater(LocalDateProvider provider) {
    this.provider = provider;

    Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1);
    Movie turningRed = new Movie("Turning Red", Duration.ofMinutes(85), 11, 0);
    Movie theBatMan = new Movie("The Batman", Duration.ofMinutes(95), 9, 0);
    schedule =
        List.of(
            new Showing(
                turningRed, 1, LocalDateTime.of(provider.currentDate(), LocalTime.of(9, 0))),
            new Showing(
                spiderMan, 2, LocalDateTime.of(provider.currentDate(), LocalTime.of(11, 0))),
            new Showing(
                theBatMan, 3, LocalDateTime.of(provider.currentDate(), LocalTime.of(12, 50))),
            new Showing(
                turningRed, 4, LocalDateTime.of(provider.currentDate(), LocalTime.of(14, 30))),
            new Showing(
                spiderMan, 5, LocalDateTime.of(provider.currentDate(), LocalTime.of(16, 10))),
            new Showing(
                theBatMan, 6, LocalDateTime.of(provider.currentDate(), LocalTime.of(17, 50))),
            new Showing(
                turningRed, 7, LocalDateTime.of(provider.currentDate(), LocalTime.of(19, 30))),
            new Showing(
                spiderMan, 8, LocalDateTime.of(provider.currentDate(), LocalTime.of(21, 10))),
            new Showing(
                theBatMan, 9, LocalDateTime.of(provider.currentDate(), LocalTime.of(23, 0))));
  }

  public static void main(String[] args) {
    Theater theater = new Theater(LocalDateProvider.singleton());
    theater.printSchedule(PrintFormat.TEXT);
  }

  public List<Showing> getSchedule() {
    return List.copyOf(this.schedule);
  }

  public Reservation reserve(Customer customer, int sequence, int howManyTickets) {
    if (sequence > schedule.size()) {
      throw new IllegalArgumentException(String.format("Sequence %s is not valid", sequence));
    }
    Showing showing = schedule.get(sequence - 1);
    return new Reservation(customer, showing, howManyTickets);
  }

  public void printSchedule(PrintFormat format) {
    System.out.println(provider.currentDate());
    System.out.println(DASH_LINE_SEPARATOR);
    switch (format) {
      case TEXT:
        printPlaintTextSchedule();
        break;
      case JSON:
        printJsonTextSchedule();
        break;
      case JSON_AND_TEXT:
        printPlaintTextSchedule();
        printJsonTextSchedule();
        break;
      default:
        throw new IllegalArgumentException(
            String.format("PrintFormat %s is not supported", format));
    }

    System.out.println(DASH_LINE_SEPARATOR);
  }

  private void printPlaintTextSchedule() {
    schedule.forEach(
        s ->
            System.out.println(
                s.getSequenceOfTheDay()
                    + ": "
                    + s.getStartTime()
                    + " "
                    + s.getMovie().getTitle()
                    + " "
                    + humanReadableFormat(s.getMovie().getRunningTime())
                    + " $"
                    + s.getMovieFee()));
  }

  private void printJsonTextSchedule() {
    try {
      String jsonText =
          ObjectMapperConfig.getInstance()
              .getObjectMapper()
              .writerWithDefaultPrettyPrinter()
              .writeValueAsString(schedule);
      System.out.println(jsonText);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public String humanReadableFormat(Duration duration) {
    long hour = duration.toHours();
    long remainingMin = duration.toMinutes() - TimeUnit.HOURS.toMinutes(duration.toHours());

    return String.format(
        "(%s hour%s %s minute%s)",
        hour, handlePlural(hour), remainingMin, handlePlural(remainingMin));
  }

  // (s) postfix should be added to handle plural correctly
  private String handlePlural(long value) {
    if (value == 1) {
      return "";
    } else {
      return "s";
    }
  }

  public enum PrintFormat {
    TEXT,
    JSON,
    JSON_AND_TEXT
  }
}
