package com.projectSta.listmodel;

	import java.util.List;

import javax.swing.event.ListDataEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import org.zkoss.zul.ext.Sortable;



import com.projectSta.dao.MemployeeDAO;
import com.projectSta.domain.Memployee;
import com.projectSta.model.AbstractPagingListModel;

import net.bytebuddy.asm.Advice.Return;

public class ListmodelEmployee extends AbstractPagingListModel<Memployee> implements Sortable<Memployee> {
	
	
	private static final long serialVersionUID = 1L;
	
	private int _size = -1;
	List<Memployee> oList;
	
	public ListmodelEmployee(int startPageNumber, int pageSize, String filter, String orderby) {
		super(startPageNumber, pageSize, filter, orderby);
	}
	
	@Override
	protected List<Memployee> getPageData(int itemStartNumber, int pageSize, String filter, String orderby) {
		MemployeeDAO oDao = new MemployeeDAO();
	    try {
	        oList = oDao.listPaging(itemStartNumber, pageSize, filter, orderby);
	        if (oList == null) {
	            oList = Collections.emptyList();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        oList = Collections.emptyList();
	    }
	    return oList;
	}

	
	
	@Override
	public int getTotalSize(String filter) {
		MemployeeDAO oDao = new MemployeeDAO();
		try {
			_size = oDao.pageCount(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _size;
	}
	
	public void sort(Comparator<Memployee> cmpr, boolean ascending) {
		Collections.sort(oList, cmpr);
		fireEvent(ListDataEvent.CONTENTS_CHANGED, -1, -1);
	}
	
	public String getSortDirection(Comparator<Memployee> cmpr) {
		return null;
	}
}