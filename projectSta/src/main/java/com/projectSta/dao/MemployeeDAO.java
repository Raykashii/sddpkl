package com.projectSta.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.projectSta.domain.Memployee;
import com.projectSta.utils.db.StoreHibernateUtil;

public class MemployeeDAO {
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Memployee> listPaging(int first, int second, String filter, String orderby) throws Exception {
		List<Memployee> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("select * from Memployee where " + filter + " order by " + orderby + " offset "
				+ first + " rows fetch next " + second + " rows only").addEntity(Memployee.class).list();
		session.close();
		return oList;
	}

	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Memployee where " + filter)
				.uniqueResult().toString());
		session.close();
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<Memployee> listByFilter(String filter, String orderby) throws Exception {
		List<Memployee> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Memployee where " + filter + " order by " + orderby).list();
		session.close();
		return oList;
	}

	public Memployee findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Memployee oForm = (Memployee) session.createQuery("from Memployee where MEMPLOYEEPK = " + pk)
				.uniqueResult();
		session.close();
		return oForm;
	}

	public Memployee findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Memployee oForm = (Memployee) session.createQuery("from Memployee where " + filter).uniqueResult();
		session.close();
		return oForm;
	}

	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
		session = StoreHibernateUtil.openSession();
		oList = session.createQuery("select " + fieldname + " from Memployee order by " + fieldname).list();
		session.close();
		return oList;
	}

	public void save(Session session, Memployee oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}

	public void delete(Session session, Memployee oForm) throws HibernateException, Exception {
		session.delete(oForm);
	}
}