package edu.asu.cassess.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class DateUtil {

  public static boolean isDateLesserThanEqual(LocalDate startDate, LocalDate endDate) {
    if(startDate == null || endDate == null) {
      return false;
    }
    return startDate.isBefore(endDate) || startDate.isEqual(endDate);
  }

  public static boolean isDateGreaterThanEqual(LocalDate date1, LocalDate date2) {
    if(date1 == null || date2 == null) {
      return false;
    }
    return date1.isAfter(date2) || date1.isEqual(date2);
  }

  public static boolean isDateBetween(LocalDate targetDate, LocalDate startDate, LocalDate endDate) {
    if(startDate == null || endDate == null || targetDate == null) {
      return false;
    }
    return (targetDate.isAfter(startDate) || targetDate.isEqual(startDate)) && targetDate.isBefore(endDate);
  }

  public static LocalDate stringToDate(String dateString) {
    Instant instant = Instant.parse(dateString);
    return LocalDate.ofInstant(instant, ZoneOffset.UTC);
  }
}
