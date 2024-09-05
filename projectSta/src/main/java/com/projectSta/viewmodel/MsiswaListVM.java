package com.projectSta.viewmodel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

import com.projectSta.dao.MsiswaDAO;
import com.projectSta.dao.MkelasDAO;
import com.projectSta.domain.Msiswa;
import com.projectSta.domain.Mkelas;
import com.projectSta.listmodel.ListmodelSiswa;
import com.projectSta.utils.PdfExport;
import com.projectSta.utils.SysUtils;
import com.projectSta.utils.db.StoreHibernateUtil;

public class MsiswaListVM {
	org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Session session;
	private Transaction transaction;
	private int pageStartNumber;
	private boolean needsPageUpdate;
	private int pageTotalSize;
	private ListmodelSiswa model;
	private String filter;
	private String orderby;
	private String nisn;
	private String namasiswa;
	private Mkelas mkelas;

	@Wire
	private Grid grid;
	@Wire
	private Paging paging;
	
	@Wire
	private String jeniskelamin;
	@Wire
	private ListModelList<String> jenisKelaminModel;

	@Wire
	private Combobox cbUsergrup;
	@Wire
	private Div divPaging;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
		Selectors.wireComponents(view, this, false);
		
		 jenisKelaminModel = new ListModelList<>(Arrays.asList("L", "P"));
		
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

