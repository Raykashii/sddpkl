package com.projectSta.viewmodel;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;

import com.projectSta.dao.DashboardDao;
import com.projectSta.domain.MPdpkproduk;
import com.projectSta.model.Mtable;
import com.projectSta.model.V_AllDpk;
import com.projectSta.model.V_Dpk;
import com.projectSta.model.VDataTotal;

import org.zkoss.chart.Chart;
import org.zkoss.chart.Charts;
import org.zkoss.chart.Color;
import org.zkoss.chart.Legend;
import org.zkoss.chart.Marker;
import org.zkoss.chart.PlotLine;
import org.zkoss.chart.Point;
import org.zkoss.chart.Responsive;
import org.zkoss.chart.Series;
import org.zkoss.chart.StackLabels;
import org.zkoss.chart.Theme;
import org.zkoss.chart.Title;
import org.zkoss.chart.XAxis;
import org.zkoss.chart.YAxis;
import org.zkoss.chart.model.CategoryModel;
import org.zkoss.chart.model.DefaultCategoryModel;
import org.zkoss.chart.plotOptions.ColumnPlotOptions;
import org.zkoss.chart.plotOptions.LinePlotOptions;
import org.zkoss.chart.plotOptions.PieDataLabels;
import org.zkoss.chart.plotOptions.PiePlotOptions;
import org.zkoss.chart.plotOptions.SeriesPlotOptions;

public class DashboardVm {
	private Calendar lastmonth;
	private int byyear;
	private Integer productbyyear;
	private String periode;
	private Charts charts;
	private Integer monthchart, monthchart1;
	private String[] months;
	private List<V_AllDpk> objList;
	private String time;
	private String timekredit;
	private Integer year1, year2, year3, year4;

	private DashboardDao oDao = new DashboardDao();

	@Wire
	private Combobox cbMonth, cbMonth1, cbyear1, cbyear2, cbyear3, cbyear4;

