package com.projectSta.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import com.projectSta.domain.Msiswa;
import com.projectSta.utils.db.StoreHibernateUtil;

public class MsiswaDAO {
	Session session;

	public Msiswa login(String loginid) throws HibernateException, Exception {
		session = StoreHibernateUtil.openSession();
		Msiswa oForm = null;
		if (loginid != null)
			loginid = loginid.trim().toUpperCase();
		oForm = (Msiswa) session.createQuery("from Msiswa where nisn =  '" + loginid + "'").uniqueResult();
		session.close();
		return oForm;
	}

	@SuppressWarnings("unchecked")
	public List<Msiswa> listPaging(int first, int second, String filter, String orderby) throws Exception {
		List<Msiswa> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session.createSQLQuery("select * from Msiswa where " + filter + " order by " + orderby + " offset "
				+ first + " rows fetch next " + second + " rows only").addEntity(Msiswa.class).list();
		session.close();
		return oList;
	}

	public int pageCount(String filter) throws Exception {
		int count = 0;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		count = Integer.parseInt((String) session.createSQLQuery("select count(*) from Msiswa where " + filter)
				.uniqueResult().toString());
		session.close();
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<Msiswa> listByFilter(String filter, String orderby) throws Exception {
		List<Msiswa> oList = null;
		if (filter == null || "".equals(filter))
			filter = "0 = 0";
		session = StoreHibernateUtil.openSession();
		oList = session.createQuery("from Msiswa where " + filter + " order by " + orderby).list();
		session.close();
		return oList;
	}

	public Msiswa findByPk(Integer pk) throws Exception {
		session = StoreHibernateUtil.openSession();
		Msiswa oForm = (Msiswa) session.createQuery("from Msiswa where Msiswapk = " + pk).uniqueResult();
		session.close();
		return oForm;
	}

	public Msiswa findByUserid(String nisn) throws Exception {
		Msiswa oForm = null;
		session = StoreHibernateUtil.openSession();
		oForm = (Msiswa) session.createQuery("from Msiswa where nisn = '" + nisn + "'").uniqueResult();
		session.close();
		return oForm;
	}

	public Msiswa findByFilter(String filter) throws Exception {
		session = StoreHibernateUtil.openSession();
		Msiswa oForm = (Msiswa) session.createQuery("from Msiswa where " + filter).uniqueResult();
		session.close();
		return oForm;
	}

	@SuppressWarnings("rawtypes")
	public List listStr(String fieldname) throws Exception {
		List oList = new ArrayList();
		session = StoreHibernateUtil.openSession();
		oList = session.createQuery("select " + fieldname + " from Msiswa order by " + fieldname).list();
		session.close();
		return oList;
	}

	@SuppressWarnings("unchecked")
	public List<Msiswa> listAll() {
		List<Msiswa> objList = null;
		session = StoreHibernateUtil.openSession();
		objList = session.createQuery("from Msiswa").list();
		session.close();
		return objList;
	}

	public void save(Session session, Msiswa oForm) throws HibernateException, Exception {
		session.saveOrUpdate(oForm);
	}

	public void delete(Session session, Msiswa oForm) throws HibernateException, Exception {
		session.delete(oForm);
	}


	@SuppressWarnings("unchecked")
	public List<Msiswa> listBymkelasfk(Integer mkelasfk) throws Exception {
		List<Msiswa> oList = null;
		session = StoreHibernateUtil.openSession();
		oList = session.createNativeQuery("SELECT * FROM Msiswa WHERE MKELASPK = " + mkelasfk)
				.addEntity(Msiswa.class).list();
		session.close();
		return oList;
	}
}