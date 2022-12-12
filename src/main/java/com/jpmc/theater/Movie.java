package com.jpmc.theater;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

public class Movie {
  private static final int MOVIE_CODE_SPECIAL = 1;

  private final String title;
  private final Duration runningTime;
  private final double ticketPrice;
  private final int specialCode;

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public Movie(
      @JsonProperty("title") String title,
      @JsonProperty("runningTime") Duration runningTime,
      @JsonProperty("ticketPrice") double ticketPrice,
      @JsonProperty("specialCode") int specialCode) {
    Objects.requireNonNull(title);
    Objects.requireNonNull(runningTime);
    this.title = title;
    this.runningTime = runningTime;
    this.ticketPrice = ticketPrice;
    this.specialCode = specialCode;
  }

  public String getTitle() {
    return title;
  }

  public Duration getRunningTime() {
    return runningTime;
  }

  public double getTicketPrice() {
    return ticketPrice;
  }

  public int getSpecialCode() {
    return specialCode;
  }

  public double calculateTicketPrice(Showing showing) {
    return ticketPrice - getDiscount(showing);
  }

  private double getDiscount(Showing showing) {
    int showSequence = showing.getSequenceOfTheDay();
    double discount = 0;

    double specialDiscount = 0;
    if (MOVIE_CODE_SPECIAL == specialCode) {
      specialDiscount = ticketPrice * 0.2; // 20% discount for special movie
    }

    double sequenceDiscount = 0;
    if (showSequence == 1) {
      sequenceDiscount = 3; // $3 discount for 1st show
    } else if (showSequence == 2) {
      sequenceDiscount = 2; // $2 discount for 2nd show
    } else if (showSequence == 7) {
      sequenceDiscount = 1; // $1 discount for the 7th show
    }

    double startTimeDiscount = 0;
    var start = LocalTime.of(11, 0); // 11 AM
    var end = LocalTime.of(16, 0); // 4 PM
    var showingTime = showing.getStartTime().toLocalTime();
    if (isTimeInTheRange(start, end, showingTime)) {
      startTimeDiscount =
          ticketPrice * 0.25; // 25% discount for movies starting between 11 AM and 4 PM
    }

    // biggest discount wins
    if (specialDiscount > discount) {
      discount = specialDiscount;
    }

    if (sequenceDiscount > discount) {
      discount = sequenceDiscount;
    }

    if (startTimeDiscount > discount) {
      discount = startTimeDiscount;
    }
    return discount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Movie movie = (Movie) o;
    return Double.compare(movie.ticketPrice, ticketPrice) == 0
        && specialCode == movie.specialCode
        && title.equals(movie.title)
        && runningTime.equals(movie.runningTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, runningTime, ticketPrice, specialCode);
  }

  private boolean isTimeInTheRange(LocalTime start, LocalTime end, LocalTime input) {
    return input.equals(start)
        || (input.isAfter(start) && input.isBefore(end))
        || input.equals(end);
  }
}
