package personal.nphuc96.money_tracker.services.service.pagination;

import lombok.Data;

@Data
public class PageRequests {
    private Integer page;
    private Integer pageSize;
    private Integer userId;
}
