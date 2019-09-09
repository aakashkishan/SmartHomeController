package tartan.smarthome.db;

import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import tartan.smarthome.auth.TartanUser;

import java.util.ArrayList;
import java.util.List;

import java.util.List;

public class UserDAO extends AbstractDAO<TartanUser> {

    // Keep a reference to the session
    private SessionFactory factory = null;

    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.factory = sessionFactory;
    }

    public TartanUser query(String userName){
        try{
            Session session = factory.openSession();
            Query query = session.createQuery("FROM TartanUser where userName='"+userName+"'");
            List<TartanUser> list =  query.list();
            TartanUser result= null;
            if (list != null && list.size() >0){
                 result = list.get(0);
            }
            session.close();
            return result;
        }catch (SessionException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<TartanUser> getUsers() {
        try {
            Session session = factory.openSession();
            Criteria criteria = session.createCriteria(TartanUser.class);
            List<TartanUser> result= list(criteria);
            session.close();
            return result;
        }catch (SessionException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

//    public TartanUser query(String username){
//        TartanUser user = null;
//        try {
//            Session session = factory.openSession();
//            Query q = session.createQuery("select * from User where user_name = '"+username+"'");
//            List<TartanUser> list = q.list();
//            if (list != null && list.size() > 0){
//                user = list.get(0);
//            }
//            return user;
//        }catch (SessionException e){
//            /* Nothing to do */
//        }
//        return user;
//    }
//
//    public List<TartanUser> users(){
//        List<TartanUser> result = null;
//        try {
//            // There is a dropwizard way to establish a Hibernate session outside of Jersey
//            // but this is more reliable
//            Session session = factory.openSession();
//            result = session.createCriteria(TartanUser.class).list();
//            session.close();
//            return result;
//
//        } catch (SessionException sx) {/* Nothing to do */ }
//        return result;
//    }
}
