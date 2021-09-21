package personal.nphuc96.money_tracker.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import personal.nphuc96.money_tracker.entity.Transaction;

@Repository
public interface TransactionDAO extends JpaRepository<Transaction, Integer> {

    @Query(value = "SELECT t FROM Transaction t where t.appUser.id =?1  order by t.id desc")
    Page<Transaction> findTransactionByUserId(Integer userId, Pageable pageable);

    @Query(value ="Select t from Transaction  t where t.id=?1 and t.appUser.id=?2")
    Transaction findTransactionById(Integer id, Integer userId);

    @Transactional
    @Modifying
    @Query(value = "Delete from Transaction t where t.id=?1 and t.appUser.id=?2")
    void deleteByIdAndUserId(Integer id, Integer userId);
}
