package com.jpmc.theater;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservationTests {

  @Test
  public void totalFee() {
    var customer = new Customer("John Doe", "unused-id");
    var showing =
        new Showing(
            new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1),
            1,
            // Fix the start time to make unit test's outcome predictable with the new start time
            // discount
            LocalDateTime.of(2022, Month.DECEMBER, 11, 10, 0));
    assertThat(new Reservation(customer, showing, 3).totalFee()).isEqualByComparingTo(28.5);
  }

  @Test
  public void whenNoDiscount_ShouldReturnCorrectTotalFee() {
    var customer = new Customer("John Doe", "unused-id");
    var showing =
        new Showing(
            // Special code: 0
            new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 0),
            // Sequence of the day: 4
            4,
            // Start Time: 10 AM
            LocalDateTime.of(2022, Month.DECEMBER, 11, 10, 0));
    assertThat(new Reservation(customer, showing, 3).totalFee()).isEqualByComparingTo(37.5);
  }

  @Test
  public void whenSpecialMovieDiscount_ShouldReturnCorrectTotalFee() {
    var customer = new Customer("John Doe", "unused-id");
    var showing =
        new Showing(
            // Special code: 1 (20% Discount)
            new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1),
            // Sequence of the day: 4
            4,
            // Start Time: 10 AM
            LocalDateTime.of(2022, Month.DECEMBER, 11, 10, 0));
    assertThat(new Reservation(customer, showing, 3).totalFee()).isEqualByComparingTo(30.0);
  }

  @Test
  public void whenSequenceDiscount_ShouldReturnCorrectTotalFee() {
    var customer = new Customer("John Doe", "unused-id");
    var showing =
        new Showing(
            // Special code: 0
            new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 0),
            // Sequence of the day: 2 ($2 discount)
            2,
            // Start Time: 10 AM
            LocalDateTime.of(2022, Month.DECEMBER, 11, 10, 0));
    assertThat(new Reservation(customer, showing, 3).totalFee()).isEqualByComparingTo(31.5);
  }

  @Test
  public void whenStartTimeDiscount_ShouldReturnCorrectTotalFee() {
    var customer = new Customer("John Doe", "unused-id");
    var showing =
        new Showing(
            // Special code: 0
            new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 0),
            // Sequence of the day: 4
            4,
            // Start Time: 12 AM (25% discount)
            LocalDateTime.of(2022, Month.DECEMBER, 11, 12, 0));
    assertThat(new Reservation(customer, showing, 3).totalFee()).isEqualByComparingTo(28.125);
  }
}