	@Wire
	private Div divPie, divChart, divCartLine, divCartLineKredit, divChartKredit, divChartKreditKepadaBank,
			divChartSumDPK, divChartKreditKepadaNonBank, divPieKredit, divPieKreditBankNonBank, divchartdpkyoy,
			divbanknonbankyoy, divPieDpk;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) throws Exception {
		Selectors.wireComponents(view, this, false);
		doReset();

	}

	@NotifyChange("*")
	public void doReset() {
		try {

			doResetDpk();
			doResetKredit();
//			doChartDpk();
			doChartSumDpk();
			doChartDpkTotal();
			doChartLineKreditTotal();
//			doChartKredit();
			doChartKreditBankNonBank("TAGIHAN KEPADA BANK");
			doChartKreditBankNonBank("TAGIHAN KEPADA NON BANK");
			doPieChartKreditBankNonBank();
			doChartDpkYoy(2022, 2023);
			doChartBankNonBankYoy(2022, 2023);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@NotifyChange("*")
	public void doResetDpk() {
		try {
			lastmonth = Calendar.getInstance();
			productbyyear = lastmonth.get(Calendar.YEAR);

			time = "PERIODE :" + new SimpleDateFormat("MMMM").format(new Date());

			cbMonth.setValue("-ALL MONTH-");

			String[] monthschart = new DateFormatSymbols().getMonths();
			for (int i = 0; i < monthschart.length; i++) {
				Comboitem item = new Comboitem();
				if (i < 12) {
					item.setLabel(monthschart[i]);

				} else {
					item.setLabel("-ALL MONTH-");
				}

				item.setValue(i + 1);
				cbMonth.appendChild(item);

			}
			// dropdown tahun
			cbyear1.setValue("Tahun");
			for (int i = 0; i < 5; i++) {
				Comboitem item = new Comboitem();
				String s = Integer.toString(productbyyear);
				item.setLabel(s);
				item.setValue(productbyyear);
				cbyear1.appendChild(item);
				productbyyear = productbyyear - 1;

			}

			cbyear2.setValue("Tahun");
			productbyyear = lastmonth.get(Calendar.YEAR);
			for (int i = 0; i < 5; i++) {
				Comboitem item = new Comboitem();
				String s = Integer.toString(productbyyear);
				item.setLabel(s);
				item.setValue(productbyyear);
				cbyear2.appendChild(item);
				productbyyear = productbyyear - 1;

			}

			System.out.println("data:" + monthchart);
			String table = "P_TDPK_" + new SimpleDateFormat("yyyyMM").format(new Date());
			doPieChartDpk("5");

			System.out.println(table);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@NotifyChange("*")
	public void doResetKredit() {
		try {

			lastmonth = Calendar.getInstance();
			productbyyear = lastmonth.get(Calendar.YEAR);

			time = "PERIODE :" + new SimpleDateFormat("MMMM").format(new Date());

			cbyear3.setValue("Tahun");
			for (int i = 0; i < 5; i++) {
				Comboitem item = new Comboitem();
				String s = Integer.toString(productbyyear);
				item.setLabel(s);
				item.setValue(productbyyear);
				cbyear3.appendChild(item);
				productbyyear = productbyyear - 1;

			}

			cbyear4.setValue("Tahun");
			productbyyear = lastmonth.get(Calendar.YEAR);
			for (int i = 0; i < 5; i++) {
				Comboitem item = new Comboitem();
				String s = Integer.toString(productbyyear);
				item.setLabel(s);
				item.setValue(productbyyear);
				cbyear4.appendChild(item);
				productbyyear = productbyyear - 1;

			}

			System.out.println("data:" + monthchart);
			String table = "P_TKREDIT_" + new SimpleDateFormat("yyyyMM").format(new Date());
			doPieChartKredit(table);
			System.out.println(table);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange("*")
	public void doSearchbyMonth() {

		System.out.println(monthchart);

		DateFormatSymbols dfs = new DateFormatSymbols(Locale.ENGLISH);
		String[] namaBulan = dfs.getMonths();
		String bulanString = namaBulan[monthchart - 1];

		time = "PERIODE :" + bulanString;

		String bulan = StringUtils.leftPad(String.valueOf(monthchart), 2, "0");

		String table = "P_TDPK_" + productbyyear + bulan;

		System.out.println(time);

		try {
			Mtable isTable = oDao.getTable(table);
			if (isTable != null) {
				System.out.println("month:" + monthchart.toString());
				doPieChartDpk(monthchart.toString());
			} else {
				Clients.evalJavaScript("swal.fire({" + "icon: 'warning',\r\n" + " title: 'Data tidak tersedia!',\r\n"
						+ " text: 'Data Periode Bulan Tidak Ditemukan!'," + "})");
				doPieChartKosong();

			}
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@Command
	@NotifyChange("*")
	public void doSearchbyMonth1() {

		DateFormatSymbols dfs = new DateFormatSymbols(Locale.ENGLISH);
		String[] namaBulan = dfs.getMonths();
		String bulanString = namaBulan[monthchart1 - 1];

		timekredit = "PERIODE :" + bulanString;

		String bulan = StringUtils.leftPad(String.valueOf(monthchart1), 2, "0");

		String table = "P_TKREDIT_" + productbyyear + bulan;

		System.out.println(time);

		try {
			Mtable isTable = oDao.getTable(table);
			if (isTable != null) {
				System.out.println("table" + table);
				doPieChartKredit(table);
			} else {
				Clients.evalJavaScript("swal.fire({" + "icon: 'warning',\r\n" + " title: 'Data tidak tersedia!',\r\n"
						+ " text: 'Data Periode Bulan Tidak Ditemukan!'," + "})");
				doPieChartKosong1();

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// BARU
	private void doPieChartDpk(String bulan) {
		try {

			divPieDpk.getChildren().clear();
			Charts chart = new Charts();
			chart.setType("pie");
			chart.setTitle("DPK PER-PRODUCT");
			chart.setPlotBackgroundColor((Color) null);
			chart.setPlotBorderWidth(null);
			chart.setPlotShadow(false);

			chart.getTooltip().setPointFormat("{series.name}: <b>{point.percentage:.1f}%</b>");

			chart.getAccessibility().getPoint().setValueSuffix("%");

			PiePlotOptions plotOptions = chart.getPlotOptions().getPie();

			plotOptions.setAllowPointSelect(true);
			plotOptions.setCursor("pointer");
			PieDataLabels dataLabels = (PieDataLabels) plotOptions.getDataLabels();
			dataLabels.setEnabled(true);
			dataLabels.setFormat("<b>{point.name}</b>: {point.percentage:.1f} %");

			Series series = chart.getSeries();
			series.setName("Products");
			series.setColorByPoint(true);
			int i = 0;

			for (V_Dpk product : oDao.getSumProductDpk(bulan)) {
				if (i == 0) {
					Point chrome = new Point(product.getKeterangan(), product.getNominal());
					chrome.setSelected(true);
					chrome.setSliced(true);
					series.addPoint(chrome);

				} else {
					series.addPoint(new Point(product.getKeterangan(), product.getNominal()));
				}
				i++;
			}

			divPieDpk.appendChild(chart);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("data tidak ditemukan");
			Clients.evalJavaScript("swal.fire({" + "icon: 'warning',\r\n" + " title: 'Data tidak tersedia!',\r\n"
					+ " text: 'Data Periode Bulan Tidak Ditemukan!'," + "})");
		}
	}

	private void doPieChartKredit(String table) {
		try {
//			divPieKredit.getChildren().clear();
			Charts chart = new Charts();
			chart.setType("pie");
			chart.setTitle("KREDIT BANK DAN NON BANK");
			chart.setPlotBackgroundColor((Color) null);
			chart.setPlotBorderWidth(null);
			chart.setPlotShadow(false);

			chart.getTooltip().setPointFormat("{series.name}: <b>{point.percentage:.1f}%</b>");

			chart.getAccessibility().getPoint().setValueSuffix("%");

			PiePlotOptions plotOptions = chart.getPlotOptions().getPie();

			plotOptions.setAllowPointSelect(true);
			plotOptions.setCursor("pointer");
			PieDataLabels dataLabels = (PieDataLabels) plotOptions.getDataLabels();
			dataLabels.setEnabled(true);
			dataLabels.setFormat("<b>{point.name}</b>: {point.percentage:.1f} %");

			Series series = chart.getSeries();
			series.setName("Products");
			series.setColorByPoint(true);

			Mtable checkTable = new DashboardDao().getTable(table);
			int i = 0;
			if (checkTable != null) {
				for (V_Dpk product : oDao.getProductKreditBankNonBank(table)) {
					if (i == 0) {
						Point chrome = new Point(product.getKeterangan(), product.getNominal());
						chrome.setSelected(true);
						chrome.setSliced(true);
						series.addPoint(chrome);

					} else {
						series.addPoint(new Point(product.getKeterangan(), product.getNominal()));
					}
					i++;
				}

				divPieKredit.appendChild(chart);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("data tidak ditemukan");
			Clients.evalJavaScript("swal.fire({" + "icon: 'warning',\r\n" + " title: 'Data tidak tersedia!',\r\n"
					+ " text: 'Data Periode Bulan Tidak Ditemukan!'," + "})");
		}
	}

	private void doPieChartKreditBankNonBank() {
		try {
			divPieKreditBankNonBank.getChildren().clear();
			Charts chart = new Charts();
			chart.setType("pie");
			chart.setTitle("KREDIT BANK DAN NON BANK TOTAL TAHUN");
			chart.setPlotBackgroundColor((Color) null);
			chart.setPlotBorderWidth(null);
			chart.setPlotShadow(false);

			chart.getTooltip().setPointFormat("{series.name}: <b>{point.percentage:.1f}%</b>");

			chart.getAccessibility().getPoint().setValueSuffix("%");

			PiePlotOptions plotOptions = chart.getPlotOptions().getPie();

			plotOptions.setAllowPointSelect(true);
			plotOptions.setCursor("pointer");
			PieDataLabels dataLabels = (PieDataLabels) plotOptions.getDataLabels();
			dataLabels.setEnabled(true);
			dataLabels.setFormat("<b>{point.name}</b>: {point.percentage:.1f} %");

			Series series = chart.getSeries();
			series.setName("Products");
			series.setColorByPoint(true);
			Map<String, BigDecimal> mapTotaldata = new HashMap<>();

			//

			for (int i = 0; i < months.length - 1; i++) {

				try {
					String bulan1 = StringUtils.leftPad(String.valueOf(i + 1), 2, "0");

					String table = "P_TKREDIT_" + productbyyear + bulan1;
					System.out.println(months);
					Mtable checkTable = new DashboardDao().getTable(table);
					if (checkTable != null) {
						List<V_Dpk> dataList = oDao.getProductKreditBankNonBank(table);
						for (V_Dpk data : dataList) {
							if (mapTotaldata.get(data.getKeterangan()) != null)
								mapTotaldata.put(data.getKeterangan(),
										mapTotaldata.get(data.getKeterangan()).add(data.getNominal()));
							else
								mapTotaldata.put(data.getKeterangan(), data.getNominal());

						}
					}

				} catch (Exception e) {

					e.printStackTrace();
				}

			}

			//

			int i = 0;
			for (Map.Entry product : mapTotaldata.entrySet()) {
				if (i == 0) {
					Point chrome = new Point(product.getKey().toString(), (Number) product.getValue());
					chrome.setSelected(true);
					chrome.setSliced(true);
					series.addPoint(chrome);

				} else {
					series.addPoint(new Point(product.getKey().toString(), (Number) product.getValue()));
				}
				i++;
			}

			divPieKreditBankNonBank.appendChild(chart);

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	private void doPieChartKosong() {
		try {
			divPieDpk.getChildren().clear();
			Charts chart = new Charts();
			chart.setType("pie");
			chart.setTitle("DPK PER-PRODUCT");
			chart.setPlotBackgroundColor((Color) null);
			chart.setPlotBorderWidth(null);
			chart.setPlotShadow(false);

			chart.getTooltip().setPointFormat("{series.name}: <b>{point.percentage:.1f}%</b>");

			chart.getAccessibility().getPoint().setValueSuffix("%");

			PiePlotOptions plotOptions = chart.getPlotOptions().getPie();

			plotOptions.setAllowPointSelect(true);
			plotOptions.setCursor("pointer");
			PieDataLabels dataLabels = (PieDataLabels) plotOptions.getDataLabels();
			dataLabels.setEnabled(true);
			dataLabels.setFormat("<b>{point.name}</b>: {point.percentage:.1f} %");

			Series series = chart.getSeries();
			series.setName("Products");
			series.setColorByPoint(true);

			divPieDpk.appendChild(chart);

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	private void doPieChartKosong1() {
		try {
			divPieKredit.getChildren().clear();
			Charts chart = new Charts();
			chart.setType("pie");
			chart.setTitle("DPK PER-PRODUCT");
			chart.setPlotBackgroundColor((Color) null);
			chart.setPlotBorderWidth(null);
			chart.setPlotShadow(false);

			chart.getTooltip().setPointFormat("{series.name}: <b>{point.percentage:.1f}%</b>");

			chart.getAccessibility().getPoint().setValueSuffix("%");

			PiePlotOptions plotOptions = chart.getPlotOptions().getPie();

			plotOptions.setAllowPointSelect(true);
			plotOptions.setCursor("pointer");
			PieDataLabels dataLabels = (PieDataLabels) plotOptions.getDataLabels();
			dataLabels.setEnabled(true);
			dataLabels.setFormat("<b>{point.name}</b>: {point.percentage:.1f} %");

			Series series = chart.getSeries();
			series.setName("Products");
			series.setColorByPoint(true);

			divPieKredit.appendChild(chart);

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	@Command
	@NotifyChange("*")
	public void doChartDpk() {
		try {
			divChart.getChildren().clear();
			lastmonth = Calendar.getInstance();
			List<String> bulan = new ArrayList<>();
			productbyyear = lastmonth.get(Calendar.YEAR);
			months = new DateFormatSymbols().getMonths();
			CategoryModel model = new DefaultCategoryModel();

			List<MPdpkproduk> objParamList = new ArrayList<>();
			objParamList = oDao.getDpkProduct();

			for (int i = 0; i < months.length - 1; i++) {

				try {
					String bulan1 = StringUtils.leftPad(String.valueOf(i + 1), 2, "0");

					String table = "P_TDPK_" + productbyyear + bulan1;
					System.out.println(months);
					bulan.add(months[i]);

					Mtable cekTable = oDao.getTable(table);
					if (cekTable == null) {
						for (MPdpkproduk data : objParamList) {
							model.setValue(data.getKeterangan().trim().toUpperCase(), months[i], 0);
						}
						// model.setValue("TABUNGAN", months[i], 0);

					} else {
						List<V_AllDpk> dpkList = oDao.getProductDpkAll(String.valueOf(i), table);

						for (V_AllDpk data : dpkList) {
							model.setValue(data.getKeterangan().trim().toUpperCase(), months[i], data.getNominal());
						}
					}

				} catch (Exception e) {

					e.printStackTrace();
				}

			}
			charts = new Charts();
			charts.setTitle("");
			charts.setType("column");
			charts.setModel(model);
			charts.getYAxis().getTitle().setText("Nominal (Rp.)");

			divChart.appendChild(charts);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange("*")
	public void doChartSumDpk() {
		try {
			divChartSumDPK.getChildren().clear();
			lastmonth = Calendar.getInstance();
			List<String> bulan = new ArrayList<>();
			productbyyear = lastmonth.get(Calendar.YEAR);
			months = new DateFormatSymbols().getMonths();
			CategoryModel model = new DefaultCategoryModel();

			List<MPdpkproduk> objParamList = new ArrayList<>();
			objParamList = oDao.getDpkProduct();

			for (int i = 0; i < months.length - 1; i++) {

				try {
					String bulan1 = StringUtils.leftPad(String.valueOf(i + 1), 2, "0");

					System.out.println(months);
					bulan.add(months[i]);

					List<V_AllDpk> dpkList = oDao.getSumProductDpkAll(String.valueOf(i + 1));

					for (MPdpkproduk data : objParamList) {
						model.setValue(data.getKeterangan().trim().toUpperCase(), months[i], 0);
					}

					for (V_AllDpk data : dpkList) {
						model.setValue(data.getKeterangan().trim().toUpperCase(), months[i], data.getNominal());
					}

				} catch (Exception e) {

					e.printStackTrace();
				}

			}
			charts = new Charts();
			charts.setTitle("");
			charts.setType("column");
			charts.setModel(model);
			charts.getYAxis().getTitle().setText("Nominal (Rp.)");

			divChartSumDPK.appendChild(charts);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doChartDpkTotal() {
		try {

			divCartLine.getChildren().clear();
			lastmonth = Calendar.getInstance();
			List<String> bulan = new ArrayList<>();
			productbyyear = lastmonth.get(Calendar.YEAR);
			months = new DateFormatSymbols().getMonths();
			List<VDataTotal> ListDpkperMonth = new ArrayList<>();
			CategoryModel model = new DefaultCategoryModel();

			for (int i = 0; i < months.length - 1; i++) {

				try {
					String bulan1 = StringUtils.leftPad(String.valueOf(i + 1), 2, "0");
					System.out.println(months);
					bulan.add(months[i]);

					ListDpkperMonth = oDao.getSumAlldpk(String.valueOf(i+1));
					if(ListDpkperMonth.size()==0) {
						model.setValue("", months[i], 0);
					}
					for (VDataTotal data : ListDpkperMonth) {
						model.setValue("", months[i], data.getNominal());
					}

				} catch (Exception e) {

					e.printStackTrace();
				}

			}

			Charts chart = new Charts();
			chart.setTitle("TOTAL DPK TAHUN :" + productbyyear);
			chart.setSubtitle("");

			chart.getYAxis().getTitle().setText("Nominal (Rp.)");

			chart.setModel(model);

			LinePlotOptions linePlotOptions = chart.getPlotOptions().getLine();
			linePlotOptions.setEnableMouseTracking(false);
			linePlotOptions.getDataLabels().setEnabled(true);

			divCartLine.appendChild(chart);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doChartLineKreditTotal() {
		try {

			divCartLineKredit.getChildren().clear();
			lastmonth = Calendar.getInstance();
			List<String> bulan = new ArrayList<>();
			productbyyear = lastmonth.get(Calendar.YEAR);
			months = new DateFormatSymbols().getMonths();
			List<VDataTotal> ListKreditperMonth = new ArrayList<>();
			CategoryModel model = new DefaultCategoryModel();

			for (int i = 0; i < months.length - 1; i++) {

				try {
					String bulan1 = StringUtils.leftPad(String.valueOf(i + 1), 2, "0");

					String table = "P_TKREDIT_" + productbyyear + bulan1;
					System.out.println(months);
					bulan.add(months[i]);

					Mtable cekTable = oDao.getTable(table);
					if (cekTable == null) {

						model.setValue("", months[i], 0);

					} else {
						ListKreditperMonth = oDao.getProductDataPerMonth(table);
						for (VDataTotal data : ListKreditperMonth) {
							model.setValue("", months[i], data.getNominal());
						}

					}

				} catch (Exception e) {

					e.printStackTrace();
				}

			}

			Charts chart = new Charts();
			chart.setTitle("TOTAL KREDIT TAHUN :" + productbyyear);
			chart.setSubtitle("");

			chart.getYAxis().getTitle().setText("Nominal (Rp.)");

			chart.setModel(model);

			LinePlotOptions linePlotOptions = chart.getPlotOptions().getLine();
			linePlotOptions.setEnableMouseTracking(false);
			linePlotOptions.getDataLabels().setEnabled(true);

			divCartLineKredit.appendChild(chart);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@NotifyChange("*")
	public void doChartKredit() {
		try {
			divChartKredit.getChildren().clear();
			lastmonth = Calendar.getInstance();
			List<String> bulan = new ArrayList<>();
			productbyyear = lastmonth.get(Calendar.YEAR);
			months = new DateFormatSymbols().getMonths();
			CategoryModel model = new DefaultCategoryModel();
			System.out.println("DATA KREDIT");

			for (int i = 0; i < months.length - 1; i++) {

				try {
					String bulan1 = StringUtils.leftPad(String.valueOf(i + 1), 2, "0");

					String table = "P_TKREDIT_" + productbyyear + bulan1;
					System.out.println(months);
					bulan.add(months[i]);

					Mtable cekTable = oDao.getTable(table);
					if (cekTable == null) {

						model.setValue("KREDIT", months[i], 0);

					} else {
						List<VDataTotal> KreditList = oDao.getProductDataPerMonth(table);
						System.out.println("ADA DATA");
						for (VDataTotal data : KreditList) {
							model.setValue("KREDIT", months[i], data.getNominal());
						}
					}

				} catch (Exception e) {

					e.printStackTrace();
				}

			}
			charts = new Charts();
			charts.setTitle("");
			charts.setType("column");
			charts.setModel(model);

			divChartKredit.appendChild(charts);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@NotifyChange("*")
	public void doChartKreditBankNonBank(String filter) {
		try {
			if (filter.equals("TAGIHAN KEPADA BANK")) {
				divChartKreditKepadaBank.getChildren().clear();
			} else {
				divChartKreditKepadaNonBank.getChildren().clear();
			}

			lastmonth = Calendar.getInstance();
			List<String> bulan = new ArrayList<>();
			productbyyear = lastmonth.get(Calendar.YEAR);
			months = new DateFormatSymbols().getMonths();
			CategoryModel model = new DefaultCategoryModel();
			System.out.println("DATA KREDIT");

			for (int i = 0; i < months.length - 1; i++) {

				try {
					String bulan1 = StringUtils.leftPad(String.valueOf(i + 1), 2, "0");

					String table = "P_TKREDIT_" + productbyyear + bulan1;
					System.out.println(months);
					bulan.add(months[i]);

					Mtable cekTable = oDao.getTable(table);
					if (cekTable == null) {

						model.setValue("KREDIT", months[i], 0);

					} else {

						List<VDataTotal> KreditList = oDao.getKreditKepadaBankNonBank(table, filter);
						System.out.println("ADA DATA");
						for (VDataTotal data : KreditList) {
							model.setValue("KREDIT", months[i], data.getNominal());
						}
					}

				} catch (Exception e) {

					e.printStackTrace();
				}

			}
			charts = new Charts();
			charts.setTitle("");
			charts.setType("column");
			charts.setModel(model);
			charts.getYAxis().getTitle().setText("Nominal (Rp.)");
			if (filter.equals("TAGIHAN KEPADA BANK")) {
				divChartKreditKepadaBank.appendChild(charts);
			} else {
				divChartKreditKepadaNonBank.appendChild(charts);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Command
	@NotifyChange("*")
	public void doSearchDpkYoy() {

		divchartdpkyoy.getChildren().clear();

		System.out.println(year1);
		System.out.println(year2);

		doChartDpkYoy(year1, year2);

	}

	@Command
	@NotifyChange("*")
	public void doSearchKreditYoy() {

		divbanknonbankyoy.getChildren().clear();

		System.out.println(year3);
		System.out.println(year4);

		doChartBankNonBankYoy(year3, year4);

	}

	@Command
	@NotifyChange("*")
	public void doChartDpkYoy(Integer year1, Integer year2) {
		try {
			if (year1 > year2) {
				Messagebox.show("MOHON MASUKAN TAHUN TAHUN SEBELUM LEBIH KECIL DARI TAHUN SESUDAH");
			} else {
				List<V_AllDpk> ListData = new ArrayList<>();
				String table1 = "P_TDPK_" + year1 + "01";
				String table2 = "P_TDPK_" + year2 + "01";
				divchartdpkyoy.getChildren().clear();
				charts = new Charts();
				charts.setTitle("");
				List<String> ListYear = new ArrayList<>();
				ListYear.add(year1.toString());
				ListYear.add(year2.toString());

				List<String> tahun = new ArrayList<>();
				List<BigDecimal> nominal = new ArrayList<>();

				Mtable cekTable1 = oDao.getTable(table1);
				Mtable cekTable2 = oDao.getTable(table2);

				if (cekTable1 != null && cekTable2 != null) {

					ListData = oDao.getProductDpkYOY(table1, table2, year1, year2);

				} else {
					Clients.evalJavaScript(
							"swal.fire({" + "icon: 'warning',\r\n" + " title: 'Data tidak tersedia!',\r\n"
									+ " text: 'Data Periode Bulan Tidak Ditemukan!'," + "})");

				}

				List<String> listKategori = new ArrayList<>();
				List<BigDecimal> listNominal = new ArrayList<>();

				Map<String, List<BigDecimal>> map = new HashMap<>();
				listKategori.add("DEPOSITO");
				listKategori.add("GIRO");
				listKategori.add("LBL");
				listKategori.add("TABUNGAN");

				for (String kategori : listKategori) {
					listNominal = new ArrayList<>();
					for (V_AllDpk data1 : ListData) {
						if (kategori.equals(data1.getKeterangan().trim())) {
							listNominal.add(data1.getNominal());
						}

					}
					map.put(kategori, listNominal);
				}

				for (Entry<String, List<BigDecimal>> entry : map.entrySet()) {
					Series series = new Series();
					series.setName(entry.getKey());
					series.setType("column");
					series.setData(entry.getValue());
					charts.addSeries(series);

				}

				for (Entry<String, List<BigDecimal>> entry : map.entrySet()) {
					Series average = new Series();
					average.setName(entry.getKey());
					average.setType("spline");
					average.setData(entry.getValue());
					Marker marker = average.getMarker();
					marker.setLineWidth(2);
					marker.setLineColor("#000000");
					marker.setFillColor("white");
					charts.addSeries(average);

				}

				charts.getXAxis().setCategories(ListYear);
				charts.getYAxis().getTitle().setText("Nominal (Rp.)");

				divchartdpkyoy.appendChild(charts);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doChartDpkYoyKosong() {
		try {

			charts = new Charts();
			divchartdpkyoy.appendChild(charts);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doChartBankNonBankYoy(Integer year3, Integer year4) {
		try {

			divbanknonbankyoy.getChildren().clear();
			charts = new Charts();
			charts.setTitle("");
			charts.setType("bar");
			String table1 = "P_TKREDIT_" + year3 + "02";
			String table2 = "P_TKREDIT_" + year4 + "02";
			Title title = charts.getTitle();
			title.setAlign("left");
			CategoryModel model = new DefaultCategoryModel();

			Mtable cekTable1 = oDao.getTable(table1);
			Mtable cekTable2 = oDao.getTable(table2);
			List<V_AllDpk> ListData = new ArrayList<>();

			if (cekTable1 != null && cekTable2 != null) {

				ListData = oDao.getProductBankNonBankYoy(table1, table2, year3, year4);

			} else {
				Clients.evalJavaScript("swal.fire({" + "icon: 'warning',\r\n" + " title: 'Data tidak tersedia!',\r\n"
						+ " text: 'Data Periode Bulan Tidak Ditemukan!'," + "})");

			}

			for (V_AllDpk data : ListData) {
				model.setValue(data.getBulan(), data.getKeterangan(), data.getNominal());

			}

			charts.setModel(model);
			charts.getYAxis().setMin(0);
			charts.getYAxis().setTitle("Goals");

			charts.getLegend().setReversed(true);
			charts.getPlotOptions().getSeries().setStacking("normal");
			divbanknonbankyoy.appendChild(charts);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Calendar getLastmonth() {
		return lastmonth;
	}

	public void setLastmonth(Calendar lastmonth) {
		this.lastmonth = lastmonth;
	}

	public int getByyear() {
		return byyear;
	}

	public String getTimekredit() {
		return timekredit;
	}

	public void setTimekredit(String timekredit) {
		this.timekredit = timekredit;
	}

	public void setByyear(int byyear) {
		this.byyear = byyear;
	}

	public int getProductbyyear() {
		return productbyyear;
	}

	public void setProductbyyear(int productbyyear) {
		this.productbyyear = productbyyear;
	}

	public String getPeriode() {
		return periode;
	}

	public void setPeriode(String periode) {
		this.periode = periode;
	}

	public Integer getMonthchart() {
		return monthchart;
	}

	public void setMonthchart(Integer monthchart) {
		this.monthchart = monthchart;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getMonthchart1() {
		return monthchart1;
	}

	public void setMonthchart1(Integer monthchart1) {
		this.monthchart1 = monthchart1;
	}

	public static Number[] getAverage() {
		return new Number[] { 47, 83.33, 70.66, 239.33, 175.66 };
	}

	public Integer getYear1() {
		return year1;
	}

	public void setYear1(Integer year1) {
		this.year1 = year1;
	}

	public Integer getYear2() {
		return year2;
	}

	public void setYear2(Integer year2) {
		this.year2 = year2;
	}

	public Integer getYear3() {
		return year3;
	}

	public void setYear3(Integer year3) {
		this.year3 = year3;
	}

	public Integer getYear4() {
		return year4;
	}

	public void setYear4(Integer year4) {
		this.year4 = year4;
	}

}
