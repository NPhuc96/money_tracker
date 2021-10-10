package personal.phuc.expense.util;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Log4j2
class DateUtilTest {

    @DisplayName("Test Output of DateUtil")
    @Test
    void givenInputGetList(){
        DateUtil dateUtil = new DateUtil();
        Integer month = 10;
        Integer year = 2021;
        List<String> weeks = dateUtil.getWeeks(month, year);
        for(String week : weeks){
           log.info(week);
        }
        assertEquals(4, weeks.size());
    }
}