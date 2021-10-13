package personal.phuc.expense.services.service.pagination;

import lombok.Data;
import org.springframework.data.domain.Page;
import personal.phuc.expense.dto.TransactionDTO;

import java.util.List;

@Data
public class Pagination {

    private PageInfo pageInfo;

    private List<TransactionDTO> transactions;

    public Pagination(List<TransactionDTO> transactions, PageRequests pageRequests, Page<?> page) {
        this.pageInfo = new PageInfo(pageRequests, page);
        this.transactions = transactions;
    }


}
