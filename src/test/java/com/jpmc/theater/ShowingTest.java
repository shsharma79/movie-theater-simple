package com.jpmc.theater;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

public class ShowingTest {

  @Test
  public void whenNoDiscount_ShouldReturnTicketPriceAsMovieFee() {
    var showing =
        new Showing(
            // Special code: 0
            new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 0),
            // Sequence of the day: 4
            4,
            // Start Time: 10 AM
            LocalDateTime.of(2022, Month.DECEMBER, 11, 10, 0));
    assertThat(showing.getMovieFee()).isEqualByComparingTo(showing.getMovie().getTicketPrice());
  }

  @Test
  public void whenSpecialMovie_ShouldReturnDiscountedMovieFee() {
    var showing =
        new Showing(
            // Special code: 1
            new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1),
            // Sequence of the day: 4
            4,
            // Start Time: 10 AM
            LocalDateTime.of(2022, Month.DECEMBER, 11, 10, 0));
    assertThat(showing.getMovieFee()).isEqualByComparingTo(10.0);
  }

  @Test
  public void whenSequenceDiscountApplies_ShouldReturnDiscountedMovieFee() {
    var showing =
        new Showing(
            // Special code: 0
            new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 0),
            // Sequence of the day: 2
            2,
            // Start Time: 10 AM
            LocalDateTime.of(2022, Month.DECEMBER, 11, 10, 0));
    assertThat(showing.getMovieFee()).isEqualByComparingTo(10.5);
  }

  @Test
  public void whenStartTimeDiscountApplies_ShouldReturnDiscountedMovieFee() {
    var showing =
        new Showing(
            // Special code: 0
            new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 0),
            // Sequence of the day: 4
            4,
            // Start Time: 11 AM
            LocalDateTime.of(2022, Month.DECEMBER, 11, 11, 0));
    assertThat(showing.getMovieFee()).isEqualByComparingTo(9.375);
  }

  @Test
  public void whenMultipleDiscountsApply_ShouldReturnMaxDiscountedMovieFee() {
    var showing =
        new Showing(
            // Special code: 1
            new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1),
            // Sequence of the day: 1
            1,
            // Start Time: 11 AM
            LocalDateTime.of(2022, Month.DECEMBER, 11, 11, 0));
    assertThat(showing.getMovieFee()).isEqualByComparingTo(9.375);
  }
}
