package personal.nphuc96.money_tracker.dao.transaction.service;

import org.springframework.stereotype.Service;
import personal.nphuc96.money_tracker.dao.transaction.CustomTransactionDAO;
import personal.nphuc96.money_tracker.entity.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;


public class CustomTransactionDAOImpl implements CustomTransactionDAO {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Transaction> findAmountByDate(LocalDate from, LocalDate to, Integer userId) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Transaction> query = builder.createQuery(Transaction.class);
        Root<Transaction> root = query.from(Transaction.class);
        Predicate predicateForUserId = builder.equal(root.get("appUser").get("id"), userId);
        Predicate fromDate = builder.greaterThanOrEqualTo(root.get("onDate"), from);
        Predicate toDate = builder.lessThanOrEqualTo(root.get("onDate"), to);
        Predicate finalPredicateForDate = builder.and(fromDate, toDate, predicateForUserId);
        query.multiselect(root.get("id"),root.get("amount")).where(finalPredicateForDate);
        return em.createQuery(query).getResultList();
    }


}
