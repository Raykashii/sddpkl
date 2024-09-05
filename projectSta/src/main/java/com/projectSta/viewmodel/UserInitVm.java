package com.projectSta.viewmodel;

import java.lang.annotation.Documented;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.chart.Charts;
import org.zkoss.chart.ChartsClickEvent;
import org.zkoss.chart.ChartsEvent;
import org.zkoss.chart.ChartsEvents;
import org.zkoss.chart.Legend;
import org.zkoss.chart.Theme;
import org.zkoss.chart.Tooltip;
import org.zkoss.chart.YAxis;
import org.zkoss.chart.model.CategoryModel;
import org.zkoss.chart.model.ChartsDataEvent;
import org.zkoss.chart.model.ChartsDataListener;
import org.zkoss.chart.model.DefaultCategoryModel;
import org.zkoss.json.JSONObject;
import org.zkoss.zhtml.Div;
import org.zkoss.zhtml.Isindex;
import org.zkoss.zhtml.Li;
import org.zkoss.zhtml.Ul;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.A;
import org.zkoss.zul.Column;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Html;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Span;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.ChartDataListener;

import com.projectSta.dao.MmenuDAO;
import com.projectSta.dao.MuserDAO;
import com.projectSta.dao.MusergroupmenuDAO;

import com.projectSta.domain.Mkelas;

import com.projectSta.domain.Mmenu;
import com.projectSta.domain.Muser;
import com.projectSta.domain.Musergroupmenu;

import com.projectSta.utils.AppUtils;
import com.projectSta.utils.db.StoreHibernateUtil;

public class UserInitVm {

	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Session session;
	private Transaction transaction;
	private Mmenu mmenu;
	private Muser muser = (Muser) zkSession.getAttribute("oUser");
	private Muser oUser;
	private boolean isSubgroup = false;
	private Integer year;
	private String filterPlan;
	private String filterSum;
	private String filterBranch;
	private String filter;
	private int index;

	@Wire
	private Treechildren root;
	@Wire
	private Div divChartDpk;
	@Wire
	private org.zkoss.zul.Div menuRoot;
	@Wire
	private org.zkoss.zul.Div divContent, divChartReal, divChartBranch;
	@Wire
	private Span namaMenu;
	@Wire
	private Combobox cbMenu, cbYear, cbAmountYear, cbPeriodYear;
	@Wire
	private Grid grid;
	@Wire
	private Column colNO;

	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		oUser = (Muser) zkSession.getAttribute("oUser");

