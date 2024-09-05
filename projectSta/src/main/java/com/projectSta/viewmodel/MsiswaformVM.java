package com.projectSta.viewmodel;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.projectSta.dao.MsiswaDAO;
import com.projectSta.dao.MkelasDAO;
import com.projectSta.domain.Msiswa;
import com.projectSta.domain.Mkelas;
import com.projectSta.utils.db.StoreHibernateUtil;

public class MsiswaformVM {
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();

	private Session session;
	private Transaction transaction;
	private Msiswa obj;
	private boolean isInsert;

	private MsiswaDAO oMserDAO = new MsiswaDAO();

	@Wire
	private Window winUserform;
	@Wire
	private Button btnSave, btnCancel;
	@Wire
	private Combobox cbUsergroup;
	@Wire
	private Div divFooter;

	@Wire
	private Row rownisn, rownama, rowkelamin;
	@Wire
	private Textbox tbNisn, tbNama;
	
	@Wire
	private Radiogroup RgKelamin;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("isEdit") final String isEdit, 
			@ExecutionArgParam("isDetail") final String isDetail,
			@ExecutionArgParam("obj") Msiswa obj) {
		Selectors.wireComponents(view, this, false);
		doReset();

		if (obj != null) {
			this.obj = obj;
			isInsert = false;
			cbUsergroup.setValue(obj.getMkelasfk().getNamakelas());
			btnSave.setDisabled(false);
			btnCancel.setVisible(false);
			} 
			if (isEdit != null && isEdit.equals("Y")) {
				btnSave.setLabel(Labels.getLabel("common.update"));
				tbNisn.setDisabled(true);
			}
		

		if (isDetail != null && isDetail.equals("Y")) {

			tbNisn.setReadonly(true);
			tbNama.setReadonly(true);
			RgKelamin.setDisabled(true);
			cbUsergroup.setReadonly(true);
			cbUsergroup.setButtonVisible(false);
			cbUsergroup.setCols(35);


			if (obj.getCreatedtime() == null)
				obj.setCreatedtime(null);

			if (obj.getCreatedby() == null || obj.getCreatedby().equals(""))
				obj.setCreatedby(new String());

			divFooter.setVisible(false);
			winUserform.setStyle("border-radius: 7px; background-color: #2F3061;");
		}
	}

	@Command
	@NotifyChange("*")
	public void doSave() {
	     session = null;
	     transaction = null;
	    try {
	        Msiswa data = null;
	        if (isInsert) {
	            data = oMserDAO.findByFilter("nisn = '" + obj.getNisn().trim() + "'");
	        }

	        if (data != null) {
	            Messagebox.show("Gagal menambahkan nisn, nisn '" + obj.getNisn().trim() + "' telah digunakan.",
	                    "Peringatan", Messagebox.OK, Messagebox.EXCLAMATION);
	        } else {
	            try {
	                Msiswa Mser = (Msiswa) zkSession.getAttribute("Muser");
	                if (Mser == null) {
	                    Mser = new Msiswa();
	                }

	                session = StoreHibernateUtil.openSession();
	                transaction = session.beginTransaction();
	                
	                if (isInsert) {
	                    obj.setNisn(obj.getNisn());
	                    obj.setCreatedby(Mser.getNisn());
	                    obj.setCreatedtime(new Date());

	                    Clients.evalJavaScript("swal.fire({" 
	                                           + "icon: 'success',\r\n" 
	                                           + "  title: 'Berhasil!',\r\n"
	                                           + " text: 'Siswa " + obj.getNisn().toUpperCase().trim() 
	                                           + " Berhasil Ditambahkan!'," + "})");
	                } else {
	                    Clients.evalJavaScript("swal.fire({"
	                                           + "icon: 'success',\r\n"
	                                           + "  title: 'Berhasil!',\r\n"
	                                           + " text: 'Siswa " + obj.getNamasiswa().trim() 
	                                           + " Berhasil Diperbarui!'," + "})");
	                }

	                oMserDAO.save(session, obj);
	                transaction.commit();

	                doReset();
	                doClose();
	            } catch (HibernateException e) {
	                if (transaction != null) {
	                    transaction.rollback();
	                }
	                if (isInsert) {
	                    obj.setMsiswapk(null);
	                }
	                Messagebox.show("Error : " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
	                e.printStackTrace();
	            } catch (Exception e) {
	                if (transaction != null) {
	                    transaction.rollback();
	                }
	                if (isInsert) {
	                    obj.setMsiswapk(null);
	                }
	                Messagebox.show("Error : " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
	                e.printStackTrace();
	            } finally {
	                if (session != null) {
	                    session.close();
	                }
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}



	@Command
	@NotifyChange("*")
	public void doReset() {
		winUserform.setStyle("border-radius: 7px; "
							+ "background-color: #3bc1e2;");
		obj = new Msiswa();
		isInsert = true;
		cbUsergroup.setValue(null);
	}

	public void doClose() {
		Event closeEvent = new Event("onClose", winUserform, obj);
		Events.postEvent(closeEvent);
	}
	
	public ListModelList<Mkelas> getUserGrupmodel() {
		ListModelList<Mkelas> lm = null;
		try {
			lm = new ListModelList<Mkelas>(new MkelasDAO().listByFilter("0=0", "namakelas"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lm;
	}

	public Validator getValidator() {
	    return new AbstractValidator() {
	        @Override
	        public void validate(ValidationContext ctx) {
	            String nisn = (String) ctx.getProperties("nisn")[0].getValue();
	            String namasiswa = (String) ctx.getProperties("namasiswa")[0].getValue();
	            String jeniskelamin = (String) ctx.getProperties("jeniskelamin")[0].getValue();
	            Mkelas mkelasfk = (Mkelas) ctx.getProperties("mkelasfk")[0].getValue();

	            if (nisn == null || "".equals(nisn.trim()))
	                this.addInvalidMessage(ctx, "nisn", Labels.getLabel("common.validator.empty"));
	            
	            if (namasiswa == null || "".equals(namasiswa.trim()))
	                this.addInvalidMessage(ctx, "namasiswa", Labels.getLabel("common.validator.empty"));
	            
	            if (jeniskelamin == null || "".equals(jeniskelamin.trim())) 
	                this.addInvalidMessage(ctx, "jeniskelamin", Labels.getLabel("common.validator.empty"));
	            
	            if (mkelasfk == null)
	                this.addInvalidMessage(ctx, "mkelasfk", Labels.getLabel("common.validator.empty"));
	        }
	    };
	}


	public Msiswa getObj() {
		return obj;
	}

	public void setObj(Msiswa obj) {
		this.obj = obj;
	}

	
	
	
}