package personal.nphuc96.money_tracker.services.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import personal.nphuc96.money_tracker.dao.TransactionDAO;
import personal.nphuc96.money_tracker.dao.TransactionGroupDAO;
import personal.nphuc96.money_tracker.dto.TransactionDTO;
import personal.nphuc96.money_tracker.dto.TransactionGroupDTO;
import personal.nphuc96.money_tracker.entity.Transaction;
import personal.nphuc96.money_tracker.entity.TransactionGroup;
import personal.nphuc96.money_tracker.entity.pagination.PagedTransaction;
import personal.nphuc96.money_tracker.exception.ResourceNotFoundException;
import personal.nphuc96.money_tracker.services.MoneyServices;
import personal.nphuc96.money_tracker.util.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class MoneyService implements MoneyServices {

    private final ModelMapper modelMapper;
    private final TransactionGroupDAO transactionGroupDAO;
    private final TransactionDAO transactionDAO;


    @Override
    public void addOrUpdate(TransactionGroupDTO transactionGroupDTO) {

        TransactionGroup transactionGroup = modelMapper.dtoToTransactionGroup(transactionGroupDTO);
        log.info("Found Transaction Group : {}", transactionGroup);
        transactionGroupDAO.save(transactionGroup);

    }

    @Override
    public void addOrUpdate(TransactionDTO transactionDTO) {

        Transaction transaction = modelMapper.dtoToTransaction(transactionDTO);
        log.info("Found Transaction : {}", transaction);
        transactionDAO.save(transaction);

    }


    @Override
    public void deleteTransactionGroup(Integer id) {
        Optional<TransactionGroup> transactionGroup = transactionGroupDAO.findById(id);
        if (transactionGroup.isEmpty()) {
            throw new ResourceNotFoundException("Can not find Transaction Group with id : " + id);
        }
        transactionGroupDAO.delete(transactionGroup.get());
    }

    @Override
    public void deleteTransaction(Integer id) {
        Optional<Transaction> transaction = transactionDAO.findById(id);
        if (transaction.isEmpty()) {
            throw new ResourceNotFoundException("Can not find Transaction with id : " + id);
        }
        transactionDAO.delete(transaction.get());
    }

    @Override
    public List<TransactionGroupDTO> allGroup() {
        List<TransactionGroup> transactionGroupList =
                transactionGroupDAO.findAll(Sort.by(Sort.Direction.ASC, "name"));
        return transactionGroupList
                .stream().map(modelMapper::transactionGroupToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PagedTransaction findTransactions(Integer pageSize, Integer currentPage) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<Transaction> transactionsPage = transactionDAO.findAndSortById(pageable);
        List<TransactionDTO> transactionDTOList =
                transactionsPage.getContent()
                        .stream()
                        .map(modelMapper::transactionToDTO)
                        .collect(Collectors.toList());
        int totalPages = transactionsPage.getTotalPages();
        return PagedTransaction.builder()
                .currentPage(currentPage)
                .totalPages(totalPages)
                .prev(currentPage - 1 >= 1)
                .next(currentPage + 1 <= totalPages)
                .transactions(transactionDTOList)
                .build();
    }


}
