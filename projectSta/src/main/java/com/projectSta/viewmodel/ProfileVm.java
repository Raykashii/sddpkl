package com.projectSta.viewmodel;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Window;

import com.projectSta.domain.Muser;

public class ProfileVm {

	private Muser obj;
	
	private Window win;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Muser obj)
			throws Exception {
		Selectors.wireComponents(view, this, false);
		this.obj=obj;

	}

	public Muser getObj() {
		return obj;
	}

	public void setObj(Muser obj) {
		this.obj = obj;
	}

}
