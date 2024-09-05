package com.projectSta.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

public class SysUtils {

	public static final int PAGESIZE = 5;
	public static final int MODELSIZE = 1000;
	public static final int IDPROCS_INSERT = 0;
	public static final int IDPROCS_UPDATE = 1;
	public static final int IDPROCS_DELETE = 2;
	public static final int IDPROCS_VIEW = 3;
	public static final String USERS_PASSWORD_DEFAULT = "123456";

	public static final String PATH_FILES_ROOT = "/files";
	public static final String PATH_DATA = "/fromtaspen/";
	public static final String JASPER_PATH = "/jasper";
	public static final String REPORT_PATH = "/report";
	public static final String PATH_TOOLS = "/themes/tools/";

	public static final String FILEHOST_AUTHENTICATION_PASSWORD_LABEL = "PASSWORD";
	public static final String FILEHOST_AUTHENTICATION_PASSWORD_VALUE = "P";
	public static final String FILEHOST_AUTHENTICATION_KEY_LABEL = "KEY FILE";
	public static final String FILEHOST_AUTHENTICATION_KEY_VALUE = "K";

	public static final String STATUS_DOWNLOAD_APPROVAL = "Data Approval";
	public static final String STATUS_REJECTED = "Rejected";
	public static final String STATUS_BLOKIR_REQUEST = "Wait Approval";
	public static final String STATUS_BLOKIR_APPROVED = "Approved";
	public static final String STATUS_BLOKIR_APPROVED_BO = "Approved Back Office";

	public static String token = "";
	public static final String BASE_URL = "https://posindonesia.co.id";
	public static final String URL_PORT = "https://api.posindonesia.co.id:8245";
	public static final String URL_PORT_PATH = "https://api.posindonesia.co.id:8245/utility/1.0.0";
	public static final int PORT = 8245;
	public static final String METHOD_TOKEN = "/token";
	public static final String METHOD_GETFEE = "/getFee";
	public static final String METHOD_GETTRACK = "/getTrackAndTrace";
	public static final String METHOD_GETDETAIL = "/getTrackAndTraceDetail";
	public static final String METHOD_GETSTATUS = "/getTrackAndTraceLastStatus";
	public static final String METHOD_GETTRACK_LN = "/getTrackAndTraceLn";

	public static final String CONSUMER_KEY = "NllJYnRlemZnZ0l2NFdPaXgzdElKWjRoN3BFYTpxY2VETGtreFMyZHJ1a3k0T0YwemdCR2hOZUFh";

	public static String encryptionCommand(String text) throws NoSuchAlgorithmException, Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte byteData[] = md5.digest(text.getBytes());

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

	public static void main(String stArgs[]) {
		try {
			System.out.println("pass : " + encryptionCommand("a"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String nullHandler(String text) {
		String returns = null;
		if (text == null)
			returns = "-";
		else if (text.trim().length() == 0)
			returns = "-";
		else
			returns = text.trim();
		return returns;
	}

	@SuppressWarnings("null")
	public static String thousandFormat(Object value) {
		String returns = "";
		if (value != null || value.toString().trim().length() > 0)
			returns = new DecimalFormat("#,###").format(Double.valueOf(value.toString()));
		return returns;
	}

}
