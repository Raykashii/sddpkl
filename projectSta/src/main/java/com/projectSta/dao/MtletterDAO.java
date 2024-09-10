package com.projectSta.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import com.projectSta.domain.Mtletter;
import com.projectSta.utils.db.StoreHibernateUtil;

public class MtletterDAO {
	Session session;

	public Mtletter login(String loginid) throws HibernateException, Exception {
		session = StoreHibernateUtil.openSession();
		Mtletter oForm = null;
		if (loginid != null)
			loginid = loginid.trim().toUpperCase();
		oForm = (Mtletter) session.createQuery("from Tletter where nisn =  '" + loginid + "'").uniqueResult();
		session.close();
		return oForm;
	}

	@SuppressWarnings("unchecked")
	public List<Mtletter> listPaging(int first, int second, String filter, String orderby) throws Exception {
		List<Mtletter> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("select * from Tletter where " + filter + " order by " + orderby + " offset "
				+ first + " rows fetch next " + second + " rows only").addEntity(Mtletter.class).list();
		session.close();
		return oList;
	}

	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Tletter where " + filter)
				.uniqueResult().toString());
		session.close();
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<Mtletter> listByFilter(String filter, String orderby) throws Exception {
		List<Mtletter> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Tletter where " + filter + " order by " + orderby).list();
		session.close();
		return oList;
	}

	public Mtletter findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Mtletter oForm = (Mtletter) session.createQuery("from Tletter where Msiswapk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}

	public Mtletter findByUserid(String nisn) throws Exception {
		Mtletter oForm = null;
		session = StoreHibernateUtil.openSession();
		oForm = (Mtletter) session.createQuery("from Tletter where nisn = '" + nisn + "'").uniqueResult();
		session.close();
		return oForm;
	}

	public Mtletter findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Mtletter oForm = (Mtletter) session.createQuery("from Tletter where " + filter).uniqueResult();
		session.close();
		return oForm;
	}

	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
		session = StoreHibernateUtil.openSession();
		oList = session.createQuery("select " + fieldname + " from Tletter order by " + fieldname).list();
		session.close();
		return oList;
	}

	@SuppressWarnings("unchecked")
	public List<Mtletter> listAll() {
		List<Mtletter> objList = null;
		session = StoreHibernateUtil.openSession();
		objList = session.createQuery("from Tletter").list();
		session.close();
		return objList;
	}

	public void save(Session session, Mtletter oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}

	public void delete(Session session, Mtletter oForm) throws HibernateException, Exception {
		session.delete(oForm);
	}


	@SuppressWarnings("unchecked")
	public List<Mtletter> listBymkelasfk(Integer mkelasfk) throws Exception {
		List<Mtletter> oList = null;
		session = StoreHibernateUtil.openSession();
		oList = session.createNativeQuery("SELECT * FROM Tletter WHERE MEMPLOYEEFK = " + mkelasfk)
				.addEntity(Mtletter.class).list();
		session.close();
		return oList;
	}
}