package personal.nphuc96.money_tracker.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.nphuc96.money_tracker.dto.GroupsDTO;
import personal.nphuc96.money_tracker.dto.TransactionDTO;
import personal.nphuc96.money_tracker.entity.pagination.PagedTransaction;
import personal.nphuc96.money_tracker.services.MoneyServices;

import java.util.List;


@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/api/")
public class MoneyController {

    private final MoneyServices moneyServices;

    @PostMapping("/group/add")
    public ResponseEntity<?> addOrUpdate(@RequestBody GroupsDTO groupsDTO) {

        log.info("Received object GroupsDTO : {}", groupsDTO.toString());

        moneyServices.addOrUpdate(groupsDTO);

        return ResponseEntity.ok("Successful");
    }

    @PostMapping("/transaction/add")
    public ResponseEntity<?> addOrUpdate(@RequestBody TransactionDTO transactionDTO) {

        log.info("Received object TransactionDTO : {}", transactionDTO.toString());
        moneyServices.addOrUpdate(transactionDTO);

        return ResponseEntity.ok("Successful");
    }

    @DeleteMapping("/group/delete/{id}")
    public ResponseEntity<?> deleteTransactionGroup(@PathVariable("id") Integer id) {

        log.info("Received id : {} of Transaction Groups", id);
        moneyServices.deleteTransactionGroup(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/transaction/delete/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable("id") Integer id) {

        log.info("Received id : {}", id);
        moneyServices.deleteTransaction(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/group/all")
    @ResponseBody
    public List<GroupsDTO> allGroup(@RequestParam(value = "userid") Integer userId) {

        return moneyServices.findGroupsByUserId(userId);

    }

    @GetMapping("/transaction")
    @ResponseBody
    public PagedTransaction findTransactions(
            @RequestParam(value = "size", defaultValue = "15", required = false) Integer size,
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(value = "userid") Integer userId) {

        return moneyServices.pagedTransactionByUserId(size, page, userId);
    }
}

