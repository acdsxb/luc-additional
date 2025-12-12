package vip.luosu.additional.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

    private DateUtils() {
    }

    public static final String[] WEEK_EN_ABBREVIATION = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter SIMPLE_TIME_FORMATTER =  DateTimeFormatter.ofPattern("HHmmss");

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public final static String PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}