		grid.setRowRenderer(new RowRenderer<Msiswa>() {
			@Override
			public void render(Row row, Msiswa data, int index) throws Exception {
				Label label = new Label();

				label = new Label(String.valueOf((SysUtils.PAGESIZE * pageStartNumber) + index + 1));
				label.setStyle("font-size: 14px;");
				row.getChildren().add(label);

				label = new Label(data.getNisn() != null ? data.getNisn() : "");
				label.setStyle("font-size: 14px;");
				row.getChildren().add(label);

				label = new Label(data.getNamasiswa() != null ? data.getNamasiswa() : "");
				label.setStyle("font-size: 14px;");
				row.getChildren().add(label);

				label = new Label(data.getJeniskelamin() != null ? data.getJeniskelamin() : "");
				label.setStyle("font-size: 14px;");
				row.getChildren().add(label);
				
				

				label = new Label(data.getMkelasfk() != null ? data.getMkelasfk().getNamakelas() : "");
				label.setStyle("font-size: 14px;");
				row.getChildren().add(label);	

				Button btnDetail = new Button("Detail");
				btnDetail.setStyle("margin: 3px; padding: 10px 20px; font-size: 16px; width: 100px;");
				btnDetail.setSclass("btn btn-primary btn-lg-active");
				btnDetail.setAutodisable("self");
				btnDetail.setTooltiptext("Detail");
				btnDetail.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("obj", data);
						map.put("isDetail", "Y");
						Window win = (Window) Executions.createComponents("/view/User Management/siswaform.zul", null,
								map);
						win.setWidth("55%");
						win.setClosable(true);
						win.doModal();
						win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
							@Override
							public void onEvent(Event event) throws Exception {
								doReset();
								BindUtils.postNotifyChange(null, null, MsiswaListVM.this, "*");
							}
						});
					}
				});

				Button btnEdit = new Button("Ubah");
				btnEdit.setStyle("margin: 3px; padding: 10px 20px; font-size: 16px; width: 100px;");
				btnEdit.setSclass("btn btn-success btn-lg-active");
				btnEdit.setAutodisable("self");
				btnEdit.setTooltiptext("Edit");
				btnEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("obj", data);
						map.put("isEdit", "Y");
						Window win = (Window) Executions.createComponents("/view/User Management/siswaform.zul", null,
								map);
						win.setWidth("55%");
						win.setClosable(true);
						win.doModal();
						win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
							@Override
							public void onEvent(Event event) throws Exception {
								doReset();
								BindUtils.postNotifyChange(null, null, MsiswaListVM.this, "*");
							}
						});
					}
				});

				Button btnDelete = new Button("Hapus");
				btnDelete.setStyle("margin: 3px; padding: 10px 20px; font-size: 16px; width: 100px;");
				btnDelete.setSclass("btn btn-danger btn-lg-active");
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
		Window win = (Window) Executions.createComponents("/view/User Management/siswaform.zul", null, null);
		win.setWidth("55%");
		win.doModal();
		win.setClosable(true);
		win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				needsPageUpdate = true;
				doReset();
				BindUtils.postNotifyChange(null, null, MsiswaListVM.this, "*");
			}
		});
	}

	@Command
	@NotifyChange("*")
	public void doSearch() {
		filter = "0=0";
		if (nisn != null && nisn.trim().length() > 0) {
			if (filter.length() > 0)
				filter += " and ";
			filter += "nisn like '%" + nisn.trim().toUpperCase() + "%'";
		}
		if (namasiswa != null && namasiswa.trim().length() > 0) {
			if (filter.length() > 0)
				filter += " and ";
			filter += "namasiswa like '%" + namasiswa.trim().toUpperCase() + "%'";
		}
		 if (jeniskelamin != null && !jeniskelamin.trim().isEmpty()) {
		        filter += " and jeniskelamin = '" + jeniskelamin + "'";
		    }
		if (mkelas != null) {
			filter += " and mkelasfk = " + mkelas.getMkelaspk();
		}

		needsPageUpdate = true;
		paging.setActivePage(0);
		pageStartNumber = 0;
		doRefresh(pageStartNumber);
	}

	@Command
	@NotifyChange("*")
	public void doReset() {
		nisn = "";
		namasiswa = "";
		jeniskelamin="";
		mkelas = null;
		cbUsergrup.setValue(null);
		doRefresh(pageStartNumber);
		doSearch();
	}
	
	 /*  @Command
	    public void doExportToPDF() {
	        try {
	            List<Msiswa> siswaList = loadSiswaData(); // Method to load the data to be exported
	            PdfExport pdfExport = new PdfExport();
	            pdfExport.exportToPDF(siswaList, "C:/path-to-save/siswa_export.pdf"); // Specify the file path

	            Clients.showNotification("PDF exported successfully!", "info", null, "middle_center", 2000);
	        } catch (Exception e) {
	            Clients.showNotification("Failed to export PDF: " + e.getMessage(), "error", null, "middle_center", 2000);
	        }
	    }*/

	public void doDelete(Msiswa user) {
		try {
			session = StoreHibernateUtil.openSession();
			transaction = session.beginTransaction();
			new MsiswaDAO().delete(session, user);
			transaction.commit();
			session.close();

			Clients.evalJavaScript("swal.fire({" 
									+ "icon: 'success',\r\n" 
									+ "  title: 'Berhasil',\r\n"
									+ "  text: 'Data Berhasil Dihapus!'," + "})");
			needsPageUpdate = true;
			doReset();
			BindUtils.postNotifyChange(null, null, MsiswaListVM.this, "*");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doRefresh(int activePage) {
		orderby = "nisn";
		paging.setPageSize(SysUtils.PAGESIZE);
		model = new ListmodelSiswa(activePage, SysUtils.PAGESIZE, filter, orderby);
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

	public ListModelList<Mkelas> getKelasmodel() {
		ListModelList<Mkelas> lm = null;
		try {
			lm = new ListModelList<Mkelas>(new MkelasDAO().listByFilter("0=0", "namakelas"));
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

	

	public String getNisn() {
		return nisn;
	}

	public void setNisn(String nisn) {
		this.nisn = nisn;
	}


	public String getNamasiswa() {
		return namasiswa;
	}

	public void setNamasiswa(String namasiswa) {
		this.namasiswa = namasiswa;
	}

	public Mkelas getMkelas() {
		return mkelas;
	}

	public void setMkelas(Mkelas mkelas) {
		this.mkelas = mkelas;
	}

	public String getJeniskelamin() {
		return jeniskelamin;
	}

	public ListModelList<String> getJenisKelaminModel() {
		return jenisKelaminModel;
	}

	public void setJenisKelaminModel(ListModelList<String> jenisKelaminModel) {
		this.jenisKelaminModel = jenisKelaminModel;
	}

	public void setJeniskelamin(String jeniskelamin) {
		this.jeniskelamin = jeniskelamin;
	}

	
}