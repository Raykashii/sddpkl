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
import org.zkoss.zul.Radio;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.projectSta.dao.MuserDAO;
import com.projectSta.dao.MusergroupDAO;
import com.projectSta.domain.Muser;
import com.projectSta.domain.Musergroup;
import com.projectSta.utils.StringUtils;
import com.projectSta.utils.SysUtils;
import com.projectSta.utils.db.StoreHibernateUtil;

public class MuserFormVM {
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();

	private Session session;
	private Transaction transaction;
	private Muser obj;
	private String isuserlogin;
	private boolean isInsert;

	private MuserDAO oUserDAO = new MuserDAO();

	@Wire
	private Window winUserform;
	@Wire
	private Button btnSave, btnCancel;
	@Wire
	private Combobox cbUsergroup;
	@Wire
	private Div divFooter;
	@Wire
	private Radio rbY, rbN;
	@Wire
	private Row rowuserid, rowusername, rowemail, rowislogin, rowlastlogin, rowcreateby, rowcreatetime;
	@Wire
	private Textbox tbUserid, tbUsername, tbEmail, tbLastlog, tbCreator, tbCreatime;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("isEdit") final String isEdit, 
			@ExecutionArgParam("isDetail") final String isDetail,
			@ExecutionArgParam("obj") Muser obj) {
		Selectors.wireComponents(view, this, false);
		doReset();

		if (obj != null) {
			this.obj = obj;
			isInsert = false;
			cbUsergroup.setValue(obj.getMusergroup().getUsergroupname());
			btnSave.setDisabled(false);
			btnCancel.setVisible(false);
			rowislogin.setVisible(false);
			rowlastlogin.setVisible(false);
			rowcreateby.setVisible(false);
			rowcreatetime.setVisible(false);
			if (obj.getIslogin() == 1) {
				rbY.setChecked(true);
			} else {
				rbN.setChecked(true);
			}

			if (isEdit != null && isEdit.equals("Y")) {
				btnSave.setLabel(Labels.getLabel("common.update"));
				tbUserid.setDisabled(true);
			}
		}

		if (isDetail != null && isDetail.equals("Y")) {
			rowislogin.setVisible(true);
			rowlastlogin.setVisible(true);
			rowcreateby.setVisible(true);
			rowcreatetime.setVisible(true);

			tbUserid.setReadonly(true);
			tbUsername.setReadonly(true);
			tbEmail.setReadonly(true);
			cbUsergroup.setReadonly(true);
			cbUsergroup.setButtonVisible(false);
			cbUsergroup.setCols(35);
			tbLastlog.setReadonly(true);
			tbCreator.setReadonly(true);
			tbCreatime.setReadonly(true);
			rbY.setDisabled(true);
			rbN.setDisabled(true);

			if (obj.getLastlogin() == null)
				obj.setLastlogin(null);

			if (obj.getCreatedtime() == null)
				obj.setCreatedtime(null);

			if (obj.getCreatedby() == null || obj.getCreatedby().equals(""))
				obj.setCreatedby(new String());

			if (obj.getIslogin() == 1)
				isuserlogin = "1";
			else
				isuserlogin = "0";

			divFooter.setVisible(false);
			winUserform.setStyle("border-radius: 7px; background-color: #2F3061;");
		}
	}

	@Command
	@NotifyChange("*")
	public void doSave() {
		try {
			Muser data = null;
			if (isInsert)
				data = oUserDAO.findByFilter("userid = '" + obj.getUserid().trim() + "'");

			if (data != null) {
				Messagebox.show("Gagal menambahkan user, user id '" + obj.getUserid().trim() + "' telah digunakan.",
						"Peringatan", Messagebox.OK, Messagebox.EXCLAMATION);
			} else {
				try {
					Muser oUser = (Muser) zkSession.getAttribute("oUser");
					if (oUser == null)
						oUser = new Muser();

					session = StoreHibernateUtil.openSession();
					transaction = session.beginTransaction();
					if (isInsert) {
						obj.setUserid(obj.getUserid());
						obj.setPassword(SysUtils.encryptionCommand("123456"));
						obj.setUpdatedby(oUser.getUserid());
						obj.setLastupdated(new Date());
						obj.setCreatedby(oUser.getUserid());
						obj.setCreatedtime(new Date());
						obj.setIslogin(0);

						Clients.evalJavaScript("swal.fire({" 
											   + "icon: 'success',\r\n" 
											   + "  title: 'Berhasil!',\r\n"
											   + " text: 'User " + obj.getUserid().toUpperCase().trim() 
											   + " Berhasil Ditambahkan!'," + "})");
					} else {
						obj.setUpdatedby(oUser.getUserid());
						obj.setLastupdated(new Date());

						Clients.evalJavaScript("swal.fire({" 
											   + "icon: 'success',\r\n" 
											   + "  title: 'Berhasil!',\r\n"
											   + " text: 'User " + obj.getUserid().trim() 
											   + " Berhasil Diperbarui!'," + "})");
					}
					oUserDAO.save(session, obj);
					transaction.commit();
					session.close();

					doReset();
					doClose();
				} catch (HibernateException e) {
					transaction.rollback();
					if (isInsert)
						obj.setMuserpk(null);
					Messagebox.show("Error : " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
					e.printStackTrace();
				} catch (Exception e) {
					if (isInsert)
						obj.setMuserpk(null);
					Messagebox.show("Error : " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
					e.printStackTrace();
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
							+ "background-color: #e27d3b;");
		obj = new Muser();
		isInsert = true;
		isuserlogin = null;
		cbUsergroup.setValue(null);
		rowislogin.setVisible(false);
		rowlastlogin.setVisible(false);
		rowcreateby.setVisible(false);
		rowcreatetime.setVisible(false);
	}

	public void doClose() {
		Event closeEvent = new Event("onClose", winUserform, obj);
		Events.postEvent(closeEvent);
	}

	public ListModelList<Musergroup> getUserGrupmodel() {
		ListModelList<Musergroup> lm = null;
		try {
			lm = new ListModelList<Musergroup>(new MusergroupDAO().listByFilter("0=0", "usergroupname"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lm;
	}

	public Validator getValidator() {
		return new AbstractValidator() {
			@Override
			public void validate(ValidationContext ctx) {
				String userid = (String) ctx.getProperties("userid")[0].getValue();
				String username = (String) ctx.getProperties("username")[0].getValue();
				String email = (String) ctx.getProperties("email")[0].getValue();
				Musergroup musergroup = (Musergroup) ctx.getProperties("musergroup")[0].getValue();

				if (userid == null || "".equals(userid.trim()))
					this.addInvalidMessage(ctx, "userid", Labels.getLabel("common.validator.empty"));
				
				if (username == null || "".equals(username.trim()))
					this.addInvalidMessage(ctx, "username", Labels.getLabel("common.validator.empty"));
				
				if (email == null || "".equals(email.trim())) 
					this.addInvalidMessage(ctx, "email", Labels.getLabel("common.validator.empty"));
				
				else
					if (!StringUtils.emailValidator(email))
						this.addInvalidMessage(ctx, "email", Labels.getLabel("common.validator.email"));
					if (musergroup == null)
						this.addInvalidMessage(ctx, "musergroup", Labels.getLabel("common.validator.empty"));
			}
		};
	}

	public Muser getObj() {
		return obj;
	}

	public void setObj(Muser obj) {
		this.obj = obj;
	}

	public String getIsuserlogin() {
		return isuserlogin;
	}

	public void setIsuserlogin(String isuserlogin) {
		this.isuserlogin = isuserlogin;
	}
}