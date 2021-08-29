package personal.nphuc96.money_tracker.entity.pagination;

import lombok.Builder;
import lombok.Data;
import personal.nphuc96.money_tracker.dto.TransactionDTO;

import java.util.List;

@Data
@Builder
public class PagedTransaction {

    private Integer currentPage;

    private Integer totalPages;

    private Boolean prev;

    private Boolean next;

    private List<TransactionDTO> transactions;


}
