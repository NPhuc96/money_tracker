package personal.phuc.expense.services.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import personal.phuc.expense.dao.transaction.CustomTransactionDAO;
import personal.phuc.expense.dao.transaction.TransactionDAO;
import personal.phuc.expense.entity.Transaction;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@Log4j2
class CustomTransactionDAOImplTest {

    private final int total = 12;
    private final LocalDate from = LocalDate.of(2021, 9, 1);
    private final LocalDate to = LocalDate.of(2021, 9, 7);
    private final Integer userId = 1;
    private final int page = 0;
    private final int pageSize = 10;
    private final String sortBy = "id";
    @Qualifier("customTransactionDAOImpl")
    @Autowired
    private CustomTransactionDAO service;
    @MockBean
    private TransactionDAO transactionDAO;

    @Test
    public void findAmountByDateTest() {
        List<Transaction> transactions = service.findAmountByDate(from, to, userId);
        for (Transaction t : transactions) {
            log.info(t.getAmount());
        }
        assertEquals(total, transactions.size());
    }

    @Test
    public void findTransactionsOrderByIdTest() {
        Page<Transaction> p = service.findTransactions(1, sortBy, PageRequest.of(page, pageSize));
        assertNotNull(p);
        log.info(p);
    }

}