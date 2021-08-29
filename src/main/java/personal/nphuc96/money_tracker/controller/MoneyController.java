package personal.nphuc96.money_tracker.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.nphuc96.money_tracker.dto.TransactionDTO;
import personal.nphuc96.money_tracker.dto.TransactionGroupDTO;
import personal.nphuc96.money_tracker.entity.pagination.PagedTransaction;
import personal.nphuc96.money_tracker.exception.BadRequestException;
import personal.nphuc96.money_tracker.services.MoneyServices;

import java.util.List;


@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/api/")
public class MoneyController {

    private final MoneyServices moneyServices;

    @PostMapping("/group/add")
    public ResponseEntity<?> addOrUpdate(@RequestBody TransactionGroupDTO transactionGroupDTO) {
        try {
            log.info("Received object TransactionGroupDTO : {}", transactionGroupDTO.toString());

            moneyServices.addOrUpdate(transactionGroupDTO);
        } catch (Exception ex) {
            throw new BadRequestException("Error save or update data");
        }
        return ResponseEntity.ok("Successful");
    }

    @PostMapping("/transaction/add")
    public ResponseEntity<?> addOrUpdate(@RequestBody TransactionDTO transactionDTO) {
        try {
            log.info("Received object TransactionDTO : {}", transactionDTO.toString());
            moneyServices.addOrUpdate(transactionDTO);
        } catch (Exception ex) {
            throw new BadRequestException("Error save or update data");
        }
        return ResponseEntity.ok("Successful");
    }

    @DeleteMapping("/group/delete/{id}")
    public ResponseEntity<?> deleteTransactionGroup(@PathVariable("id") Integer id) {

        log.info("Received id : {} of Transaction Group", id);
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
    public List<TransactionGroupDTO> allGroup() {
        try {
            return moneyServices.allGroup();
        } catch (Exception ex) {
            throw new BadRequestException("Failed to fetch data");
        }
    }

    @GetMapping("/transaction")
    @ResponseBody
    public PagedTransaction findTransactions(
            @RequestParam(value = "size", defaultValue = "15", required = false) Integer size,
            @RequestParam(value = "page", defaultValue = "1", required = false) Integer page) {
        try {
            return moneyServices.findTransactions(size, page);
        } catch (Exception ex) {
            throw new BadRequestException("Failed to fetch data");
        }
    }
}
