package com.projectSta.viewmodel;

import java.math.BigDecimal;
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
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.projectSta.dao.MusergroupDAO;
import com.projectSta.domain.Musergroup;
import com.projectSta.listmodel.ListModelUsergroup;
import com.projectSta.utils.SysUtils;
import com.projectSta.utils.db.StoreHibernateUtil;

public class Musergrouplist {
	org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Session session;
	private Transaction transaction;
	private int pageStartNumber;
	private boolean needsPageUpdate;
	private int pageTotalSize;
	private ListModelUsergroup model;
	private String filter;
	private String orderby;
	private String usergroupcode;
	private String usergroupname;

	@Wire
	private Grid grid;
	@Wire
	private Paging paging;
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
		

		grid.setRowRenderer(new RowRenderer<Musergroup>() {
			@Override
			
			public void render(Row row, Musergroup data, int index) throws Exception {
				
				
				Label label = new Label();
				
				label = new Label(String.valueOf((SysUtils.PAGESIZE * pageStartNumber) + index + 1));
				label.setStyle("font-size: 14px;");
				row.getChildren().add(label);

				label = new Label(data.getUsergroupcode() != null ? data.getUsergroupcode() : "");
				label.setStyle("font-size: 14px;");
				row.getChildren().add(label);

				label = new Label(data.getUsergroupname() != null ? data.getUsergroupname() : "");
				label.setStyle("font-size: 14px;");
				row.getChildren().add(label);

				label = new Label(data.getUsergroupdesc() != null ? data.getUsergroupdesc() : "");
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
						Window win = (Window) Executions.createComponents("/view/User Management/usergroupform.zul",
								null, map);
						win.setWidth("30%");
						win.setClosable(true);
						win.doModal();
						win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
							@Override
							public void onEvent(Event event) throws Exception {
								doReset();
								BindUtils.postNotifyChange(null, null, Musergrouplist.this, "*");
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
						Window win = (Window) Executions.createComponents("/view/User Management/usergroupform.zul",
								null, map);
						win.setWidth("30%");
						win.setClosable(true);
						win.doModal();
						win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
							@Override
							public void onEvent(Event event) throws Exception {
								doReset();
								BindUtils.postNotifyChange(null, null, Musergrouplist.this, "*");
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
		Window win = (Window) Executions.createComponents("/view/User Management/usergroupform.zul", null, null);
		win.setWidth("60%");
		win.doModal();
		win.setClosable(true);
		win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				needsPageUpdate = true;
				doReset();
				BindUtils.postNotifyChange(null, null, Musergrouplist.this, "*");
			}
		});
	}

	@Command
	@NotifyChange("*")
	public void doSearch() {
		filter = "0=0";
		if (usergroupcode != null && usergroupcode.trim().length() > 0) {
			if (filter.length() > 0)
				filter += " and ";
			filter += "usergroupcode like '%" + usergroupcode.trim().toUpperCase() + "%'";
		}
		if (usergroupname != null && usergroupname.trim().length() > 0) {
			if (filter.length() > 0)
				filter += " and ";
			filter += "usergroupname like '%" + usergroupname.trim().toUpperCase() + "%'";
		}

		needsPageUpdate = true;
		paging.setActivePage(0);
		pageStartNumber = 0;
		doRefresh(pageStartNumber);
	}

	@Command
	@NotifyChange("*")
	public void doReset() {
		usergroupcode = "";
		usergroupname = "";
		doRefresh(pageStartNumber);
		doSearch();
	}

	public void doDelete(Musergroup usergroup) {
		try {
			session = StoreHibernateUtil.openSession();
			transaction = session.beginTransaction();
			
			new MusergroupDAO().delete(session, usergroup);
			transaction.commit();
			session.close();

			Clients.evalJavaScript("swal.fire({" 
									+ "icon: 'success',\r\n" 
									+ "  title: 'Berhasil',\r\n"
									+ "  text: 'Data Berhasil Dihapus!'," + "})");
			needsPageUpdate = true;
			doReset();
			BindUtils.postNotifyChange(null, null, Musergrouplist.this, "*");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doRefresh(int activePage) {
		orderby = "usergroupname";
		paging.setPageSize(SysUtils.PAGESIZE);
		model = new ListModelUsergroup(activePage, SysUtils.PAGESIZE, filter, orderby);
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

	public int getPageTotalSize() {
		return pageTotalSize;
	}

	public void setPageTotalSize(int pageTotalSize) {
		this.pageTotalSize = pageTotalSize;
	}

	public String getUsergroupcode() {
		return usergroupcode;
	}

	public void setUsergroupcode(String usergroupcode) {
		this.usergroupcode = usergroupcode;
	}

	public String getUsergroupname() {
		return usergroupname;
	}

	public void setUsergroupname(String usergroupname) {
		this.usergroupname = usergroupname;
	}
}