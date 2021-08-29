package personal.nphuc96.money_tracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.nphuc96.money_tracker.entity.TransactionGroup;

@Repository
public interface TransactionGroupDAO extends JpaRepository<TransactionGroup, Integer> {


}
