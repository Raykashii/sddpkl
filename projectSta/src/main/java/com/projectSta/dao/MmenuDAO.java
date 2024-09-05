package com.projectSta.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.projectSta.domain.Mmenu;
import com.projectSta.utils.db.StoreHibernateUtil;

public class MmenuDAO {
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Mmenu> listPaging(int first, int second, String filter, String orderby) throws Exception {
		List<Mmenu> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("select * from Mmenu " + "where " + filter + " order by " + orderby + " limit "
				+ second + " offset " + first).addEntity(Mmenu.class).list();

		session.close();
		return oList;
	}

	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Mmenu " + "where " + filter)
				.uniqueResult().toString());
		session.close();
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<Mmenu> listByFilter(String filter, String orderby) throws Exception {
		List<Mmenu> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Mmenu where " + filter + " order by " + orderby).list();
		session.close();
		return oList;
	}

	public Mmenu findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Mmenu oForm = (Mmenu) session.createQuery("from Mmenu where Mmenupk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}

	public Mmenu findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Mmenu oForm = (Mmenu) session.createQuery("from Mmenu where " + filter).uniqueResult();
		session.close();
		return oForm;
	}

	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
		session = StoreHibernateUtil.openSession();
		oList = session.createQuery("select " + fieldname + " from Mmenu order by " + fieldname).list();
		session.close();
		return oList;
	}

	public void save(Session session, Mmenu oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}

	public void delete(Session session, Mmenu oForm) throws HibernateException, Exception {
		session.delete(oForm);
	}

	@SuppressWarnings("unchecked")
	public List<Mmenu> startsWith(int maxrow, String value) {
		List<Mmenu> oList = new ArrayList<Mmenu>();
		session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery(
				"select top " + maxrow + " * from Mmenu where upper(menuname) like '%" + value + "%' order by menuname")
				.addEntity(Mmenu.class).list();
		session.close();
		return oList;
	}
}