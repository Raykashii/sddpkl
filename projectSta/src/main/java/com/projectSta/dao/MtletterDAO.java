package com.projectSta.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.projectSta.domain.Memployee;
import com.projectSta.domain.Mtletter;
import com.projectSta.utils.db.StoreHibernateUtil;

public class MtletterDAO {
    public void save(Session session, Mtletter mtletter) throws Exception {
        try {
            session.saveOrUpdate(mtletter);
        } catch (Exception e) {
            throw e;
        }
    }

    public void delete(Session session, Mtletter mtletter) throws Exception {
        try {
            session.delete(mtletter);
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Mtletter> findAll() throws Exception {
        Session session = StoreHibernateUtil.openSession();
        try {
            Query<Mtletter> query = session.createNamedQuery("Mtletter.findAll", Mtletter.class);
            return query.getResultList();
        } finally {
            session.close();
        }
    }

    public Mtletter findById(Session session, Integer tletterpk) throws Exception {
        return session.get(Mtletter.class, tletterpk);
    }

    public List<Mtletter> findByFilter(String filter) throws Exception {
        Session session = StoreHibernateUtil.openSession();
        try {
            String hql = "FROM Mtletter WHERE tujuan LIKE :filter OR memployeefk LIKE :filter";
            Query<Mtletter> query = session.createQuery(hql, Mtletter.class);
            query.setParameter("filter", "%" + filter + "%");
            return query.getResultList();
        } finally {
            session.close();
        }
    }

    public Long countAll() throws Exception {
        Session session = StoreHibernateUtil.openSession();
        try {
            String hql = "SELECT count(*) FROM Mtletter";
            Query<Long> query = session.createQuery(hql, Long.class);
            return query.uniqueResult();
        } finally {
            session.close();
        }
    }
}
