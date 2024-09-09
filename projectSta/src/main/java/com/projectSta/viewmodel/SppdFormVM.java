package com.projectSta.viewmodel;

import java.util.Date;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;

import com.projectSta.dao.MtletterDAO;
import com.projectSta.domain.Mtletter;
import com.projectSta.dao.MemployeeDAO;
import com.projectSta.domain.Memployee;
import com.projectSta.utils.db.StoreHibernateUtil;

public class SppdFormVM {

    private Mtletter mtletter = new Mtletter(); // Inisialisasi objek untuk form
    private boolean isInsert = true; // Menandakan mode insert atau update

    @Wire
    private Combobox cbEmployeegroup;

    private Mtletter obj;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view,
                             @ExecutionArgParam("obj") Mtletter obj) {
        Selectors.wireComponents(view, this, false);
        doReset();

        if (obj != null) {
            this.obj = obj;
            isInsert = false;
            cbEmployeegroup.setValue(obj.getMemployeefk().getNama());
        }
    }

    @Command
    @NotifyChange("mtletter")
    public void submit() {
        if (!validateForm()) {
            return; // If validation fails, don't proceed
        }

        Session session = null;
        Transaction transaction = null;
        try {
            session = StoreHibernateUtil.openSession();
            transaction = session.beginTransaction();
            MtletterDAO mtletterDAO = new MtletterDAO();

            // Assign selected employee from combobox to mtletter
            Memployee selectedEmployee = cbEmployeegroup.getSelectedItem().getValue();
            mtletter.setMemployeefk(selectedEmployee); // Ensure MemployeeFK is set

            if (isInsert && mtletter.getTgl_berangkat() == null) {
                mtletter.setTgl_berangkat(new Date());
            }

            // Save or update data
            mtletterDAO.save(session, mtletter);
            transaction.commit();

            // Notification for success
            Clients.showNotification("Data saved successfully", Clients.NOTIFICATION_TYPE_INFO, null, "top_center", 3000);
            doReset(); // Reset form after successful submit

            // Trigger print function with data
            String printContent = generatePrintContent(mtletter); // Generate printable content
            printContent = printContent.replace("'", "\\'"); // Escape single quotes for JavaScript
            Clients.evalJavaScript("showPrintPreview('" + printContent + "');");

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackException) {
                    rollbackException.printStackTrace();
                }
            }
            e.printStackTrace();
            Clients.showNotification("Error: " + e.getMessage(), Clients.NOTIFICATION_TYPE_ERROR, null, "top_center", 3000);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    private String generatePrintContent(Mtletter mtletter) {
        StringBuilder content = new StringBuilder();
        content.append("<html><head><title>Print Preview</title></head><body>");
        content.append("<h1 style='text-align:center;'>SPPD Letter</h1>");
        content.append("<p><strong>Tanggal Berangkat:</strong> " + mtletter.getTgl_berangkat() + "</p>");
        content.append("<p><strong>Tanggal Pulang:</strong> " + mtletter.getTgl_pulang() + "</p>");
        content.append("<p><strong>Tujuan:</strong> " + mtletter.getTujuan() + "</p>");
        content.append("<p><strong>Keterangan:</strong> " + mtletter.getKeterangan() + "</p>");
        content.append("</body></html>");
        
        return content.toString().replace("'", "\\'"); // Escape single quotes for JavaScript
    }



    @Command
    @NotifyChange("mtletter")
    public void reset() {
        doReset();
    }

    public ListModelList<Memployee> getEmploymodel() {
        ListModelList<Memployee> lm = null;
        try {
            lm = new ListModelList<Memployee>(new MemployeeDAO().listByFilter("0=0", "nama"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lm;
    }

    public void doReset() {
        mtletter = new Mtletter(); // Mengosongkan semua field
        isInsert = true; // Kembali ke mode insert
        obj = new Mtletter();
        cbEmployeegroup.setValue(null);
    }

    private boolean validateForm() {
        if (mtletter.getTgl_berangkat() == null) {
            Clients.showNotification("Tanggal Berangkat harus diisi", Clients.NOTIFICATION_TYPE_WARNING, null, "top_center", 3000);
            return false;
        }
        if (mtletter.getTgl_pulang() == null) {
            Clients.showNotification("Tanggal Pulang harus diisi", Clients.NOTIFICATION_TYPE_WARNING, null, "top_center", 3000);
            return false;
        }
        if (mtletter.getTujuan() == null || mtletter.getTujuan().trim().isEmpty()) {
            Clients.showNotification("Tujuan harus diisi", Clients.NOTIFICATION_TYPE_WARNING, null, "top_center", 3000);
            return false;
        }
        if (mtletter.getKeterangan() == null || mtletter.getKeterangan().trim().isEmpty()) {
            Clients.showNotification("Keterangan harus diisi", Clients.NOTIFICATION_TYPE_WARNING, null, "top_center", 3000);
            return false;
        }
        return true;
    }

    public Mtletter getMtletter() {
        return mtletter;
    }

    public void setMtletter(Mtletter mtletter) {
        this.mtletter = mtletter;
    }

    public Combobox getCbEmployeegroup() {
        return cbEmployeegroup;
    }

    public void setCbEmployeegroup(Combobox cbEmployeegroup) {
        this.cbEmployeegroup = cbEmployeegroup;
    }

    public Mtletter getObj() {
        return obj;
    }

    public void setObj(Mtletter obj) {
        this.obj = obj;
    }
}
