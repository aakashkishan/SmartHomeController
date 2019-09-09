package tartan.report.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.checkerframework.checker.formatter.qual.Format;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.tainting.qual.Tainted;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.SessionFactory;
import tartan.report.core.ReportData;
import tartan.report.enums.ReportType;

import org.checkerframework.checker.tainting.qual.Untainted;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.checkerframework.checker.formatter.qual.ConversionCategory.GENERAL;
import static org.checkerframework.checker.formatter.qual.ConversionCategory.INT;

public class ReportDAO extends AbstractDAO<ReportData> {
    // Keep a reference to the session
    private SessionFactory factory;

    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public ReportDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.factory = sessionFactory;
    }

    /**
     * Save the report data to the database
     */
    public void create(ReportData reportData) {
        try {
            // There is a dropwizard way to establish a Hibernate session outside of Jersey
            // but this is more reliable
            Session session = factory.openSession();
            session.beginTransaction();
            session.save(reportData);
            session.getTransaction().commit();
            session.close();
        } catch (SessionException sx) {/* Nothing to do */ }
    }

    public List<ReportData> queryReportByUserID_Date_Type(long userId, Date date, ReportType type) {
        try {
            Session session = factory.openSession();
            Query q = session.createQuery("FROM ReportData where user_id = "+userId +
                    " and create_time= :date and type= :type");
            q.setParameter("date", date);
            q.setParameter("type", type);
            List<ReportData> result = q.list();
            session.close();
            return result;
        }catch (SessionException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<ReportData> searchReportByUserID_Filename(long userId, @Tainted String filename) {
        try {
            Session session = factory.openSession();
            String hql = constructSearchHQL("FROM ReportData where user_id = %d  and report_name = '%s'",userId,filename);
            Query q = session.createQuery(hql);
            List<ReportData> result = q.list();
            session.close();
            return result;
        } catch (SessionException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private String constructSearchHQL(@Format({INT,GENERAL}) String template, long userId, String filename){
        return String.format(template,userId,filename);
    }

    public List<ReportData> searchReportByUserID_Date(long userId, Date date) {
        try {
            Session session = factory.openSession();
            String hql = "FROM ReportData where user_id = :userId and create_date = :date";
            Query q = session.createQuery(hql);
            q.setParameter("userId", userId);
            q.setParameter("date",date);
            List<ReportData> result = q.list();
            session.close();
            return result;
        } catch (SessionException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public boolean deleteByReportID(long report_id) {
        try {
            Session session = factory.openSession();
            @Nullable ReportData reportData = session.load(ReportData.class, report_id);
            if (reportData != null) {
                session.delete(reportData);
                session.flush();
                session.close();
            }
            return true;
        } catch (SessionException e) {
            e.printStackTrace();
        }
        return false;
    }
}
