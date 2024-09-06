package com.projectSta.viewmodel;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.projectSta.dao.MemployeeDAO;
import com.projectSta.dao.MkelasDAO;
import com.projectSta.domain.Memployee;
import com.projectSta.domain.Mkelas;
import com.projectSta.utils.db.StoreHibernateUtil;

public class MemployeeFormVM {
	org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	
	private Session session;
	private Transaction transaction;
	private Memployee obj;
	private boolean isInsert;
	
	@Wire
	private Grid gridMenu, gridSetup;
	
	@Wire
	private Window winUsergroupform;
	
	@Wire
	private Button btnSave, btnCancel;
	

	
	@Wire
	private Div divRoot, divFooter, divListMenu;
	
	@Wire
	private Textbox tbNama,tbJabatan, tbNotelp, tbAlamat;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, 
			@ExecutionArgParam("isEdit") final String isEdit, 
			@ExecutionArgParam("isDetail") final String isDetail,
			@ExecutionArgParam("obj") Memployee obj) throws Exception {
		Selectors.wireComponents(view, this, false);
		doReset();
		
		if (obj != null) {
			this.obj = obj;
			isInsert = false;
		}
		
		if(isEdit != null && isEdit.equals("Y")) {
			btnSave.setLabel(Labels.getLabel("common.update"));
			btnCancel.setVisible(false);
		
		}
		
		if (isDetail != null && isDetail.equals("Y")) {
			tbNama.setReadonly(true);
			tbJabatan.setReadonly(true);
			tbNotelp.setReadonly(true);
			tbAlamat.setReadonly(true);
			
			divFooter.setVisible(false);
			
		
		
			if (gridMenu != null) {
		
			}
		}
	}
	
	
	public ListModelList<Memployee> getEmployeemodel() {
		ListModelList<Memployee> lm = null;
		try {
			lm = new ListModelList<Memployee>(new MemployeeDAO().listByFilter("0=0", "nama"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lm;
	}
	
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Command
	@NotifyChange("*")
	public void doSave() {
	    try {
	        Messagebox.show(
	            isInsert ? "Are you sure you want to save?" : "Are you sure you want to update?",
	            "Confirmation", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
	            event -> {
	                if (event.getName().equals(Messagebox.ON_OK)) {
	                    Session session = null;
	                    Transaction transaction = null;
	                    try {
	                        if (isInsert) {
	                            Memployee existingKelas = new MemployeeDAO().findByFilter(
	                                "nama = '" + obj.getNama().trim() + "'");
	                            if (existingKelas != null) {
	                                Messagebox.show(
	                                    "Gagal menambahkan pegawai, nama'" + obj.getNama().trim() + "' sudah digunakan.",
	                                    "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
	                                return;
	                            }
	                        }

	                        session = StoreHibernateUtil.openSession();
	                        transaction = session.beginTransaction();
	                        
	               

	                        new MemployeeDAO().save(session, obj);
	                        transaction.commit();

	                        String message = isInsert ? "Pegawai Inserted successfully!" : "Pegawai updated successfully!";
	                        Clients.evalJavaScript("swal.fire({icon: 'success', title: 'Success!', text: '" + message + "'});");

	                        doClose(); 
	                    } catch (Exception e) {
	                        if (transaction != null) transaction.rollback();
	                        e.printStackTrace();
	                        Messagebox.show("An error occurred: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
	                    } finally {
	                        if (session != null) session.close();
	                    }
	                }
	            }
	        );
	    } catch (Exception e) {
	        e.printStackTrace();
	        Messagebox.show("An error occurred: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
	    }
	}


							
	@Command
	@NotifyChange("*")
	public void doReset() {
		  winUsergroupform.setStyle("border-radius: 7px; background-color: #130c6e");
		  obj = new Memployee();
		  isInsert = true;
		}
	
	public void doClose() {
		Event closeevent = new Event("onClose", winUsergroupform, obj);
		Events.postEvent(closeevent);
	}
	
	public Validator getValidator() {
		return new AbstractValidator() {
			
			@Override
			public void validate(ValidationContext ctx) {
				String nama = (String) ctx.getProperties("nama")[0].getValue();
				String jabatan = (String) ctx.getProperties("jabatan")[0].getValue();
				String no_telp = (String) ctx.getProperties("no_telp")[0].getValue();
				String alamat = (String) ctx.getProperties("alamat")[0].getValue();
				
				if (nama == null || "".equals(nama.trim()))
					this.addInvalidMessage(ctx, "nama", Labels.getLabel("common.validator.empty"));
				if (jabatan == null || "".equals(jabatan.trim()))
					this.addInvalidMessage(ctx, "jabatan", Labels.getLabel("common.validator.empty"));
				if (no_telp == null || "".equals(no_telp.trim()))
					this.addInvalidMessage(ctx, "no_telp", Labels.getLabel("common.validator.empty"));
				if (alamat == null || "".equals(alamat.trim()))
					this.addInvalidMessage(ctx, "alamat", Labels.getLabel("common.validator.empty"));
		
			}
		};
	}





	public Memployee getObj() {
		return obj;
	}





	public void setObj(Memployee obj) {
		this.obj = obj;
	}



	
}
	
