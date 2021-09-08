package personal.nphuc96.money_tracker.services.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import personal.nphuc96.money_tracker.dao.AppUserDAO;
import personal.nphuc96.money_tracker.dao.GroupsDAO;
import personal.nphuc96.money_tracker.dao.TransactionDAO;
import personal.nphuc96.money_tracker.dto.GroupsDTO;
import personal.nphuc96.money_tracker.dto.TransactionDTO;
import personal.nphuc96.money_tracker.entity.Groups;
import personal.nphuc96.money_tracker.entity.Transaction;
import personal.nphuc96.money_tracker.entity.app_user.AppUser;
import personal.nphuc96.money_tracker.entity.pagination.PagedTransaction;
import personal.nphuc96.money_tracker.exception.FailedSQLExeption;
import personal.nphuc96.money_tracker.services.MoneyServices;
import personal.nphuc96.money_tracker.util.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.capitalize;

@Service
@AllArgsConstructor
@Log4j2
public class MoneyService implements MoneyServices {

    private final TransactionDAO transactionDAO;
    private final ModelMapper modelMapper;
    private final AppUserDAO appUserDAO;
    private final GroupsDAO groupsDAO;


    @Override
    public void addOrUpdate(GroupsDTO groupsDTO) {
        AppUser appUser = appUserDAO.getById(groupsDTO.getAppUserId());
        log.info("Found AppUser : {}", appUser.toString());

        Groups groups = modelMapper.dtoToGroups(groupsDTO);
        log.info("Found Groups Before : {}", groups.toString());
        groups.setAppUser(appUser);
        log.info("Found Groups After : {}", groups.toString());
        groups.setName(capitalize(groups.getName()));
        try {
            groupsDAO.saveAndFlush(groups);
        } catch (DataAccessException ex) {
            log.info(ex.getCause());
            throw new FailedSQLExeption("Something went wrong. Can not save data");
        }
    }

    @Override
    public void addOrUpdate(TransactionDTO transactionDTO) {
        AppUser appUser = appUserDAO.getById(transactionDTO.getAppUserId());
        log.info("Found AppUser : {}", appUser.toString());

        Groups groups = groupsDAO.getById(transactionDTO.getGroupsId());
        log.info("Found Groups : {}", groups.toString());

        Transaction transaction = modelMapper.dtoToTransaction(transactionDTO);
        log.info("Found Transaction Before : {}", transaction.toString());
        transaction.setGroups(groups);
        transaction.setAppUser(appUser);
        log.info("Found Transaction After : {}", transaction.toString());

        try {
            transactionDAO.saveAndFlush(transaction);
        } catch (DataAccessException ex) {
            log.info(ex.getCause());
            throw new FailedSQLExeption("Something went wrong. Can not save data");
        }
    }


    @Override
    public void deleteGroup(Integer id, Integer userId) {
        try {
            groupsDAO.deleteByIdAndUserId(id, userId);
        } catch (DataAccessException ex) {
            log.info(ex.getCause());
            throw new FailedSQLExeption("Failed in deleting group with id : " + id);
        }
    }

    @Override
    public void deleteTransaction(Integer id, Integer userId) {
        try {
            transactionDAO.deleteByIdAndUserId(id, userId);
        } catch (DataAccessException ex) {
            log.info(ex.getCause());
            throw new FailedSQLExeption("Failed in deleting transaction with id : " + id);
        }
    }

    @Override
    public List<GroupsDTO> findGroupsByUserId(Integer userId) {
        List<Groups> groupsList =
                groupsDAO.findGroupsByUserId(userId);
        return groupsList
                .stream().map(modelMapper::groupsToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PagedTransaction pagedTransactionByUserId(Integer pageSize, Integer currentPage, Integer userId) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        try {
            Page<Transaction> page = transactionDAO.findTransactionByUserId(userId, pageable);
            List<TransactionDTO> dtos = buildTransactionDtoList(page);
            int totalPages = page.getTotalPages();
            return buildPagedTransaction(currentPage, totalPages, dtos);
        } catch (DataAccessException ex) {
            log.info(ex.getCause());
            throw new FailedSQLExeption("Failed in fetching data with this id : " + userId);
        }
    }

    private List<TransactionDTO> buildTransactionDtoList(Page<Transaction> page) {
        return page.getContent()
                .stream()
                .map(modelMapper::transactionToDTO)
                .collect(Collectors.toList());
    }

    private PagedTransaction buildPagedTransaction(Integer currentPage, Integer totalPages, List<TransactionDTO> dtos) {
        return PagedTransaction.builder()
                .currentPage(currentPage)
                .totalPages(totalPages)
                .prev(currentPage - 1 >= 1)
                .next(currentPage + 1 <= totalPages)
                .transactions(dtos)
                .build();
    }
}
