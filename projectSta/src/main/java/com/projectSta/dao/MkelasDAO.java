package com.projectSta.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.projectSta.domain.Mkelas;
import com.projectSta.utils.db.StoreHibernateUtil;

public class MkelasDAO {
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Mkelas> listPaging(int first, int second, String filter, String orderby) throws Exception {
		List<Mkelas> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("select * from Mkelas where " + filter + " order by " + orderby + " offset "
				+ first + " rows fetch next " + second + " rows only").addEntity(Mkelas.class).list();
		session.close();
		return oList;
	}

	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Mkelas where " + filter)
				.uniqueResult().toString());
		session.close();
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<Mkelas> listByFilter(String filter, String orderby) throws Exception {
		List<Mkelas> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Mkelas where " + filter + " order by " + orderby).list();
		session.close();
		return oList;
	}

	public Mkelas findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Mkelas oForm = (Mkelas) session.createQuery("from Mkelas where MKELASPK = " + pk)
				.uniqueResult();
		session.close();
		return oForm;
	}

	public Mkelas findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Mkelas oForm = (Mkelas) session.createQuery("from Mkelas where " + filter).uniqueResult();
		session.close();
		return oForm;
	}

	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
		session = StoreHibernateUtil.openSession();
		oList = session.createQuery("select " + fieldname + " from Mkelas order by " + fieldname).list();
		session.close();
		return oList;
	}

	public void save(Session session, Mkelas oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}

	public void delete(Session session, Mkelas oForm) throws HibernateException, Exception {
		session.delete(oForm);
	}
}