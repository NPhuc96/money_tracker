package personal.nphuc96.money_tracker.services;

import personal.nphuc96.money_tracker.dto.GroupsDTO;
import personal.nphuc96.money_tracker.dto.TransactionDTO;
import personal.nphuc96.money_tracker.entity.pagination.PageRequests;
import personal.nphuc96.money_tracker.entity.pagination.Pagination;

import java.util.List;


public interface MoneyServices {

    void addOrUpdate(GroupsDTO groupsDTO);

    void addOrUpdate(TransactionDTO transactionDTO);

    void deleteGroup(Integer id, Integer userId);

    void deleteTransaction(Integer id, Integer userId);

    List<GroupsDTO> findGroups(Integer userId);

    GroupsDTO findGroup(Integer id, Integer userId);

    Pagination findTransactions(PageRequests pageRequests);

    TransactionDTO findTransaction(Integer id, Integer userId);


}
