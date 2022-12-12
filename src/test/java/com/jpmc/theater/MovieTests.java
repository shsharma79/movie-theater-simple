package com.jpmc.theater;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

public class MovieTests {
  @Test
  void specialMovieWith20PercentDiscount() {
    Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1);

    Showing showing =
        new Showing(
            spiderMan,
            5,
            // Fixed start time so that start time discount does not apply
            LocalDateTime.of(2022, Month.DECEMBER, 11, 17, 0));
    assertThat(spiderMan.calculateTicketPrice(showing)).isEqualByComparingTo(10d);
  }

  @Test
  public void whenSequenceDiscountIsMaximum_ShouldReturnSequenceDiscount() {
    // Special Movie discount 10 * 0.20 = $2
    // Start time discount 10 *.25 = $2.5
    // Sequence of the day discount for sequence 1 = $3
    Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 10, 1);
    Showing showing =
        new Showing(
            spiderMan,
            1,
            // Fixed start time so that start time discount applies
            LocalDateTime.of(2022, Month.DECEMBER, 11, 12, 0));
    assertThat(spiderMan.calculateTicketPrice(showing)).isEqualByComparingTo(7d);
  }

  @Test
  public void whenSpecialMovieDiscountIsMaximum_ShouldReturnSpecialMovieDiscount() {
    // Special Movie discount 12.5 * 0.20 = $2.5
    // Sequence of the day discount for sequence 2 = $2
    Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1);
    Showing showing =
        new Showing(
            spiderMan,
            2,
            // Fixed start time so that start time discount does not apply
            LocalDateTime.of(2022, Month.DECEMBER, 11, 17, 0));
    assertThat(spiderMan.calculateTicketPrice(showing)).isEqualByComparingTo(10d);
  }

  @Test
  public void whenStartTimeDiscountIsMaximum_ShouldReturnStartTimeDiscount() {
    // Special Movie discount 12.5 * 0.20 = $2.5
    // Start time discount 12.5 *.25 = $3.125
    // Sequence of the day discount for sequence 1 = $3
    Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1);
    Showing showing =
        new Showing(
            spiderMan,
            1,
            // Fixed start time so that start time discount does not apply
            LocalDateTime.of(2022, Month.DECEMBER, 11, 11, 0));
    assertThat(spiderMan.calculateTicketPrice(showing)).isEqualByComparingTo(9.375);
  }

  @Test
  public void when7thSequenceAndNoOtherDiscount_ShouldReturnOneDollarDiscount() {
    Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 0);
    Showing showing =
        new Showing(
            spiderMan,
            7,
            // Fixed start time so that start time discount does not apply
            LocalDateTime.of(2022, Month.DECEMBER, 11, 17, 0));
    assertThat(spiderMan.calculateTicketPrice(showing)).isEqualByComparingTo(11.5);
  }

  @Test
  public void when1stSequenceAndNoOtherDiscount_ShouldReturnThreeDollarDiscount() {
    Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 0);
    Showing showing =
        new Showing(
            spiderMan,
            1,
            // Fixed start time so that start time discount does not apply
            LocalDateTime.of(2022, Month.DECEMBER, 11, 17, 0));
    assertThat(spiderMan.calculateTicketPrice(showing)).isEqualByComparingTo(9.5);
  }

  @Test
  public void when2ndSequenceAndNoOtherDiscount_ShouldReturnTwoDollarDiscount() {
    Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 0);
    Showing showing =
        new Showing(
            spiderMan,
            2,
            // Fixed start time so that start time discount does not apply
            LocalDateTime.of(2022, Month.DECEMBER, 11, 17, 0));
    assertThat(spiderMan.calculateTicketPrice(showing)).isEqualByComparingTo(10.5);
  }

  @Test
  public void whenSpecialMovieAndNoOtherDiscount_ShouldReturn20PercentDiscount() {
    Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1);
    Showing showing =
        new Showing(
            spiderMan,
            3,
            // Fixed start time so that start time discount does not apply
            LocalDateTime.of(2022, Month.DECEMBER, 11, 17, 0));
    assertThat(spiderMan.calculateTicketPrice(showing)).isEqualByComparingTo(10d);
  }

  @Test
  public void whenStartTime11AMAndNoOtherDiscount_ShouldReturn25PercentDiscount() {
    Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 0);
    Showing showing =
        new Showing(
            spiderMan,
            3,
            // Fixed start time so that start time discount does not apply
            LocalDateTime.of(2022, Month.DECEMBER, 11, 11, 0));
    assertThat(spiderMan.calculateTicketPrice(showing)).isEqualByComparingTo(9.375d);
  }

  @Test
  public void whenStartTimeBetween11AMAnd4PMAndNoOtherDiscount_ShouldReturn25PercentDiscount() {
    Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 0);
    Showing showing =
        new Showing(
            spiderMan,
            3,
            // Fixed start time so that start time discount does not apply
            LocalDateTime.of(2022, Month.DECEMBER, 11, 14, 0));
    assertThat(spiderMan.calculateTicketPrice(showing)).isEqualByComparingTo(9.375d);
  }

  @Test
  public void whenStartTime4PMAndNoOtherDiscount_ShouldReturn25PercentDiscount() {
    Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 0);
    Showing showing =
        new Showing(
            spiderMan,
            3,
            // Fixed start time so that start time discount does not apply
            LocalDateTime.of(2022, Month.DECEMBER, 11, 11, 0));
    assertThat(spiderMan.calculateTicketPrice(showing)).isEqualByComparingTo(9.375d);
  }

  @Test
  public void whenNoDiscount_ShouldReturnTicketPrice() {
    Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 0);
    Showing showing =
        new Showing(
            spiderMan,
            3,
            // Fixed start time so that start time discount does not apply
            LocalDateTime.of(2022, Month.DECEMBER, 11, 17, 0));
    assertThat(spiderMan.calculateTicketPrice(showing))
        .isEqualByComparingTo(spiderMan.getTicketPrice());
  }
}