		if (oUser == null) {
			if (zkSession.getAttribute("oUser") != null) {
				zkSession.removeAttribute("oUser");
			}

			Executions.sendRedirect("/timeout.zul");
			Muser user = (Muser) zkSession.getAttribute("oUser");

			Clients.evalJavaScript("showToast1('success','Sesi Login Habis')");
		} else {
			try {
				Executions.createComponents("/view/dashboard/dashboard.zul", divContent, null);
				boolean isOpen = true;
				boolean isSubgroup = true;

				org.zkoss.zul.Div divMenuGrup = new org.zkoss.zul.Div();
				Ul ulluar = new Ul();

				String menugroup = "";
				String menusubgroup = "";
				String menugroup_now = "";

				List<Musergroupmenu> objListmenu = new MusergroupmenuDAO().listByFilter(
						"musergroup.musergrouppk = " + oUser.getMusergroup().getMusergrouppk(),
						"mmenu.menuorderno, mmenu.menuname");
				if (objListmenu.size() > 0) {
					for (final Musergroupmenu obj : objListmenu) {
						menugroup = obj.getMmenu().getMenugroup();

						if (!obj.getMmenu().getMenugroup().equals(menugroup_now)) {
							if (obj.getMmenu().getMenusubgroup().isEmpty()) {
								divMenuGrup = new org.zkoss.zul.Div();
								isSubgroup = false;
								menugroup_now = "";
								ulluar = new Ul();

								org.zkoss.zul.Div divLuar = new org.zkoss.zul.Div();
								divLuar.setClass("dropdown menu-apps");

								org.zkoss.zul.A tagA = new A();

								if (!obj.getMmenu().getMenugroupicon().isEmpty()) {
									Image icon = new Image();
									icon.setSrc("/images/" + obj.getMmenu().getMenugroupicon());
									tagA.appendChild(icon);
								}

								tagA.setClass("btn btn-link");
								Span span = new Span();
								span.appendChild(new Html(menugroup));

								tagA.appendChild(span);
								divLuar.appendChild(tagA);

								menuRoot.appendChild(divLuar);
							} else {
								isSubgroup = true;
								menugroup_now = menugroup;

								org.zkoss.zul.Div divLuar = new org.zkoss.zul.Div();
								divLuar.setClass("dropdown menu-apps");

								org.zkoss.zul.A tagA = new A();
								tagA.setClass("btn btn-link dropdown-toggle after-none");
								tagA.setHref("#");
								tagA.setAttribute("data-bs-toggle", "dropdown");
								tagA.setAttribute("aria-expanded", "false");
								tagA.setClientAttribute("data-bs-toggle", "dropdown");
								tagA.setClientAttribute("aria-expanded", "false");
								tagA.setStyle("font-size : 20px;");

								Span span = new Span();
								span.appendChild(new Html(menugroup));

								if (!obj.getMmenu().getMenugroupicon().isEmpty()) {
									Image icon = new Image();
									icon.setSrc("/images/" + obj.getMmenu().getMenugroupicon());
									tagA.appendChild(icon);
								}

								tagA.appendChild(span);

								Ul ulmenu = new Ul();
								ulmenu.setSclass("dropdown-menu p-2 shadow animation_delay");
								ulmenu.setWidgetClass("dropdown-menu p-2 shadow animation_delay");

								divLuar.appendChild(tagA);
								divLuar.appendChild(ulmenu);

								ulluar = ulmenu;
								divMenuGrup = divLuar;
								menuRoot.appendChild(divLuar);
							}

						}

						if (isSubgroup && menugroup_now.equals(obj.getMmenu().getMenugroup())) {
							Li limenu = new Li();

							org.zkoss.zul.A tagA = new A();
							tagA.setClass("dropdown-item");
							tagA.setStyle("padding-left:5px !important");

							Image img = new Image();
							img.setSrc("/images/" + obj.getMmenu().getMenuicon());
							img.setWidth("20px");
							img.setStyle("padding-right: 3px !important;");
							tagA.appendChild(img);
							Label label = new Label(obj.getMmenu().getMenuname());

							tagA.appendChild(label);

							limenu.appendChild(tagA);
							tagA.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

								@Override
								public void onEvent(Event event) throws Exception {
									divContent.getChildren().clear();
									Map<String, String> map = new HashMap<String, String>();
									map.put(obj.getMmenu().getMenuparamname().trim(),
											obj.getMmenu().getMenuparamvalue().trim());

									Executions.createComponents(obj.getMmenu().getMenupath().trim(), divContent, map);
									namaMenu.getChildren().clear();
									namaMenu.appendChild(new Html(obj.getMmenu().getMenusubgroup() + " - Menu "
											+ obj.getMmenu().getMenuname()));
									Clients.evalJavaScript("scrollTo('" + obj.getMmenu().getMenupath() + "')");

								}
							});

							ulluar.appendChild(limenu);
						}
					}
				}

				Clients.evalJavaScript("startTheme()");
				if (namaMenu.getChildren().size() == 0) {
					namaMenu.appendChild(new Html("Dashboard"));
				}

				// zkSession.setAttribute("oUser", user);

				doReset(null);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Command
	public void doRedirect(@BindingParam("item") Mmenu obj) {
		if (obj != null) {
			try {
				divContent.getChildren().clear();
				Map<String, String> map = new HashMap<String, String>();
				map.put(obj.getMenuparamname().trim(), obj.getMenuparamvalue().trim());
				

				map.put("a", null);
				map.put("b", null);

				Executions.createComponents(obj.getMenupath().trim(), divContent, map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Command
	@NotifyChange("*")
	public void doSearch() {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange("*")
	public void doReset(@BindingParam("arg") String arg) {
		
	}
	
	
	@Command
	public void doChangePassword() {
		Map<String, Object> map = new HashMap<>();
		map.put("userid", muser.getUserid());
		Window win = (Window) Executions.createComponents("/view/User Management/changepassword.zul", null, map);
		win.setWidth("50%");
		win.setClosable(true);
		win.doModal();
	}

	@Command
	public void doLogout() {
		try {
			Muser user = new MuserDAO().findByUserid(oUser.getUserid().trim());
			if (user != null) {
				session = StoreHibernateUtil.openSession();
				transaction = session.beginTransaction();
				user.setIslogin(0);
				user.setLastlogin(new Date());
				new MuserDAO().save(session, user);
				transaction.commit();
				session.close();
			}

			if (zkSession.getAttribute("oUser") != null) {
				zkSession.removeAttribute("oUser");
			}

			Executions.sendRedirect("/");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Command
	public void viewProfil() {
		try {
			Muser user = new MuserDAO().findByUserid(oUser.getUserid().trim());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("obj", user);
			Window win = (Window) Executions.createComponents("/view/User Management/profile.zul", null, map);
			win.setWidth("60%");
			win.setClosable(true);
			win.doModal();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Mmenu getMmenu() {
		return mmenu;
	}

	public void setMmenu(Mmenu mmenu) {
		this.mmenu = mmenu;
	}

	public Muser getoUser() {
		return oUser;
	}

	public void setoUser(Muser oUser) {
		this.oUser = oUser;
	}

}