package com.projectSta.viewmodel;

import java.util.Date;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.util.Clients;
import com.projectSta.dao.MtletterDAO;
import com.projectSta.domain.Mtletter;
import com.projectSta.utils.db.StoreHibernateUtil;

public class SppdFormVM {

    private Mtletter mtletter = new Mtletter(); // Inisialisasi objek untuk form
    private boolean isInsert = true; // Menandakan mode insert atau update

    public Mtletter getMtletter() {
        return mtletter;
    }

    public void setMtletter(Mtletter mtletter) {
        this.mtletter = mtletter;
    }

    @Command
    @NotifyChange("mtletter")
    public void submit() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = StoreHibernateUtil.openSession();
            transaction = session.beginTransaction();
            MtletterDAO mtletterDAO = new MtletterDAO();

            if (isInsert) {
                mtletter.setTgl_berangkat(new Date());
            }

            // Simpan atau perbarui data
            mtletterDAO.save(session, mtletter);
            transaction.commit();

            // Notifikasi berhasil
            Clients.showNotification("Data saved successfully", Clients.NOTIFICATION_TYPE_INFO, null, "top_center", 3000);
            doReset(); // Bersihkan form setelah submit

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            Clients.showNotification("Error: " + e.getMessage(), Clients.NOTIFICATION_TYPE_ERROR, null, "top_center", 3000);
        } finally {
            if (session != null) session.close();
        }
    }

    @Command
    @NotifyChange("mtletter")
    public void reset() {
        doReset();
    }

    public void doReset() {
        mtletter = new Mtletter(); // Mengosongkan semua field
        isInsert = true; // Kembali ke mode insert
    }
}
