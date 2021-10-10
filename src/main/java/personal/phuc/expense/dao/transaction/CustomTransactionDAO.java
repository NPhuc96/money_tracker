package personal.phuc.expense.dao.transaction;

import personal.phuc.expense.entity.Transaction;

import java.time.LocalDate;
import java.util.List;


public interface CustomTransactionDAO {
    List<Transaction> findAmountByDate(LocalDate from, LocalDate to, Integer userId);
}
