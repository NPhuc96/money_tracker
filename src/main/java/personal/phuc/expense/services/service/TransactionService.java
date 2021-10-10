package personal.phuc.expense.services.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import personal.phuc.expense.dao.transaction.GroupsDAO;
import personal.phuc.expense.dao.transaction.TransactionDAO;
import personal.phuc.expense.dao.user.AppUserDAO;
import personal.phuc.expense.dto.GroupsDTO;
import personal.phuc.expense.dto.TransactionDTO;
import personal.phuc.expense.dto.reports.ReportRequest;
import personal.phuc.expense.dto.reports.ReportResponse;
import personal.phuc.expense.entity.Groups;
import personal.phuc.expense.entity.Transaction;
import personal.phuc.expense.entity.user.AppUser;
import personal.phuc.expense.exception.FailedSQLExeption;
import personal.phuc.expense.services.TransactionServices;
import personal.phuc.expense.services.service.pagination.PageRequests;
import personal.phuc.expense.services.service.pagination.Pagination;
import personal.phuc.expense.util.DateUtil;
import personal.phuc.expense.util.ModelMapper;
import personal.phuc.expense.util.StringUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.capitalize;

@Service
@AllArgsConstructor
@Log4j2
public class TransactionService implements TransactionServices {

    private final TransactionDAO transactionDAO;
    private final ModelMapper modelMapper;
    private final AppUserDAO appUserDAO;
    private final GroupsDAO groupsDAO;
    private final StringUtil stringUtil;
    private final DateUtil dateUtil;

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
        if (transaction.getGroups() != null) {
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
            transactionDAO.updateTransactionByGroupsId(id);
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
        try {
            Groups groups = groupsDAO.findGroup(id, userId);
            return modelMapper.groupsToDto(groups);
        } catch (DataAccessException ex) {
            throw new FailedSQLExeption("Failed in fetching data with this id : " + id);
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
        try {
            Transaction transaction = transactionDAO.findTransaction(id, userId);
            return modelMapper.transactionToDTO(transaction);
        } catch (DataAccessException ex) {
            throw new FailedSQLExeption("Failed in fetching data with this id : " + id);
        }
    }

    @Override
    public ReportResponse findReport(ReportRequest request) {
        List<String> dates = dateUtil.getWeeks(request.getMonth(), request.getYear());
        Map<LocalDate, LocalDate> rangeOfTime = stringUtil.rangeOfTime(dates, request.getYear());
        List<BigDecimal> spentOfWeek = spentOfWeeks(rangeOfTime, request.getUserId());
        BigDecimal spentOfMonth = spentOfMonth(spentOfWeek);
        return new ReportResponse(dates, spentOfWeek, spentOfMonth);
    }

    private List<BigDecimal> spentOfWeeks(Map<LocalDate, LocalDate> rangeOfTime, Integer userId) {
        List<BigDecimal> amountOfWeek = new ArrayList<>();
        for (var dates : rangeOfTime.entrySet()) {
            amountOfWeek.add(spentOfWeek(dates, userId));
        }
        return amountOfWeek;
    }

    private BigDecimal spentOfWeek(Map.Entry<LocalDate, LocalDate> dates, Integer userId) {
        try {
            List<Transaction> amounts = transactionDAO
                    .findAmountByDate(dates.getKey(), dates.getValue(), userId);
            BigDecimal amount = BigDecimal.ZERO;
            for (Transaction t : amounts) {
                amount = amount.add(t.getAmount());
            }
            return amount;
        } catch (FailedSQLExeption ex) {
            throw new FailedSQLExeption("Error fetching data");
        }
    }

    private BigDecimal spentOfMonth(List<BigDecimal> spentOfWeek) {
        BigDecimal amountOfMonth = BigDecimal.ZERO;
        for (BigDecimal amount : spentOfWeek) {
            amountOfMonth = amountOfMonth.add(amount);
        }
        return amountOfMonth;
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
