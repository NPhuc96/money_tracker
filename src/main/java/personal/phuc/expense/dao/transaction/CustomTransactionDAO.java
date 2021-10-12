package personal.phuc.expense.dao.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import personal.phuc.expense.entity.Transaction;

import java.time.LocalDate;
import java.util.List;


public interface CustomTransactionDAO {
    List<Transaction> findAmountByDate(LocalDate from, LocalDate to, Integer userId);

    Page<Transaction> findTransactions(Integer userId, String sortBy, Pageable pageable);
}
