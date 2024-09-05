package com.projectSta.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.zkoss.zul.AbstractListModel;

@SuppressWarnings("rawtypes")
public abstract class AbstractPagingListModel<T> extends AbstractListModel {
	
	private static final long serialVersionUID = 6613208067174831719L;
	
	private int startPageNumber;
	private int pageSize;
	private int itemStartNumber;
	private Session session;
	
	private List<T> items = new ArrayList<T>();

	public AbstractPagingListModel(int startPageNumber, int pageSize, String filter, String orderby) {
		super();

		this.startPageNumber = startPageNumber;
		this.pageSize = pageSize;
		this.itemStartNumber = startPageNumber * pageSize;

		items = getPageData(itemStartNumber, pageSize, filter, orderby);
	}

	public abstract int getTotalSize(String filter);

	protected abstract List<T> getPageData(int itemStartNumber, int pageSize, String filter, String orderby);

	
	public Object getElementAt(int index) {
		return items.get(index);
	}
	
	public int getSize() {
		return items.size();
	}
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public int getStartPageNumber() {
		return startPageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getItemStartNumber() {
		return itemStartNumber;
	}

	public List<T> getItems() {
		return items;
	}

}
