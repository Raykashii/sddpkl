package com.projectSta.viewmodel;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.projectSta.dao.MuserDAO;
import com.projectSta.dao.MusergroupDAO;
import com.projectSta.domain.Muser;
import com.projectSta.domain.Musergroup;
import com.projectSta.listmodel.ListmodelUser;
import com.projectSta.utils.SysUtils;
import com.projectSta.utils.db.StoreHibernateUtil;

public class MuserListVM {
	org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Session session;
	private Transaction transaction;
	private int pageStartNumber;
	private boolean needsPageUpdate;
	private int pageTotalSize;
	private ListmodelUser model;
	private String filter;
	private String orderby;
	private String userid;
	private String username;
	private Musergroup musergroup;

	@Wire
	private Grid grid;
	@Wire
	private Paging paging;
	@Wire
	private Combobox cbUsergrup;
	@Wire
	private Div divPaging;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
		Selectors.wireComponents(view, this, false);

		paging.addEventListener("onPaging", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				PagingEvent pe = (PagingEvent) event;
				pageStartNumber = pe.getActivePage();
				doRefresh(pageStartNumber);
			}
		});
		needsPageUpdate = true;
		doRefresh(pageStartNumber);

		grid.setRowRenderer(new RowRenderer<Muser>() {
			@Override
			public void render(Row row, Muser data, int index) throws Exception {
				Label label = new Label();

				label = new Label(String.valueOf((SysUtils.PAGESIZE * pageStartNumber) + index + 1));
				label.setStyle("font-size: 14px;");
				row.getChildren().add(label);

				label = new Label(data.getUserid() != null ? data.getUserid() : "");
				label.setStyle("font-size: 14px;");
				row.getChildren().add(label);

				label = new Label(data.getUsername() != null ? data.getUsername() : "");
				label.setStyle("font-size: 14px;");
				row.getChildren().add(label);

				label = new Label(data.getEmail() != null ? data.getEmail() : "");
				label.setStyle("font-size: 14px;");
				row.getChildren().add(label);

				label = new Label(data.getMusergroup() != null ? data.getMusergroup().getUsergroupname() : "");
				label.setStyle("font-size: 14px;");
				row.getChildren().add(label);

//				Image imgCard = new Image();
//				imgCard.setTooltiptext(AppData.getIsOnline(data.getIslogin()));
//				imgCard.setSrc("/images/" + data.getIslogin() + ".png");
//				imgCard.setWidth("20px");
				label = new Label(data.getIslogin() == 0 ? "off" : "on");
				label.setStyle("font-size: 14px;");
				row.getChildren().add(label);

				Button btnDetail = new Button("Detail");
				btnDetail.setStyle("margin: 3px;");
				btnDetail.setSclass("btn btn-primary btn-sm");
				btnDetail.setAutodisable("self");
				btnDetail.setTooltiptext("Detail");
				btnDetail.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("obj", data);
						map.put("isDetail", "Y");
						Window win = (Window) Executions.createComponents("/view/User Management/userform.zul", null,
								map);
						win.setWidth("25%");
						win.setClosable(true);
						win.doModal();
						win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
							@Override
							public void onEvent(Event event) throws Exception {
								doReset();
								BindUtils.postNotifyChange(null, null, MuserListVM.this, "*");
							}
						});
					}
				});

				Button btnEdit = new Button("Ubah");
				btnEdit.setStyle("margin: 3px");
				btnEdit.setSclass("btn btn-success btn-sm");
				btnEdit.setAutodisable("self");
				btnEdit.setTooltiptext("Edit");
				btnEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("obj", data);
						map.put("isEdit", "Y");
						Window win = (Window) Executions.createComponents("/view/User Management/userform.zul", null,
								map);
						win.setWidth("25%");
						win.setClosable(true);
						win.doModal();
						win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
							@Override
							public void onEvent(Event event) throws Exception {
								doReset();
								BindUtils.postNotifyChange(null, null, MuserListVM.this, "*");
							}
						});
					}
				});

				Button btnDelete = new Button("Hapus");
				btnDelete.setStyle("margin: 3px");
				btnDelete.setSclass("btn btn-danger btn-sm");
				btnDelete.setAutodisable("self");
				btnDelete.setTooltiptext("Delete");
				btnDelete.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
						Messagebox.show(Labels.getLabel("common.delete.confirm"), "Confirm Dialog",
								Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener() {
									public void onEvent(Event event) throws Exception {
										if (event.getName().equals("onOK")) {
											doDelete(data);
										}
									}
								});
					}
				});

				Div div = new Div();
				div.appendChild(btnDetail);
				div.appendChild(btnEdit);
				div.appendChild(btnDelete);

				row.getChildren().add(div);
			}
		});
	}

	@Command
	public void doAddnew() {
		Window win = (Window) Executions.createComponents("/view/User Management/userform.zul", null, null);
		win.setWidth("25%");
		win.doModal();
		win.setClosable(true);
		win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				needsPageUpdate = true;
				doReset();
				BindUtils.postNotifyChange(null, null, MuserListVM.this, "*");
			}
		});
	}

	@Command
	@NotifyChange("*")
	public void doSearch() {
		filter = "0=0";
		if (userid != null && userid.trim().length() > 0) {
			if (filter.length() > 0)
				filter += " and ";
			filter += "userid like '%" + userid.trim().toUpperCase() + "%'";
		}
		if (username != null && username.trim().length() > 0) {
			if (filter.length() > 0)
				filter += " and ";
			filter += "username like '%" + username.trim().toUpperCase() + "%'";
		}
		if (musergroup != null) {
			filter += " and musergroupfk = " + musergroup.getMusergrouppk();
		}

		needsPageUpdate = true;
		paging.setActivePage(0);
		pageStartNumber = 0;
		doRefresh(pageStartNumber);
	}

	@Command
	@NotifyChange("*")
	public void doReset() {
		userid = "";
		username = "";
		musergroup = null;
		cbUsergrup.setValue(null);
		doRefresh(pageStartNumber);
		doSearch();
	}

	public void doDelete(Muser user) {
		try {
			session = StoreHibernateUtil.openSession();
			transaction = session.beginTransaction();
			new MuserDAO().delete(session, user);
			transaction.commit();
			session.close();

			Clients.evalJavaScript("swal.fire({" + "icon: 'success',\r\n" + "  title: 'Berhasil',\r\n"
					+ "  text: 'Data Berhasil Dihapus!'," + "})");
			needsPageUpdate = true;
			doReset();
			BindUtils.postNotifyChange(null, null, MuserListVM.this, "*");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doRefresh(int activePage) {
		orderby = "userid";
		paging.setPageSize(SysUtils.PAGESIZE);
		model = new ListmodelUser(activePage, SysUtils.PAGESIZE, filter, orderby);
		if (needsPageUpdate) {
			pageTotalSize = model.getTotalSize(filter);
			needsPageUpdate = false;
		}
		
		if (pageTotalSize <= SysUtils.PAGESIZE)
			divPaging.setVisible(false);
		else
			divPaging.setVisible(true);
		
		paging.setTotalSize(pageTotalSize);
		grid.setModel(model);
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

	public int getPageTotalSize() {
		return pageTotalSize;
	}

	public void setPageTotalSize(int pageTotalSize) {
		this.pageTotalSize = pageTotalSize;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Musergroup getMusergroup() {
		return musergroup;
	}

	public void setMusergroup(Musergroup musergroup) {
		this.musergroup = musergroup;
	}
}