package com.jpmc.theater;

import java.time.Clock;
import java.time.LocalDate;

public class LocalDateProvider {
  private static LocalDateProvider instance = null;
  private final Clock clock = Clock.systemDefaultZone();

  private LocalDateProvider() {}

  public static synchronized LocalDateProvider singleton() {
    if (instance == null) {
      instance = new LocalDateProvider();
    }
    return instance;
  }

  public LocalDate currentDate() {
    return LocalDate.now(clock);
  }
}
