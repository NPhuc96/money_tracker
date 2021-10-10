package personal.phuc.expense.services.service.pagination;

import lombok.Data;

@Data
public class PageRequests {
    private Integer page;
    private Integer pageSize;
    private Integer userId;
}
