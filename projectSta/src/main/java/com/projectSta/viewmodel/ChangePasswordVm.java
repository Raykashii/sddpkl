package com.projectSta.viewmodel;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.projectSta.dao.MuserDAO;
import com.projectSta.domain.Muser;
import com.projectSta.model.ChangepasswordModel;
import com.projectSta.utils.SysUtils;

public class ChangePasswordVm {

	private String userid;
	private MuserDAO muserDao = new MuserDAO();
	private ChangepasswordModel changepassModel = new ChangepasswordModel();
	private Session zkSession = Sessions.getCurrent();
	

	@Wire
	private Window win;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("isDetail") final String isDetail, @ExecutionArgParam("userid") String userid) {
		Selectors.wireComponents(view, this, false);
		if (userid != null)
			this.userid = userid;
	}

	@Command
	public void doSave() {
		try {
			Muser muser = muserDao.login(userid);
			if (SysUtils.encryptionCommand(changepassModel.getCurrentpass()).equals(muser.getPassword())) {
				if (changepassModel.getNewpass().equals(changepassModel.getNewpassconfirm())) {
					Messagebox.show(
							"After change password you will be redirect to login page again, are you sure to change password?",
							"Change Password", Messagebox.OK | Messagebox.NO, Messagebox.QUESTION,
							new EventListener<Event>() {
								@Override
								public void onEvent(Event event) throws Exception {
									if (event.getName().equalsIgnoreCase(Events.ON_OK)) {
										muserDao.changepw(userid,
												SysUtils.encryptionCommand(changepassModel.getNewpassconfirm()));
										if (zkSession.getAttribute("oUser") != null) {
											zkSession.removeAttribute("oUser");
										}
										doClose();
										Executions.sendRedirect("/index.zul");
									}
								}
							});

				} else
					Clients.showNotification("Password Confirmation Not Match", "warning", null, "middle_center", 3000);
			} else {
				Clients.showNotification("Current Passoword is Invalid", "warning", null, "middle_center", 3000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Validator getValidator() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				String currentpass = (String) ctx.getProperties("currentpass")[0].getValue();
				String newpass = (String) ctx.getProperties("newpass")[0].getValue();
				String newpassconfirm = (String) ctx.getProperties("newpassconfirm")[0].getValue();

				if (currentpass == null || "".equals(currentpass.trim()))
					this.addInvalidMessage(ctx, "currentpass", Labels.getLabel("common.validator.empty"));
				if (newpass == null || "".equals(newpass.trim()))
					this.addInvalidMessage(ctx, "newpass", Labels.getLabel("common.validator.empty"));
				if (newpassconfirm == null || "".equals(newpassconfirm.trim()))
					this.addInvalidMessage(ctx, "newpassconfirm", Labels.getLabel("common.validator.empty"));
				
			}

			
		};

	}

	public void doClose() {
		Event closeEvent = new Event("onClose", win, null);
		Events.postEvent(closeEvent);
	}

	public ChangepasswordModel getChangepassModel() {
		return changepassModel;
	}

	public void setChangepassModel(ChangepasswordModel changepassModel) {
		this.changepassModel = changepassModel;
	}

	

}
