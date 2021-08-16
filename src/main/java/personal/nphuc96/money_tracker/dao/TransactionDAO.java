package personal.nphuc96.money_tracker.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import personal.nphuc96.money_tracker.entity.Transaction;

@Repository
public interface TransactionDAO extends JpaRepository<Transaction, Integer> {

    @Query(value = "SELECT t FROM Transaction t order by t.id desc")
    Page<Transaction> findAndSortById(Pageable pageable);
}
