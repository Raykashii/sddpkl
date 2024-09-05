package com.projectSta.listmodel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.zkoss.zul.event.ListDataEvent;
import org.zkoss.zul.ext.Sortable;

import com.projectSta.dao.MsiswaDAO;
import com.projectSta.domain.Msiswa;
import com.projectSta.model.AbstractPagingListModel;

public class ListmodelSiswa extends AbstractPagingListModel<Msiswa> implements Sortable<Msiswa> {
	private static final long serialVersionUID = 1L;

	private int _size = -1;
	private List<Msiswa> oList;

	public ListmodelSiswa(int startPageNumber, int pageSize, String filter, String orderby) {
		super(startPageNumber, pageSize, filter, orderby);
	}

	@Override
	protected List<Msiswa> getPageData(int itemStartNumber, int pageSize, String filter, String orderby) {
		MsiswaDAO oDao = new MsiswaDAO();
		try {
			oList = oDao.listPaging(itemStartNumber, pageSize, filter, orderby);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oList;
	}

	@Override
	public int getTotalSize(String filter) {
		MsiswaDAO oDao = new MsiswaDAO();
		try {
			_size = oDao.pageCount(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _size;
	}

	public void sort(Comparator<Msiswa> cmpr, boolean ascending) {
		Collections.sort(oList, cmpr);
		fireEvent(ListDataEvent.CONTENTS_CHANGED, -1, -1);

	}

	public String getSortDirection(Comparator<Msiswa> cmpr) {
		return null;
	}
}
