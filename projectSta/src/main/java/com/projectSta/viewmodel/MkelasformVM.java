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

import com.projectSta.dao.MkelasDAO;
import com.projectSta.domain.Mkelas;


import com.projectSta.utils.db.StoreHibernateUtil;

public class MkelasformVM {
	org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	
	private Session session;
	private Transaction transaction;
	private Mkelas obj;
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
	private Textbox tbCodekelasSiswa,tbNamaKelasSiswa, tbDescKelasSiswa;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, 
			@ExecutionArgParam("isEdit") final String isEdit, @ExecutionArgParam("isDetail") final String isDetail,
			@ExecutionArgParam("obj") Mkelas obj) throws Exception {
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
			tbCodekelasSiswa.setReadonly(true);
			tbNamaKelasSiswa.setReadonly(true);
			tbDescKelasSiswa.setReadonly(true);
			
			divFooter.setVisible(false);
			
		
		
			if (gridMenu != null) {
		
			}
		}
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
	                            Mkelas existingKelas = new MkelasDAO().findByFilter(
	                                "kodekelas = '" + obj.getKodekelas().trim() + "'");
	                            if (existingKelas != null) {
	                                Messagebox.show(
	                                    "Gagal menambahkan kelas, kelas kode'" + obj.getKodekelas().trim() + "' sudah digunakan.",
	                                    "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
	                                return;
	                            }
	                        }

	                        session = StoreHibernateUtil.openSession();
	                        transaction = session.beginTransaction();
	                        
	                        if (isInsert) {
	                            obj.setCreatedtime(new Date());
	                        }

	                        new MkelasDAO().save(session, obj);
	                        transaction.commit();

	                        String message = isInsert ? "Class created successfully!" : "Class updated successfully!";
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
		  obj = new Mkelas();
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
				String kodekelas = (String) ctx.getProperties("kodekelas")[0].getValue();
				String namakelas = (String) ctx.getProperties("namakelas")[0].getValue();
				
				if (kodekelas == null || "".equals(kodekelas.trim()))
					this.addInvalidMessage(ctx, "kodekelas", Labels.getLabel("common.validator.empty"));
				if (namakelas == null || "".equals(namakelas.trim()))
					this.addInvalidMessage(ctx, "namakelas", Labels.getLabel("common.validator.empty"));
		
			}
		};
	}

	public Mkelas getObj() {
		return obj;
	}

	public void setObj(Mkelas obj) {
		this.obj = obj;
	}


	
}
	
