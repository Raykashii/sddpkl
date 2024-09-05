package com.projectSta.listmodel;

	import java.util.List;

import javax.swing.event.ListDataEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import org.zkoss.zul.ext.Sortable;



import com.projectSta.dao.MkelasDAO;
import com.projectSta.domain.Mkelas;
import com.projectSta.model.AbstractPagingListModel;

import net.bytebuddy.asm.Advice.Return;

public class ListModelKelas extends AbstractPagingListModel<Mkelas> implements Sortable<Mkelas> {
	
	
	private static final long serialVersionUID = 1L;
	
	private int _size = -1;
	List<Mkelas> oList;
	
	public ListModelKelas(int startPageNumber, int pageSize, String filter, String orderby) {
		super(startPageNumber, pageSize, filter, orderby);
	}
	
	@Override
	protected List<Mkelas> getPageData(int itemStartNumber, int pageSize, String filter, String orderby) {
	    MkelasDAO oDao = new MkelasDAO();
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
		MkelasDAO oDao = new MkelasDAO();
		try {
			_size = oDao.pageCount(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _size;
	}
	
	public void sort(Comparator<Mkelas> cmpr, boolean ascending) {
		Collections.sort(oList, cmpr);
		fireEvent(ListDataEvent.CONTENTS_CHANGED, -1, -1);
	}
	
	public String getSortDirection(Comparator<Mkelas> cmpr) {
		return null;
	}
}