package personal.nphuc96.money_tracker.dto.reports;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class ReportResponse {
    private List<String> weekOfMonth;
    private List<BigDecimal> spentOfWeek;
    private BigDecimal spentOfMonth;


}
