package personal.phuc.expense.dao.transaction.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import personal.phuc.expense.dao.transaction.CustomTransactionDAO;
import personal.phuc.expense.entity.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;

@Service
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
        query.multiselect(root.get("id"), root.get("amount")).where(finalPredicateForDate);
        return em.createQuery(query).getResultList();
    }

    @Override
    public Page<Transaction> findTransactions(Integer userId, String sortBy, Pageable pageable) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Transaction> query = builder.createQuery(Transaction.class);
        Root<Transaction> root = query.from(Transaction.class);
        Predicate predicateForUserId = builder.equal(root.get("appUser").get("id"), userId);
        setOrder(sortBy, builder, query, root);
        List<Transaction> result = transactionList(query, pageable);
        query.select(root).where(predicateForUserId);
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        countQuery.select(builder.count(countQuery.from(Transaction.class)));
        Long count = em.createQuery(countQuery).getSingleResult();
        return new PageImpl<>(result, pageable, count);
    }

    private void setOrder(String sortBy, CriteriaBuilder builder,
                          CriteriaQuery<Transaction> query,
                          Root<Transaction> root) {
        if (sortBy.equals("id")) {
            query.orderBy(builder.desc(root.get("id")));
        } else query.orderBy(builder.desc(root.get("onDate")));
    }

    private List<Transaction> transactionList(CriteriaQuery<Transaction> query, Pageable pageable) {
        return em.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }
}
