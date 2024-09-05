package com.projectSta.model;

public class ChangepasswordModel {

	private String currentpass;
	private String newpass;
	private String newpassconfirm;

	public String getCurrentpass() {
		return currentpass;
	}

	public void setCurrentpass(String currentpass) {
		this.currentpass = currentpass;
	}

	public String getNewpass() {
		return newpass;
	}

	public void setNewpass(String newpass) {
		this.newpass = newpass;
	}

	public String getNewpassconfirm() {
		return newpassconfirm;
	}

	public void setNewpassconfirm(String newpassconfirm) {
		this.newpassconfirm = newpassconfirm;
	}
	
}
