package personal.nphuc96.money_tracker.services;

import personal.nphuc96.money_tracker.dto.TransactionDTO;
import personal.nphuc96.money_tracker.dto.TransactionGroupDTO;
import personal.nphuc96.money_tracker.entity.pagination.PagedTransaction;

import java.util.List;


public interface MoneyServices {

    void addOrUpdate(TransactionGroupDTO transactionGroupDTO);

    void addOrUpdate(TransactionDTO transactionDTO);

    void deleteTransactionGroup(Integer id);

    void deleteTransaction(Integer id);

    List<TransactionGroupDTO> allGroup();

    PagedTransaction findTransactions(Integer pageSize, Integer currentPage);


}
