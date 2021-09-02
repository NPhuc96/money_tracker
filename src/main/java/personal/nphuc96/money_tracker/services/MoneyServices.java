package personal.nphuc96.money_tracker.services;

import personal.nphuc96.money_tracker.dto.GroupsDTO;
import personal.nphuc96.money_tracker.dto.TransactionDTO;
import personal.nphuc96.money_tracker.entity.pagination.PagedTransaction;

import java.util.List;


public interface MoneyServices {

    void addOrUpdate(GroupsDTO groupsDTO);

    void addOrUpdate(TransactionDTO transactionDTO);

    void deleteGroup(Integer id, Integer userId);

    void deleteTransaction(Integer id, Integer userId);

    List<GroupsDTO> findGroupsByUserId(Integer userId);

    PagedTransaction pagedTransactionByUserId(Integer pageSize,
                                              Integer currentPage,
                                              Integer userId);


}
