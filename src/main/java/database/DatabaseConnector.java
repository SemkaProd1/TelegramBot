package database;

import com.sun.deploy.util.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

public class DatabaseConnector {

    private static SessionFactory sessionFactory;


    static {
        try {
            java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
            System.out.println("Hibernate: started");
            //Configuration configuration = new Configuration().configure();
            StandardServiceRegistry serviceRegistry = new
                    StandardServiceRegistryBuilder().configure("hibernate.cfg.xml")
                    .build();

            Metadata metadata = new MetadataSources(serviceRegistry)
                    .getMetadataBuilder().build();
            sessionFactory = metadata.getSessionFactoryBuilder().build();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public User getUserByUserName(String name) {

        List users = getSessionFactory().openSession().createQuery(" from database.User WHERE userName =:ParamName ")
                .setParameter("ParamName", name)
                .list();
        if (users == null) {
            return null;
        } else {
            return (User) users.get(0);
        }
    }

    public String getInfo(String userName) {
        List sa = getAllSubUsers(userName);
        List results = new LinkedList();
        for (Object aSa : sa) {
            results.add("\uD83D\uDE0E <a href='tg://user?id=" + getParamByUserName(String.valueOf(aSa), "userId") + "'>" + aSa + "</a> \n");
        }
        return StringUtils.join(results, "");
    }

    public Boolean ifUserExists(int userId) {
        Query query = getSessionFactory().openSession().createQuery("select userId from database.User where userId = :id");
        query.setParameter("id", userId);

        return (query.uniqueResult() != null);
    }
    public Boolean ifUserExists(String authorname) {
        Query query = getSessionFactory().openSession().createQuery("select authorName from database.User where authorName = :authorN");
        query.setParameter("authorN", authorname);

        return (query.uniqueResult() != null);
    }

    private List getAllSubUsers(String username) {
        Query query = getSessionFactory().openSession().
                createQuery("select userName from database.User where  authorName= :authorN");
        query.setParameter("authorN", username);

        return query.list();
    }

    public Object getParamByUserName(String name, String parameter) {

        List tokens = getSessionFactory().openSession().createQuery("select " + parameter + " from database.User WHERE userName =:userN ")
                .setMaxResults(1)
                .setParameter("userN", name)
                .list();
        return tokens.get(0);
    }

    public Object getParamByUserId(int id1, String parameter) {

        List tokens = getSessionFactory().openSession().createQuery("select " + parameter + " from database.User WHERE userId =:userI ")
                .setMaxResults(1)
                .setParameter("userI", id1)
                .list();

        return tokens.get(0);

    }


    public int getNumberOfPinns(String userName) {
        List pins = getSessionFactory().openSession().createQuery("select messageId from database.Pinned WHERE userName=:userN")
                .setParameter("userN", userName)
                .list();
        return pins.size();
    }

    public Boolean pinExists(String text) {
        Query query = getSessionFactory().openSession().createQuery("select textMessage from database.Pinned where textMessage= :id");
        query.setParameter("id", text);
        return (query.uniqueResult() != null);
    }

    public void deletePin(String text, String username) {
        Session session = getSessionFactory().openSession();
        session.getTransaction().begin();
        String hql = "delete from database.Pinned where textMessage =: text AND userName =: username";
        session.createQuery(hql).setParameter("text", text).setParameter("username", username).executeUpdate();
        session.getTransaction().commit();
    }

    public void insert(User user) {
        Session session =  getSessionFactory().openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
    }

    public void insert(Pinned pinned) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        System.out.println(pinned);
        session.save(pinned);
        session.getTransaction().commit();
    }

    public List getAllPinsByUserName(String name) {

        List pins =  getSessionFactory().openSession().createQuery(" from database.Pinned WHERE userName =:ParamName ")
                .setParameter("ParamName", name)
                .list();
        if (pins == null) {
            return null;
        } else {
            return pins;
        }
    }

}
