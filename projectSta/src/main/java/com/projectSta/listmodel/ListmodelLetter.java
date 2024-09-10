package com.projectSta.listmodel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.zkoss.zul.event.ListDataEvent;
import org.zkoss.zul.ext.Sortable;

import com.projectSta.dao.MtletterDAO;
import com.projectSta.domain.Mtletter;
import com.projectSta.model.AbstractPagingListModel;

public class ListmodelLetter extends AbstractPagingListModel<Mtletter> implements Sortable<Mtletter> {
	private static final long serialVersionUID = 1L;

	private int _size = -1;
	private List<Mtletter> oList;

	public ListmodelLetter(int startPageNumber, int pageSize, String filter, String orderby) {
		super(startPageNumber, pageSize, filter, orderby);
	}

	@Override
	protected List<Mtletter> getPageData(int itemStartNumber, int pageSize, String filter, String orderby) {
		MtletterDAO oDao = new MtletterDAO();
		try {
			oList = oDao.listPaging(itemStartNumber, pageSize, filter, orderby);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oList;
	}

	@Override
	public int getTotalSize(String filter) {
		MtletterDAO oDao = new MtletterDAO();
		try {
			_size = oDao.pageCount(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _size;
	}

	public void sort(Comparator<Mtletter> cmpr, boolean ascending) {
		Collections.sort(oList, cmpr);
		fireEvent(ListDataEvent.CONTENTS_CHANGED, -1, -1);

	}

	public String getSortDirection(Comparator<Mtletter> cmpr) {
		return null;
	}
}
