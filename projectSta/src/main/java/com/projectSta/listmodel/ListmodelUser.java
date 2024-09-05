package com.projectSta.listmodel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.zkoss.zul.event.ListDataEvent;
import org.zkoss.zul.ext.Sortable;

import com.projectSta.dao.MuserDAO;
import com.projectSta.domain.Muser;
import com.projectSta.model.AbstractPagingListModel;

public class ListmodelUser extends AbstractPagingListModel<Muser> implements Sortable<Muser> {
	private static final long serialVersionUID = 1L;

	private int _size = -1;
	private List<Muser> oList;

	public ListmodelUser(int startPageNumber, int pageSize, String filter, String orderby) {
		super(startPageNumber, pageSize, filter, orderby);
	}

	@Override
	protected List<Muser> getPageData(int itemStartNumber, int pageSize, String filter, String orderby) {
		MuserDAO oDao = new MuserDAO();
		try {
			oList = oDao.listPaging(itemStartNumber, pageSize, filter, orderby);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oList;
	}

	@Override
	public int getTotalSize(String filter) {
		MuserDAO oDao = new MuserDAO();
		try {
			_size = oDao.pageCount(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _size;
	}

	public void sort(Comparator<Muser> cmpr, boolean ascending) {
		Collections.sort(oList, cmpr);
		fireEvent(ListDataEvent.CONTENTS_CHANGED, -1, -1);

	}

	public String getSortDirection(Comparator<Muser> cmpr) {
		return null;
	}
}
