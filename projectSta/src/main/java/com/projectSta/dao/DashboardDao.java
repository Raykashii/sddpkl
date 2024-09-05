package com.projectSta.dao;

import java.util.List;

import org.hibernate.Session;

import com.projectSta.domain.MPdpkproduk;
import com.projectSta.model.Mtable;
import com.projectSta.model.VDataTotal;
import com.projectSta.model.V_AllDpk;
import com.projectSta.model.V_Dpk;
import com.projectSta.model.V_SumDPK;
import com.projectSta.utils.db.StoreHibernateUtil;

public class DashboardDao {

	private Session session;

	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<V_Dpk> getProductDpk(String tablename) throws Exception {
		session = StoreHibernateUtil.getSessionFactory().openSession();
		List<V_Dpk> oList = null;
		try {
			 oList = session.createSQLQuery(
					"SELECT M.KETERANGAN, CASE WHEN SUM(TOTCURRENTLY) !=0 THEN SUM(TOTCURRENTLY) ELSE 0 END AS NOMINAL\r\n"
							+ "FROM " + tablename + " JOIN MPDPKPRODUK M on " + tablename + ".KETERANGAN = M.KETERANGAN\r\n"
							+ "GROUP BY M.KETERANGAN")
					.addEntity(V_Dpk.class).list();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			session.close();	
		}
		
		return oList;
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<V_Dpk> getSumProductDpk(String bulan) throws Exception {
		session = StoreHibernateUtil.getSessionFactory().openSession();
		List<V_Dpk> oList = null;
		try {
			 oList = session.createSQLQuery(
					"SELECT M.KETERANGAN, NOMINAL FROM TSUMMARYDPK JOIN MPDPKPRODUK M on TSUMMARYDPK.KETERANGAN = M.KETERANGAN\r\n" + 
					"WHERE BULAN = '"+bulan+"'")
					.addEntity(V_Dpk.class).list();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			session.close();	
		}
		
		return oList;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<V_Dpk> getProductKreditBankNonBank(String tablename) throws Exception {

		session = StoreHibernateUtil.getSessionFactory().openSession();
		List<V_Dpk> oList = session.createSQLQuery("SELECT M.KETERANGAN, SUM(TOTCURRENTLY) AS NOMINAL FROM " + tablename
				+ " JOIN\r\n" + "    MPKATEGORIPORTOFOLIO M on " + tablename
				+ ".KATEGORIPORTOFOLIO = M.SANDIREFERENSI\r\n" + "GROUP BY M.KETERANGAN;").addEntity(V_Dpk.class)
				.list();
		session.close();
		return oList;
	}

	public Mtable getTable(String tableName) throws Exception {
		session = StoreHibernateUtil.getSessionFactory().openSession();
		Mtable oForm = (Mtable) session
				.createSQLQuery(
						"select table_name from information_schema.tables where table_name = '" + tableName + "'")
				.addEntity(Mtable.class).uniqueResult();
		session.close();
		return oForm;
	}

	@SuppressWarnings("unchecked")
	public List<V_AllDpk> getProductDpkAll(String month, String table) throws Exception {
		session = StoreHibernateUtil.getSessionFactory().openSession();
		List<V_AllDpk> oForm = session.createSQLQuery("SELECT '" + month
				+ "' AS BULAN,M.KETERANGAN, CASE WHEN SUM(TOTCURRENTLY) !=0 THEN SUM(TOTCURRENTLY) ELSE 0 END AS NOMINAL\r\n"
				+ "FROM " + table + " JOIN MPDPKPRODUK M on " + table + ".KETERANGAN = M.KETERANGAN\r\n"
				+ "GROUP BY M.KETERANGAN").addEntity(V_AllDpk.class).list();
		session.close();
		return oForm;
	}
	
	@SuppressWarnings("unchecked")
	public List<V_AllDpk> getSumProductDpkAll(String month) throws Exception {
		session = StoreHibernateUtil.getSessionFactory().openSession();
		List<V_AllDpk> oForm = session.createSQLQuery("SELECT M.KETERANGAN, BULAN, NOMINAL FROM TSUMMARYDPK RIGHT JOIN MPDPKPRODUK M on TSUMMARYDPK.KETERANGAN = M.KETERANGAN\r\n" + 
				" WHERE BULAN='"+month+"';").addEntity(V_AllDpk.class).list();
		session.close();
		return oForm;
	}
	


	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<MPdpkproduk> getDpkProduct() throws Exception {
		session = StoreHibernateUtil.getSessionFactory().openSession();
		List<MPdpkproduk> oForm = session.createSQLQuery("SELECT * FROM MPDPKPRODUK").addEntity(MPdpkproduk.class)
				.list();
		session.close();
		return oForm;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<VDataTotal> getKreditKepadaBankNonBank(String tablename, String filter) throws Exception {

		session = StoreHibernateUtil.getSessionFactory().openSession();
		List<VDataTotal> oList = session
				.createSQLQuery(
						"SELECT CASE WHEN SUM(TOTCURRENTLY) !=0 THEN SUM(TOTCURRENTLY) ELSE 0 END AS NOMINAL FROM "
								+ tablename + " JOIN\r\n" + "    MPKATEGORIPORTOFOLIO M on " + tablename
								+ ".KATEGORIPORTOFOLIO = M.SANDIREFERENSI\r\n" + "WHERE M.KETERANGAN='" + filter + "'")
				.addEntity(VDataTotal.class).list();
		session.close();
		return oList;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<VDataTotal> getProductDataPerMonth(String tablename) throws Exception {

		session = StoreHibernateUtil.getSessionFactory().openSession();
		List<VDataTotal> oList = session.createSQLQuery(
				"SELECT CASE WHEN SUM(TOTCURRENTLY) !=0 THEN SUM(TOTCURRENTLY) ELSE 0 END AS NOMINAL\r\n" + "FROM "
						+ tablename + "")
				.addEntity(VDataTotal.class).list();
		session.close();
		return oList;
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<VDataTotal> getSumAlldpk(String bulan) throws Exception {

		session = StoreHibernateUtil.getSessionFactory().openSession();
		List<VDataTotal> oList = session.createSQLQuery(
				"SELECT SUM(NOMINAL) AS NOMINAL FROM TSUMMARYDPK WHERE BULAN = '"+bulan+"' GROUP BY BULAN")
				.addEntity(VDataTotal.class).list();
		session.close();
		return oList;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<V_AllDpk> getProductDpkYOY(String table1, String table2, Integer year1, Integer year2) throws Exception {

		session = StoreHibernateUtil.getSessionFactory().openSession();
		List<V_AllDpk> oList = session.createSQLQuery(
				"SELECT '"+year1+"' AS BULAN,M.KETERANGAN, CASE WHEN SUM(TOTCURRENTLY) !=0 THEN SUM(TOTCURRENTLY) ELSE 0 END AS NOMINAL\r\n"
						+ "FROM "+table1+" JOIN MPDPKPRODUK M on "+table1+".KETERANGAN = M.KETERANGAN\r\n"
						+ "GROUP BY M.KETERANGAN\r\n" + "UNION\r\n"
						+ "SELECT '"+year2+"' AS BULAN,M.KETERANGAN, CASE WHEN SUM(TOTCURRENTLY) !=0 THEN SUM(TOTCURRENTLY) ELSE 0 END AS NOMINAL\r\n"
						+ "FROM "+table2+" JOIN MPDPKPRODUK M on "+table2+".KETERANGAN = M.KETERANGAN\r\n"
						+ "GROUP BY M.KETERANGAN")
				.addEntity(V_AllDpk.class).list();
		session.close();
		return oList;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<V_AllDpk> getProductBankNonBankYoy(String table1, String table2, Integer year3, Integer year4 ) throws Exception {
		session = StoreHibernateUtil.getSessionFactory().openSession();
		List<V_AllDpk> oForm = session
				.createSQLQuery("SELECT M.KETERANGAN, '"+year3+"' AS BULAN, SUM(TOTCURRENTLY) AS NOMINAL\r\n"
						+ "FROM "+table1+"\r\n" + "         JOIN MPKATEGORIPORTOFOLIO M ON\r\n"
						+ "    "+table1+".KATEGORIPORTOFOLIO = M.SANDIREFERENSI\r\n" + "GROUP BY M.KETERANGAN\r\n"
						+ "UNION\r\n" + "SELECT M.KETERANGAN, '"+year4+"' AS TAHUN, SUM(TOTCURRENTLY) AS NOMINAL\r\n"
						+ "FROM "+table2+"\r\n" + "         JOIN MPKATEGORIPORTOFOLIO M ON\r\n"
						+ "    "+table2+".KATEGORIPORTOFOLIO = M.SANDIREFERENSI\r\n" + "GROUP BY M.KETERANGAN ")
				.addEntity(V_AllDpk.class).list();
		session.close();
		return oForm;
	}
	


}
