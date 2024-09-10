package com.projectSta.viewmodel;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Date;

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
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;
import java.text.SimpleDateFormat;


import com.projectSta.dao.MemployeeDAO;

import com.projectSta.dao.MtletterDAO;
import com.projectSta.domain.Mtletter;
import com.projectSta.domain.Memployee;
import com.projectSta.listmodel.ListmodelLetter;
import com.projectSta.utils.SysUtils;
import com.projectSta.utils.db.StoreHibernateUtil;
import com.projectSta.utils.PdfExport;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class SppdListVM {
	org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Session session;
	private Transaction transaction;
	private int pageStartNumber;
	private boolean needsPageUpdate;
	private int pageTotalSize;
	private ListmodelLetter model;
	private String filter;
	private String orderby;
	private Memployee nama;
	private Date tgl_berangkat;
	private Date tgl_pulang;
	private String tujuan;

	@Wire
	private Grid grid;
	@Wire
	private Paging paging;


	@Wire
	private Div divPaging;
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


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

		grid.setRowRenderer(new RowRenderer<Mtletter>() {
			@Override
			public void render(Row row, Mtletter data, int index) throws Exception {
				Label label = new Label();

				label = new Label(String.valueOf((SysUtils.PAGESIZE * pageStartNumber) + index + 1));
				label.setStyle("font-size: 14px;");
				row.getChildren().add(label);
				

				label = new Label(data.getMemployeefk() != null ? data.getMemployeefk().getNama() : "");
				label.setStyle("font-size: 14px;");
				row.getChildren().add(label);	

				String tglBerangkat = data.getTgl_berangkat() != null ? dateFormat.format(data.getTgl_berangkat()) : "";
				label = new Label(tglBerangkat);
				label.setStyle("font-size: 14px;");
				row.getChildren().add(label);

				String tglPulang = data.getTgl_pulang() != null ? dateFormat.format(data.getTgl_pulang()) : "";
				label = new Label(tglPulang);
				label.setStyle("font-size: 14px;");
				row.getChildren().add(label);

				label = new Label(data.getTujuan() != null ? data.getTujuan() : "");
				label.setStyle("font-size: 14px;");
				row.getChildren().add(label);
				
				
				label = new Label(data.getKeterangan() != null ? data.getKeterangan() : "");
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
						Window win = (Window) Executions.createComponents("/view/Sppd Management/sppdform.zul", null,
								map);
						win.setWidth("55%");
						win.setClosable(true);
						win.doModal();
						win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
							@Override
							public void onEvent(Event event) throws Exception {
								doReset();
								BindUtils.postNotifyChange(null, null, SppdListVM.this, "*");
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
						Window win = (Window) Executions.createComponents("/view/Sppd Management/sppdform.zul", null,
								map);
						win.setWidth("55%");
						win.setClosable(true);
						win.doModal();
						win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {
							@Override
							public void onEvent(Event event) throws Exception {
								doReset();
								BindUtils.postNotifyChange(null, null, SppdListVM.this, "*");
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
				
				Button btnPrint = new Button("Print");
				btnPrint.setStyle("margin: 3px; padding: 10px 20px; font-size: 16px; width: 100px;");
				btnPrint.setSclass("btn btn-primary btn-lg-active");
				btnPrint.setAutodisable("self");
				btnPrint.setTooltiptext("Print");
				btnPrint.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
					@Override
					public void onEvent(Event event) throws Exception {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("obj", data);
						map.put("isPrint", "Y");
						createPrintContent(data);
						doReset();
					}
				});
				


				Div div = new Div();
				div.appendChild(btnDetail);
				div.appendChild(btnEdit);
				div.appendChild(btnPrint);
				div.appendChild(btnDelete);

				row.getChildren().add(div);
			}
		});
	}

	
	private void createPrintContent(Mtletter data) {
	    PrinterJob job = PrinterJob.getPrinterJob(); 
	    job.setPrintable(new Printable() {
	        @Override
	        public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
	            if (page > 0) {
	                return NO_SUCH_PAGE;
	            }

	            Graphics2D g2d = (Graphics2D) g;
	            g2d.translate(pf.getImageableX(), pf.getImageableY());

	            // Set font style and size
	            Font font = new Font("Century", Font.PLAIN, 18);
	            g2d.setFont(font);
	            int y = 100;
	            int cellHeight = 60;

	            g2d.drawString("Nama pegawai: " + data.getMemployeefk().getNama(), 100, y);
	            y += cellHeight;
	            g2d.drawString("Tanggal berangkat: " + data.getTgl_berangkat(), 100, y);
	            y += cellHeight;
	            g2d.drawString("Tanggal Pulang: " + data.getTgl_pulang(), 100, y);
	            y += cellHeight;
	            g2d.drawString("Tujuan: " + data.getTujuan(), 100, y);
	            y += cellHeight;
	            g2d.drawString("Keterangan: " + data.getKeterangan(), 100, y);
	            y += cellHeight;

	            return PAGE_EXISTS;
	        }
	    });

	    // Show the print dialog
	    boolean doPrint = job.printDialog();
	    if (doPrint) {
	        try {
	            job.print(); // Trigger the actual print
	        } catch (PrinterException ex) {
	            ex.printStackTrace();
	        }
	    }
	}

	@Command
	@NotifyChange("*")
	public void doSearch() {
		if (nama != null) {
			filter += " and MemployeeFK = " + nama.getMemployeepk();
		}
		
		// Filter by Tanggal Berangkat
        if (tgl_berangkat != null) {
            filter += " and tgl_berangkat >= '" + new java.sql.Date(tgl_berangkat.getTime()) + "'";
        }

        // Filter by Tanggal Pulang
        if (tgl_pulang != null) {
            filter += " and tgl_pulang <= '" + new java.sql.Date(tgl_pulang.getTime()) + "'";
        }
		needsPageUpdate = true;
		paging.setActivePage(0);
		pageStartNumber = 0;
		doRefresh(pageStartNumber);
	}

	@Command
	@NotifyChange("*")
	public void doReset() {
		nama = null;
		tgl_berangkat = null;
		tgl_pulang = null;
		doRefresh(pageStartNumber);
		doSearch();
	}
	
	
		
	

	@Command
	public void doAddnew() {
		Window win = (Window) Executions.createComponents("/view/User Management/sppdform.zul", null, null);
		win.setWidth("55%");
		win.doModal();
		win.setClosable(true);
		win.addEventListener(Events.ON_CLOSE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				needsPageUpdate = true;
				doReset();
				BindUtils.postNotifyChange(null, null, SppdListVM.this, "*");
			}
		});
	}
	/*@Command
    public void exportToPDF() {
        try {
            List<Memployee> siswaList = loadSiswaData(); // Method to load the data to be exported
            PdfExport pdfExport = new PdfExport();
            String filePath = "C:\\Users\\rasya\\Documents\\tespdf\\siswa_export.pdf"; // Specify the file path

            pdfExport.exportToPDF(siswaList, filePath);

            // Notify the user
            Clients.showNotification("PDF exported successfully!", "info", null, "middle_center", 2000);

            // Optionally, open the file automatically
            File pdfFile = new File(filePath);
            if (pdfFile.exists()) {
                Executions.getCurrent().sendRedirect(filePath);
            }
        } catch (Exception e) {
            Clients.showNotification("Failed to export PDF: " + e.getMessage(), "error", null, "middle_center", 2000);
        }
    }

    // Method to load siswa data
    private List<Msiswa> loadSiswaData() {
        // Implement the logic to load the list of Msiswa objects from the database or other sources
        return null; // Replace with actual data retrieval code
    }*/


	public void doDelete(Mtletter user) {
		try {
			session = StoreHibernateUtil.openSession();
			transaction = session.beginTransaction();
			new MtletterDAO().delete(session, user);
			transaction.commit();
			session.close();

			Clients.evalJavaScript("swal.fire({" 
									+ "icon: 'success',\r\n" 
									+ "  title: 'Berhasil',\r\n"
									+ "  text: 'Data Berhasil Dihapus!'," + "})");
			needsPageUpdate = true;
			doReset();
			BindUtils.postNotifyChange(null, null, SppdListVM.this, "*");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doRefresh(int activePage) {
		orderby = "MemployeeFK";
		paging.setPageSize(SysUtils.PAGESIZE);
		model = new ListmodelLetter(activePage, SysUtils.PAGESIZE, filter, orderby);
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

	public ListModelList<Memployee> getKelasmodel() {
		ListModelList<Memployee> lm = null;
		try {
			lm = new ListModelList<Memployee>(new MemployeeDAO().listByFilter("0=0", "nama"));
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

	



	public Memployee getNama() {
		return nama;
	}

	public void setNama(Memployee nama) {
		this.nama = nama;
	}



	public Date getTgl_berangkat() {
		return tgl_berangkat;
	}


	public void setTgl_berangkat(Date tgl_berangkat) {
		this.tgl_berangkat = tgl_berangkat;
	}


	public Date getTgl_pulang() {
		return tgl_pulang;
	}


	public void setTgl_pulang(Date tgl_pulang) {
		this.tgl_pulang = tgl_pulang;
	}


	public String getTujuan() {
		return tujuan;
	}


	public void setTujuan(String tujuan) {
		this.tujuan = tujuan;
	}


	

	
}