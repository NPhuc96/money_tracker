package personal.nphuc96.money_tracker.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Component
public class DateUtil {

    public List<String> getWeeks(Integer month, Integer year) {
        YearMonth date = YearMonth.of(year, month);
        return weeks(date);
    }

    private List<String> weeks(YearMonth date) {
        List<String> weeks = new ArrayList<>();
        weeks.add(getDayAndMonth(date, 1));
        weeks.add(getDayAndMonth(date, 8));
        weeks.add(getDayAndMonth(date, 15));
        weeks.add(getDayAndMonth(date, 22));
        return weeks;
    }

    private String getDayAndMonth(YearMonth date, int day) {
        StringBuilder week = new StringBuilder();
        LocalDate from = date.atDay(day);
        int remainDate = getRemainDate(date);
        LocalDate to = week(from, remainDate);
        week.append(from.getDayOfMonth()).append("/")
                .append(from.getMonthValue()).append("-")
                .append(to.getDayOfMonth()).append("/")
                .append(to.getMonthValue());
        return week.toString();
    }

    private LocalDate week(LocalDate from, int remainsDate) {
        if (from.getDayOfMonth() == 22) {
            return from.plusDays(6 + remainsDate);
        }
        return from.plusDays(6);
    }

    private int getRemainDate(YearMonth date) {
        return date.lengthOfMonth() % 4;
    }
}

