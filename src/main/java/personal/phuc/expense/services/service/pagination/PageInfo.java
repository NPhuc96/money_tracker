package personal.phuc.expense.services.service.pagination;

import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class PageInfo {

    private Long totalElements;

    private Integer currentPage;

    private Integer totalPages;

    private Integer pageSize;

    private Integer from;

    private Integer to;

    public PageInfo(PageRequests pageRequests, Page<?> page) {
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.currentPage = pageRequests.getPage();
        this.pageSize = pageRequests.getPageSize();
        this.from = from(pageRequests.getPage(), pageRequests.getPageSize());
        this.to = to(pageRequests.getPage(), pageRequests.getPageSize(), (int) page.getTotalElements());
    }

    private Integer from(Integer currentPage, Integer pageSize) {
        return ((currentPage - 1) * pageSize) + 1;
    }

    private Integer to(Integer currentPage, Integer pageSize, Integer total) {
        return currentPage * pageSize < total ? currentPage * pageSize : total;
    }

}
