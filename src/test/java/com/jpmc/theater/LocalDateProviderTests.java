package com.jpmc.theater;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalDateProviderTests {
  @Test
  void makeSureCurrentTime() {
    assertThat(LocalDateProvider.singleton().currentDate())
        .isEqualTo(LocalDate.now(Clock.systemDefaultZone()));
  }
}
