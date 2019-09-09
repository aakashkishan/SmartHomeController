package tartan.report.db;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.SessionFactory;
import tartan.report.core.SubscriptionData;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionDAO extends AbstractDAO<SubscriptionData> {
    // Keep a reference to the session
    private SessionFactory factory;

    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public SubscriptionDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.factory = sessionFactory;
    }

    /**
     * Save the report data to the database
     */
    public void create(SubscriptionData subData) {
        try {
            // There is a dropwizard way to establish a Hibernate session outside of Jersey
            // but this is more reliable
            Session session = factory.openSession();
            session.beginTransaction();
            session.save(subData);
            session.getTransaction().commit();
            session.close();
        } catch (SessionException sx) {/* Nothing to do */ }
    }

    public List<SubscriptionData> querySubscriptionByUserID(long userId) {
        try {
            Session session = factory.openSession();
            Query q = session.createQuery("FROM SubscriptionData where user_id = " + userId);
            List<SubscriptionData> result = q.list();
            session.close();
            return result;
        } catch (SessionException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    public boolean deleteByUserID(long userId) {
        try {
            Session session = factory.openSession();
            session.beginTransaction();

            String hql = "DELETE FROM SubscriptionData where user_id = :userId";
            Query query = session.createQuery(hql);
            query.setParameter("userId", userId);
            int result = query.executeUpdate();
            session.getTransaction().commit();
            session.close();
            return true;
        } catch (SessionException e) {
            e.printStackTrace();
        }
        return false;
    }
}