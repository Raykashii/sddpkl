package com.projectSta.viewmodel;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import com.projectSta.dao.MuserDAO;
import com.projectSta.domain.Muser;
import com.projectSta.services.LoggingService;
import com.projectSta.utils.SysUtils;
import com.projectSta.utils.db.StoreHibernateUtil;

public class AuthLogin {

	private String userid;
	private String password;	
	private String lblMessage;
	@SuppressWarnings("unused")
	private Logger logger = LoggingService.getLog(AuthLogin.class);
	private Session session;
	private Transaction transaction;

	@SuppressWarnings("unused")
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
		Selectors.wireComponents(view, this, false);


		long heapSize = Runtime.getRuntime().totalMemory();
		long heapMaxSize = Runtime.getRuntime().maxMemory();
		long heapFreeSize = Runtime.getRuntime().freeMemory();

	}

	@Command
	@NotifyChange("*")
	public void doLogin() {
		try {
			if (userid != null && !userid.trim().equals("") && password != null && !password.trim().equals("")) {
				Muser oForm = new MuserDAO().login(userid);
				if (oForm != null) {
					if (password != null)
						password = password.trim();
					String passencrypted = SysUtils.encryptionCommand(password);
					if (oForm.getPassword().equals(passencrypted)) {
						Sessions.getCurrent().setAttribute("oUser", oForm);
						Executions.sendRedirect("view/index.zul");

						Muser user = new MuserDAO().findByUserid(userid);
						if (user != null) {
							session = StoreHibernateUtil.openSession();
							transaction = session.beginTransaction();
							user.setIslogin(1);
							new MuserDAO().save(session, user);
							transaction.commit();
							session.close();
						}
					} else {
						Clients.evalJavaScript("swal.fire({" + "icon: 'error',\r\n" + "  title: 'Failed!',\r\n"
								+ " text: 'Invalid Password!'," + "})");
					}

				} else {
					Clients.evalJavaScript("swal.fire({" + "icon: 'error',\r\n" + "  title: 'Failed!',\r\n"
							+ " text: 'Invalid Userid!'," + "})");
				}
			}
		} catch (Exception e) {
			lblMessage = e.getMessage();
			e.printStackTrace();

		}
	}

	@Command
	public void doForgetPassword() {
		Window win = (Window) Executions.createComponents("/forgetpassword.zul", null, null);
		win.setClosable(true);
		win.doModal();
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLblMessage() {
		return lblMessage;
	}

	public void setLblMessage(String lblMessage) {
		this.lblMessage = lblMessage;
	}

}
