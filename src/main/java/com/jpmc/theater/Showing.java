package com.jpmc.theater;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class Showing {
  private final Movie movie;
  private final int sequenceOfTheDay;
  private final LocalDateTime startTime;

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public Showing(
      @JsonProperty("movie") Movie movie,
      @JsonProperty("sequenceOfTheDay") int sequenceOfTheDay,
      @JsonProperty("startTime") LocalDateTime startTime) {
    this.movie = movie;
    this.sequenceOfTheDay = sequenceOfTheDay;
    this.startTime = startTime;
  }

  public Movie getMovie() {
    return movie;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  @JsonIgnore
  public double getMovieFee() {
    return movie.calculateTicketPrice(this);
  }

  public int getSequenceOfTheDay() {
    return sequenceOfTheDay;
  }
}
