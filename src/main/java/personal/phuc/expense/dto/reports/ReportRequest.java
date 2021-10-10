package personal.phuc.expense.dto.reports;

import lombok.Data;

@Data
public class ReportRequest {
    private Integer month;

    private Integer year;

    private Integer userId;
}
