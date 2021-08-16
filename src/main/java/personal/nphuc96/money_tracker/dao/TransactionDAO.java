package personal.nphuc96.money_tracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.nphuc96.money_tracker.entity.Transaction;

@Repository
public interface MoneyEventDAO extends JpaRepository<Transaction, Integer> {
}
