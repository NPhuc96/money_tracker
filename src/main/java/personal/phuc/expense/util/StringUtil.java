package personal.phuc.expense.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class StringUtil {

    public String randomToken(int length,String words) {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(words.charAt(secureRandom.nextInt(words.length())));
        }
        return sb.toString();
    }

    public Map<LocalDate, LocalDate> rangeOfTime(List<String> dates, int year) {
        Map<LocalDate, LocalDate> rot = new LinkedHashMap<>();
        for (String date : dates) {
            String[] splitDash = splitDash(date);
            int[] firstSplit = splitStick(splitDash[0]);
            int[] secondSplit = splitStick(splitDash[1]);
            rot.put(LocalDate.of(year, firstSplit[1], firstSplit[0]),
                    LocalDate.of(year, secondSplit[1], secondSplit[0]));
        }
        return rot;
    }

    private String[] splitDash(String date) {
        return date.split("-");
    }

    private int[] splitStick(String date) {
        String[] split = date.split("/");
        int[] dates = new int[split.length];
        for (int i = 0; i < dates.length; i++) {
            dates[i] = Integer.parseInt(split[i]);
        }
        return dates;
    }
}
