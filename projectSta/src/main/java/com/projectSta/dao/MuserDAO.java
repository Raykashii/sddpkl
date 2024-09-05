package com.projectSta.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.projectSta.domain.Muser;
import com.projectSta.utils.db.StoreHibernateUtil;

public class MuserDAO {
	Session session;

	public Muser login(String loginid) throws HibernateException, Exception {
		session = StoreHibernateUtil.openSession();
		Muser oForm = null;
		if (loginid != null)
			loginid = loginid.trim().toUpperCase();
		oForm = (Muser) session.createQuery("from Muser where userid =  '" + loginid + "'").uniqueResult();
		session.close();
		return oForm;
	}

	@SuppressWarnings("unchecked")
	public List<Muser> listPaging(int first, int second, String filter, String orderby) throws Exception {
		List<Muser> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("select * from Muser where " + filter + " order by " + orderby + " offset "
				+ first + " rows fetch next " + second + " rows only").addEntity(Muser.class).list();
		session.close();
		return oList;
	}

	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Muser where " + filter)
				.uniqueResult().toString());
		session.close();
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<Muser> listByFilter(String filter, String orderby) throws Exception {
		List<Muser> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Muser where " + filter + " order by " + orderby).list();
		session.close();
		return oList;
	}

	public Muser findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Muser oForm = (Muser) session.createQuery("from Muser where Muserpk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}

	public Muser findByUserid(String userid) throws Exception {
		Muser oForm = null;
		session = StoreHibernateUtil.openSession();
		oForm = (Muser) session.createQuery("from Muser where userid = '" + userid + "'").uniqueResult();
		session.close();
		return oForm;
	}

	public Muser findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Muser oForm = (Muser) session.createQuery("from Muser where " + filter).uniqueResult();
		session.close();
		return oForm;
	}

	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
		session = StoreHibernateUtil.openSession();
		oList = session.createQuery("select " + fieldname + " from Muser order by " + fieldname).list();
		session.close();
		return oList;
	}

	@SuppressWarnings("unchecked")
	public List<Muser> listAll() {
		List<Muser> objList = null;
		session = StoreHibernateUtil.openSession();
		objList = session.createQuery("from Muser").list();
		session.close();
		return objList;
	}

	public void save(Session session, Muser oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}

	public void delete(Session session, Muser oForm) throws HibernateException, Exception {
		session.delete(oForm);
	}

	public void changepw(String userid, String newpass) throws HibernateException, Exception {
		session = StoreHibernateUtil.openSession();
		Transaction trx = session.beginTransaction();
		session.createNativeQuery("UPDATE MUSER SET PASSWORD = '" + newpass + "' WHERE USERID = '" + userid + "';")
				.executeUpdate();
		trx.commit();
		session.close();
	}

	@SuppressWarnings("unchecked")
	public List<Muser> listByMusergroupfk(Integer musergroupfk) throws Exception {
		List<Muser> oList = null;
		session = StoreHibernateUtil.openSession();
		oList = session.createNativeQuery("SELECT * FROM MUSER WHERE MUSERGROUPFK = " + musergroupfk)
				.addEntity(Muser.class).list();
		session.close();
		return oList;
	}
}