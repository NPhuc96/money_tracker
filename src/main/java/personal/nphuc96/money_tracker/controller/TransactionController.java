package personal.nphuc96.money_tracker.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.nphuc96.money_tracker.dto.GroupsDTO;
import personal.nphuc96.money_tracker.dto.TransactionDTO;
import personal.nphuc96.money_tracker.dto.reports.ReportRequest;
import personal.nphuc96.money_tracker.dto.reports.ReportResponse;
import personal.nphuc96.money_tracker.services.TransactionServices;
import personal.nphuc96.money_tracker.services.service.pagination.PageRequests;
import personal.nphuc96.money_tracker.services.service.pagination.Pagination;

import java.util.List;


@AllArgsConstructor
@RestController
@RequestMapping("/api")
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

