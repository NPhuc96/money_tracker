package personal.nphuc96.money_tracker.dao.transaction;

import personal.nphuc96.money_tracker.entity.Transaction;

import java.time.LocalDate;
import java.util.List;


public interface CustomTransactionDAO {
    List<Transaction> findAmountByDate(LocalDate from, LocalDate to, Integer userId);
}
