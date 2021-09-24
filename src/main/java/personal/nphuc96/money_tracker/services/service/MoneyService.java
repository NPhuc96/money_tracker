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
import personal.nphuc96.money_tracker.entity.pagination.PageRequests;
import personal.nphuc96.money_tracker.entity.pagination.Pagination;
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
        AppUser appUser = appUserDAO.getById(groupsDTO.getUserId());
        Groups groups = modelMapper.dtoToGroups(groupsDTO);
        groups.setAppUser(appUser);
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
        AppUser appUser = appUserDAO.getById(transactionDTO.getUserId());
        Transaction transaction = modelMapper.dtoToTransaction(transactionDTO);
        if(transaction.getGroups() !=null){
            transaction.getGroups().setAppUser(appUser);
        }
        transaction.setAppUser(appUser);
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
    public List<GroupsDTO> findGroups(Integer userId) {
        List<Groups> groupsList =
                groupsDAO.findGroups(userId);
        return groupsList
                .stream().map(modelMapper::groupsToDto)
                .collect(Collectors.toList());
    }

    @Override
    public GroupsDTO findGroup(Integer id, Integer userId) {
        try{
            Groups groups = groupsDAO.findGroup(id, userId);
            return modelMapper.groupsToDto(groups);
        }catch (DataAccessException ex){
            throw new FailedSQLExeption("Failed in fetching data with this id : "+id);
        }
    }

    @Override
    public Pagination findTransactions(PageRequests pageRequests) {
        Pageable pageable = PageRequest.of(pageRequests.getPage() - 1, pageRequests.getPageSize());
        try {
            Page<Transaction> page = transactionDAO.findTransactions(pageRequests.getUserId(), pageable);
            List<TransactionDTO> dtos = buildTransactionDtoList(page);
            return buildPagination(dtos, pageRequests, page);
        } catch (DataAccessException ex) {
            log.info(ex.getCause());
            throw new FailedSQLExeption("Failed in fetching data with this id : " + pageRequests.getUserId());
        }
    }

    @Override
    public TransactionDTO findTransaction(Integer id, Integer userId) {
        try{
            Transaction transaction = transactionDAO.findTransaction(id, userId);
            return modelMapper.transactionToDTO(transaction);
        }catch (DataAccessException ex){
            throw new FailedSQLExeption("Failed in fetching data with this id : "+id);
        }

    }

    private List<TransactionDTO> buildTransactionDtoList(Page<Transaction> page) {
        return page.getContent()
                .stream()
                .map(modelMapper::transactionToDTO)
                .collect(Collectors.toList());
    }

    private Pagination buildPagination(List<TransactionDTO> dtos, PageRequests pageRequests, Page<?> page) {
        return new Pagination(dtos, pageRequests, page);
    }




}
