package tartan.smarthome.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import tartan.smarthome.auth.TartanUser;
import tartan.smarthome.core.TartanHouseData;

import java.util.ArrayList;
import java.util.List;

public class HouseDAO extends AbstractDAO<TartanHouseData> {
    // Keep a reference to the session
    private SessionFactory factory = null;

    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public HouseDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.factory = sessionFactory;
    }

    /**
     * Save the taratn home data to the database
     * @param
     */
    public void create(TartanHouseData tartanHouseData) {
        try {
            // There is a dropwizard way to establish a Hibernate session outside of Jersey
            // but this is more reliable
            Session session = factory.openSession();
            session.beginTransaction();
            session.save(tartanHouseData);
            session.getTransaction().commit();
            session.close();
        } catch (SessionException sx) {/* Nothing to do */ }
    }

    public List<TartanHouseData> queryHousesByUserID(long userId) {
        try {
            Session session = factory.openSession();
            Query q = session.createQuery("FROM TartanHouseData where user_id = "+userId);
            List<TartanHouseData> result = q.list();
            session.close();
            return result;
        }catch (SessionException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
