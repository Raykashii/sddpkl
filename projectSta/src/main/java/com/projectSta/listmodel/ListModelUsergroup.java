package com.projectSta.listmodel;

import java.util.List;

import javax.swing.event.ListDataEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import org.zkoss.zul.ext.Sortable;


import com.projectSta.dao.MusergroupDAO;
import com.projectSta.dao.MkelasDAO;
import com.projectSta.domain.Mkelas;
import com.projectSta.domain.Musergroup;
import com.projectSta.model.AbstractPagingListModel;

import net.bytebuddy.asm.Advice.Return;

public class ListModelUsergroup extends AbstractPagingListModel<Musergroup> implements Sortable<Musergroup> {
	
	
	private static final long serialVersionUID = 1L;
	
	private int _size = -1;
	List<Musergroup> oList;
	
	public ListModelUsergroup(int startPageNumber, int pageSize, String filter, String orderby) {
		super(startPageNumber, pageSize, filter, orderby);
	}
	
	@Override
	protected List<Musergroup> getPageData(int itemStartNumber, int pageSize, String filter, String orderby) {
		MusergroupDAO oDao = new MusergroupDAO();
		try {
			oList = oDao.listPaging(itemStartNumber, pageSize, filter, orderby);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return oList;
	}
	
	
	@Override
	public int getTotalSize(String filter) {
		MusergroupDAO oDao = new MusergroupDAO();
		try {
			_size = oDao.pageCount(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _size;
	}
	
	public void sort(Comparator<Musergroup> cmpr, boolean ascending) {
		Collections.sort(oList, cmpr);
		fireEvent(ListDataEvent.CONTENTS_CHANGED, -1, -1);
	}
	
	public String getSortDirection(Comparator<Musergroup> cmpr) {
		return null;
	}
}