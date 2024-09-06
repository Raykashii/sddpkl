package com.projectSta.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.projectSta.domain.Memployee;
import com.projectSta.domain.Mtletter;
import com.projectSta.utils.db.StoreHibernateUtil;

public class MtletterDAO {
	
	public List<Memployee> getAllEmployees() {
        Session session = StoreHibernateUtil.openSession();
        try {
            Query<Memployee> query = session.createQuery("from Memployee", Memployee.class);
            return query.list();
        } finally {
            session.close();
        }
    }

    // Method untuk menyimpan atau memperbarui data
    public void save(Session session, Mtletter mtletter) throws Exception {
        Transaction trx = session.beginTransaction();
        try {
            session.saveOrUpdate(mtletter);
            trx.commit();
        } catch (Exception e) {
            if (trx != null) trx.rollback();
            throw e;
        }
    }

    // Method untuk menghapus data berdasarkan objek Mtletter
    public void delete(Session session, Mtletter mtletter) throws Exception {
        Transaction trx = session.beginTransaction();
        try {
            session.delete(mtletter);
            trx.commit();
        } catch (Exception e) {
            if (trx != null) trx.rollback();
            throw e;
        }
    }

    // Method untuk mencari semua data Mtletter
    public List<Mtletter> findAll() throws Exception {
        Session session = StoreHibernateUtil.openSession();
        try {
            Query<Mtletter> query = session.createNamedQuery("Mtletter.findAll", Mtletter.class);
            return query.getResultList();
        } finally {
            session.close();
        }
    }

    // Method untuk mencari Mtletter berdasarkan primary key
    public Mtletter findById(Session session, Integer tletterpk) throws Exception {
        return session.get(Mtletter.class, tletterpk);
    }

    // Method untuk mencari data berdasarkan filter tertentu (misalnya memployeefk atau tujuan)
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

    // Method untuk menghitung jumlah data Mtletter
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
