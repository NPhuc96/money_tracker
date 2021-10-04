package personal.nphuc96.money_tracker.dto.reports;

import lombok.Data;

@Data
public class ReportRequest {
    private Integer month;

    private Integer year;

    private Integer userId;
}
