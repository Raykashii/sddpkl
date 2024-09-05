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

import com.projectSta.dao.MmenuDAO;
import com.projectSta.dao.MusergroupDAO;
import com.projectSta.dao.MusergroupmenuDAO;
import com.projectSta.domain.Mmenu;
import com.projectSta.domain.Muser;
import com.projectSta.domain.Musergroup;


import com.projectSta.domain.Musergroupmenu;
import com.projectSta.utils.db.StoreHibernateUtil;

public class MusergroupFormVM {
	org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	
	private Session session;
	private Transaction transaction;
	private Musergroup obj;
	private boolean isInsert;
	
	private List<Musergroupmenu> listGroupmenu;
	private Map<Integer, Mmenu> map = new HashMap<Integer, Mmenu>();
	private List<Mmenu> listMmenu = new ArrayList<Mmenu>();
	private List<Musergroupmenu> listgroup = new ArrayList<Musergroupmenu>();
	
	@Wire
	private Grid gridMenu, gridSetup;
	
	@Wire
	private Window winUsergroupform;
	
	@Wire
	private Button btnSave, btnCancel;
	
	@Wire
	private Groupbox gbDaftar;
	
	@Wire
	private Div divRoot, divFooter, divListMenu;
	
	@Wire
	private Textbox tbUsergroupcode,tbUsergroupname, tbUsergroupdesc;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, 
			@ExecutionArgParam("isEdit") final String isEdit, @ExecutionArgParam("isDetail") final String isDetail,
			@ExecutionArgParam("obj") Musergroup obj) throws Exception {
		Selectors.wireComponents(view, this, false);
		doReset();
		
		if (obj != null) {
			this.obj = obj;
			isInsert = false;
			listGroupmenu = new MusergroupmenuDAO().listByFilter("musergroup.musergrouppk = " +  obj.getMusergrouppk(), "mmenu.menuorderno");
		}
		
		if(isEdit != null && isEdit.equals("Y")) {
			btnSave.setLabel(Labels.getLabel("common.update"));
			btnCancel.setVisible(false);
			
			if (listGroupmenu.size() > 0 ) {
				for (Musergroupmenu data : listGroupmenu) {
					map.put(data.getMmenu().getMmenupk(), data.getMmenu());
				}
				
				if (map.size() > 0)
					btnSave.setDisabled(false);
				
			}
		}
		
		if (isDetail != null && isDetail.equals("Y")) {
			tbUsergroupcode.setReadonly(true);
			tbUsergroupname.setReadonly(true);
			tbUsergroupdesc.setReadonly(true);
			
			divFooter.setVisible(false);
			gbDaftar.setVisible(true);
			gridSetup.setVisible(false);
			
			if (listGroupmenu.size() > 0) {
				gridMenu.setModel(new ListModelList<>(listGroupmenu));			
				}
				winUsergroupform.setStyle("border-radius: 7px; "
										+ "background-color: #2F3061;");
				}
		
			if (gridMenu != null) {
				gridMenu.setRowRenderer(new RowRenderer<Musergroupmenu>() {
					@Override
					public void render(Row row, final Musergroupmenu data, int index) throws Exception {
						row.getChildren().add(new Label(String.valueOf(index + 1)));
						row.getChildren().add(new Label(data.getMmenu().getMenugroup() + " - " + data.getMmenu().getMenuname()));
					}
				});
			}
			doListMenu();
	}
	
	@NotifyChange("*")
	public void doListMenu() {
		try {
			divListMenu.getChildren().clear();

			List<Mmenu> menus = new MmenuDAO().listByFilter("0=0", "menuorderno");
			if (menus.size() > 0) {
				String menugroup = new String();
				Grid grid = null;
				Rows rows = null;
				for (Mmenu menu : menus) {
					if (!menugroup.equals(menu.getMenugroup().trim())) {
						menugroup = menu.getMenugroup().trim();
						divListMenu.appendChild(new Separator());
						grid = new Grid();
						Columns columns = new Columns();
						Column column1 = new Column();
						column1.setAlign("center");
						column1.setWidth("80px");
						final Checkbox chk = new Checkbox();

						chk.addEventListener(Events.ON_CHECK, new EventListener<Event>() {
							@Override
							public void onEvent(Event event) throws Exception {
								doChechkedAll((Grid) chk.getParent().getParent().getParent(), chk.isChecked());
							}
						});

						column1.appendChild(chk);
						Column column2 = new Column(menugroup);
						column2.setStyle(
								"color: #f2f2f2; "
								+ "font-size: 14px; "
								+ "font-weight: bold; "
								+ "background-color: #2F3061; "
								+ "border-color: #ffffff;");
						columns.setStyle("background-color:#f2f2f2; "
										+ "border-color:#f2f2f2");
						column1.setStyle(
								"color: #f2f2f2; "
								+ "font-size: 14px; "
								+ "font-weight: bold; "
								+ "background-color: #2F3061; "
								+ "border-color: #ffffff;");
						columns.appendChild(column1);
						columns.appendChild(column2);
						grid.appendChild(columns);
						rows = new Rows();

						grid.appendChild(rows);
						grid.setParent(divListMenu);
					}
					Row row = new Row();
					final Checkbox chk = new Checkbox();
					chk.setAttribute("obj", menu);
					chk.addEventListener(Events.ON_CHECK, new EventListener<Event>() {

						@Override
						public void onEvent(Event event) throws Exception {
							if (chk.isChecked()) {

								listMmenu.add((Mmenu) chk.getAttribute("obj"));
								boolean isCheckall = true;
								Grid grid = (Grid) chk.getParent().getParent().getParent();
								List<Row> components = grid.getRows().getChildren();
								for (Row comp : components) {
									Checkbox chk = (Checkbox) comp.getChildren().get(0);
									if (!chk.isChecked()) {
										isCheckall = false;
									}
								}
									if (isCheckall) {
										List<Column> cols = grid.getColumns().getChildren();
										System.out.println(cols.size());
										for (Column col : cols) {
											System.out.println(col.getChildren().size());
											if (col.getChildren().size() > 0) {
												Checkbox chk = (Checkbox) col.getChildren().get(0);
													if (chk != null) {
														chk.setChecked(true);
													}
												}
											}
										}
									} else {
										listMmenu.remove(chk.getAttribute("obj"));
										Grid grid = (Grid) chk.getParent().getParent().getParent();
										List<Column> cols = grid.getColumns().getChildren();
										for (Column col : cols) {
											if (col.getChildren().size() > 0) {
												Checkbox chk = (Checkbox) col.getChildren().get(0);
												if (chk != null) {
													chk.setChecked(false);
												}
											}
										}
									}
									if (listMmenu.size() == 0) {
										btnSave.setDisabled(true);
									}
							}
					});
					
					try {
						if (map != null && map.get(menu.getMmenupk()) != null) {
							chk.setChecked(true);
							listMmenu.add(menu);
						} else {
							chk.setChecked(false);
							System.out.println(menu.getMenuname());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					Label label = new Label(menu.getMenuname().trim());
					row.appendChild(chk);
					row.appendChild(label);
					row.setParent(rows);
				}
			}
		} catch (Exception e) {
			if (e.getCause() instanceof ConnectException)
				Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
	}
	
	public void doChechkedAll(Grid grid, boolean checked) {
		try {
			List<Row> components = grid.getRows().getChildren();
			for (Row comp : components) {
				Checkbox chk = (Checkbox) comp.getChildren().get(0);
				if (checked) {
					chk.setChecked(true);
					listMmenu.remove(chk.getAttribute("obj"));
					listMmenu.add((Mmenu) chk.getAttribute("obj"));
				} else {
					chk.setChecked(false);
					listMmenu.remove(chk.getAttribute("obj"));
				}
				
				if (listMmenu.size() == 0) {
					btnSave.setDisabled(true);
				}
			}
		} catch (Exception e) {
			if (e.getCause() instanceof ConnectException) {
				Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
			}
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Command
	@NotifyChange("*")
	public void doSave() {
		try {
			if (listMmenu.size() > 0 ) {
				Messagebox.show(
						isInsert ? Labels.getLabel("common.confirm.save") : Labels.getLabel("common.update.confirm"),
								WebApps.getCurrent().getAppName(), Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION,
								new EventListener() {
									public void onEvent(Event event) throws Exception {
										if (event.getName().equals("onOK")) {
											try {
												Musergroup datamug = null;
												
														if (isInsert)
															datamug = new MusergroupDAO().findByFilter(
																"usergroupcode = '" + obj.getUsergroupcode().trim() + "'");
														
														if (datamug != null) {
															Messagebox.show(
																	"Gagal menambahkan grup user, grup user code '" + obj.getUsergroupcode().trim()+ "' telah digunakan.",
																	"PERINGATAN", Messagebox.OK, Messagebox.EXCLAMATION);
														} else {
															try {
																Muser oUser = (Muser) zkSession.getAttribute("ouser");
																if (oUser == null)
																	oUser = new Muser();
																
																session = StoreHibernateUtil.openSession();
																System.out.println("tes 1");
																if (isInsert) {
																	System.out.println("tes 2");
																	obj.setCreatedby(oUser.getUserid());
																	obj.setCreatedtime(new Date());
																	
																	transaction = session.beginTransaction();
																	new MusergroupDAO().save(session, obj);
																	transaction.commit();
																	
																	for (Mmenu data : listMmenu) {
																		Musergroupmenu objMenu = new Musergroupmenu();
																		objMenu.setMmenu(data);
																		objMenu.setMusergroup(obj);
																		listgroup.add(objMenu);
																	}
																	
																	if (listgroup.size() > 0) {
																		System.out.println("tes 3");
																		for (Musergroupmenu usergroupmenu : listgroup) {
																			Musergroupmenu musergroupmenu = new Musergroupmenu();
																			musergroupmenu.setMmenu(usergroupmenu.getMmenu());
																			musergroupmenu.setMusergroup(usergroupmenu.getMusergroup());
																			
																			transaction = session.beginTransaction();
																			new MusergroupmenuDAO().save(session, musergroupmenu);
																			transaction.commit();																			
																		}
																	}
																	session.close();
																	
																	Clients.evalJavaScript("swal.fire({" + "icon: 'success', \r\n" 
																							+ "title: 'berhasil!!!', \r\n" + " text: Grup user "
																							+ obj.getUsergroupname().toUpperCase().trim()
																							+ "Berhasil DITAMBAHKANN!!!'," + "})");
																} else {
																	obj.setUpdatedby(oUser.getUserid());
																	obj.setLastupdated(new Date());
																	
																	transaction = session.beginTransaction();
																	new MusergroupDAO().save(session, obj);
																	transaction.commit();
																	
																	List<Musergroupmenu> listMenus = new MusergroupmenuDAO()
																			.listByFilter(
																					"musergroup.musergrouppk = " + obj.getMusergrouppk(),
																					"mmenu.menuorderno asc");
																	for (Musergroupmenu data : listMenus) {
																		transaction = session.beginTransaction();
																		new MusergroupmenuDAO().delete(session, data);
																		transaction.commit();
																	}
																	
																	if (listgroup.size() > 0 ) {
																		for (Musergroupmenu usergroupmenu : listgroup) {
																			Musergroupmenu musergroupmenu = new Musergroupmenu();
																			musergroupmenu.setMmenu(usergroupmenu.getMmenu());
																			musergroupmenu.setMusergroup(usergroupmenu.getMusergroup());
																			
																			transaction = session.beginTransaction();
																			new MusergroupmenuDAO().save(session, musergroupmenu);
																			transaction.commit();
																		}
																	}
																	Clients.evalJavaScript("swal,fire({" + "icon: 'success', \r\n"
																							+" title: 'berhasill!!', \r\n" + "text: 'User"
																							+ obj.getUsergroupname().trim() + " Berhasil Diperbarui!!',"
																							+ "})");				
																	}
																	doClose();
															} catch (Exception e) {
																// TODO: handle exception
																e.printStackTrace();
															}
															
														}												
													} catch (Exception e) {
														// TODO: handle exception
														e.printStackTrace();
													}
													
													doReset();
										}
									}
								});
									}else {
										Messagebox.show("Menu grup user harus dipilih.", WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.INFORMATION);
									}
								} catch (Exception e) {
									// TODO: handle exception
									if (isInsert)
										Messagebox.show("Error : " + e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
									
									if(e.getCause() instanceof ConnectException)
										Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
										
									e.printStackTrace();
								}
							}
	@Command
	@NotifyChange("*")
	public void doReset() {
		winUsergroupform.setStyle("border-radius: 7px;"
								  + "background-color: #e27d3b");
		obj = new Musergroup();
		isInsert = true;
		map.clear();
		doListMenu();
		gbDaftar.setVisible(false);
		gridSetup.setVisible(true);
	}
	
	public void doClose() {
		Event closeevent = new Event("onClose", winUsergroupform, obj);
		Events.postEvent(closeevent);
	}
	
	public Validator getValidator() {
		return new AbstractValidator() {
			
			@Override
			public void validate(ValidationContext ctx) {
				String usergroupcode = (String) ctx.getProperties("usergroupcode")[0].getValue();
				String usergroupname = (String) ctx.getProperties("usergroupname")[0].getValue();
				
				if (usergroupcode == null || "".equals(usergroupcode.trim()))
					this.addInvalidMessage(ctx, "usergroupcode", Labels.getLabel("common.validator.empty"));
				if (usergroupname == null || "".equals(usergroupname.trim()))
					this.addInvalidMessage(ctx, usergroupname, Labels.getLabel("common.validator.empty"));
		
			}
		};
	}

	public Musergroup getObj() {
		return obj;
	}

	public void setObj(Musergroup obj) {
		this.obj = obj;
	}
	
	
	
}
	
