package personal.phuc.expense.services.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import personal.phuc.expense.dao.transaction.CustomTransactionDAO;
import personal.phuc.expense.dao.transaction.TransactionDAO;
import personal.phuc.expense.entity.Transaction;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Log4j2
class CustomTransactionDAOImplTest {
    @Autowired
    private CustomTransactionDAO service;
    @MockBean
    TransactionDAO transactionDAO;

    private int total = 12;
    private LocalDate from = LocalDate.of(2021, 9, 1);
    private LocalDate to = LocalDate.of(2021, 9, 7);
    private Integer userId = 1;

    @Test
    public void findAmountByDateTest() {
        List<Transaction> transactions = service.findAmountByDate(from, to, userId);
        for (Transaction t : transactions) {
            log.info(t.getAmount());
        }
        assertEquals(total, transactions.size());
    }
}