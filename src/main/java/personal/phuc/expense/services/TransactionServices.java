package personal.phuc.expense.services;

import personal.phuc.expense.dto.GroupsDTO;
import personal.phuc.expense.dto.TransactionDTO;
import personal.phuc.expense.dto.reports.ReportRequest;
import personal.phuc.expense.dto.reports.ReportResponse;
import personal.phuc.expense.services.service.pagination.PageRequests;
import personal.phuc.expense.services.service.pagination.Pagination;

import java.util.List;


public interface TransactionServices {

    void addOrUpdate(GroupsDTO groupsDTO);

    void addOrUpdate(TransactionDTO transactionDTO);

    void deleteGroup(Integer id, Integer userId);

    void deleteTransaction(Integer id, Integer userId);

    List<GroupsDTO> findGroups(Integer userId);

    GroupsDTO findGroup(Integer id, Integer userId);

    Pagination findTransactions(PageRequests pageRequests);

    TransactionDTO findTransaction(Integer id, Integer userId);

    ReportResponse findReport(ReportRequest request);
}
