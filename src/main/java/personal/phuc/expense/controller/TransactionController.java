package personal.phuc.expense.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.phuc.expense.dto.GroupsDTO;
import personal.phuc.expense.dto.TransactionDTO;
import personal.phuc.expense.dto.reports.ReportRequest;
import personal.phuc.expense.dto.reports.ReportResponse;
import personal.phuc.expense.services.TransactionServices;
import personal.phuc.expense.services.service.pagination.PageRequests;
import personal.phuc.expense.services.service.pagination.Pagination;

import java.util.List;


@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TransactionController {

    private final TransactionServices transactionServices;

    @PostMapping("/group/add")
    public ResponseEntity<?> addOrUpdate(@RequestBody GroupsDTO groupsDTO) {
        transactionServices.addOrUpdate(groupsDTO);
        return ResponseEntity.ok("Successful");
    }

    @PostMapping("/transaction/add")
    public ResponseEntity<?> addOrUpdate(@RequestBody TransactionDTO transactionDTO) {
        transactionServices.addOrUpdate(transactionDTO);
        return ResponseEntity.ok("Successful");
    }

    @DeleteMapping("/group/delete")
    public ResponseEntity<?> deleteTransactionGroup(
            @RequestParam("id") Integer id,
            @RequestParam("userId") Integer userId) {
        transactionServices.deleteGroup(id, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/transaction/delete")
    public ResponseEntity<?> deleteTransaction(
            @RequestParam("id") Integer id,
            @RequestParam("userId") Integer userId) {
        transactionServices.deleteTransaction(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/groups")
    @ResponseBody
    public List<GroupsDTO> findGroups(@RequestParam(value = "userId") Integer userId) {
        return transactionServices.findGroups(userId);
    }

    @PostMapping("/transactions")
    @ResponseBody
    public Pagination findTransactions(@RequestBody PageRequests pageRequests) {
        return transactionServices.findTransactions(pageRequests);
    }

    @PostMapping("/transaction")
    @ResponseBody
    public TransactionDTO findTransaction(@RequestParam("id") Integer id,
                                          @RequestParam("userId") Integer userId) {
        return transactionServices.findTransaction(id, userId);
    }

    @PostMapping("/group")
    @ResponseBody
    public GroupsDTO findGroup(@RequestParam("id") Integer id,
                               @RequestParam("userId") Integer userId) {
        return transactionServices.findGroup(id, userId);
    }

    @PostMapping("/report")
    @ResponseBody
    public ReportResponse findReport(@RequestBody ReportRequest request) {
        return transactionServices.findReport(request);
    }

}

