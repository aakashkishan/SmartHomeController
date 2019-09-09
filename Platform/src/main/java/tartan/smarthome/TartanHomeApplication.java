package tartan.smarthome;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import tartan.report.core.ReportData;
import tartan.report.db.ReportDAO;
import tartan.report.resources.ReportResource;
import tartan.report.enums.ReportType;
import tartan.report.resources.SubResource;
import tartan.smarthome.auth.TartanAuthenticator;
import tartan.smarthome.auth.TartanUser;
import tartan.report.core.SubscriptionData;
import tartan.smarthome.core.TartanHomeData;
import tartan.smarthome.core.TartanHouseData;
import tartan.smarthome.db.HomeDAO;
import tartan.smarthome.db.HouseDAO;
import tartan.report.db.SubscriptionDAO;
import tartan.smarthome.db.UserDAO;
import tartan.smarthome.resources.TartanResource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This is the driver for the program.
 * @see <a href="https://www.dropwizard.io/1.0.0/docs/manual/core.html#application">Dropwizard Applications</a>
 */
public class TartanHomeApplication extends Application<TartanHomeConfiguration> {

    private final HibernateBundle<TartanHomeConfiguration> hibernateBundle =
            new HibernateBundle<TartanHomeConfiguration>(TartanUser.class, TartanHouseData.class,TartanHomeData.class, SubscriptionData.class, ReportData.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(TartanHomeConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    /**
     * The driver
     * @param args command line arguments
     * @throws Exception a catch all exception
     */
    public static void main(final String[] args) throws Exception {
        new TartanHomeApplication().run(args);
    }

    /**
     * Get the application name. This is the core URL for the system
     * @return the name
     */
    @Override
    public String getName() {
        return "smarthome";
    }

    /**
     * Initialize the system
     * @param bootstrap the initial settings from from the YAML file
     */
    @Override
    public void initialize(final Bootstrap<TartanHomeConfiguration> bootstrap) {
        // We need the view bundle for rendering
        bootstrap.addBundle(new ViewBundle());
        bootstrap.addBundle(hibernateBundle);
    }

    /**
     * Run the system.
     * @param configuration system settings
     * @param environment system environment
     */
    @Override
    public void run(final TartanHomeConfiguration configuration,
                    final Environment environment) {
        HomeDAO homeDAO = new HomeDAO(hibernateBundle.getSessionFactory());
        UserDAO userDAO = new UserDAO(hibernateBundle.getSessionFactory());
        HouseDAO houseDAO = new HouseDAO(hibernateBundle.getSessionFactory());
        SubscriptionDAO subscriptionDAO = new SubscriptionDAO(hibernateBundle.getSessionFactory());
        ReportDAO reportDAO = new ReportDAO(hibernateBundle.getSessionFactory());
        reportDAO.create(new ReportData(userDAO.getUsers().get(0), ReportType.ALL, new Date(), "xxx", 5, 24, 50, 5, 26, 45, 411));

        List<TartanHouseData> houses = new ArrayList<TartanHouseData>();
        for (TartanUser user: userDAO.getUsers()){
             houses.addAll(houseDAO.queryHousesByUserID(user.getId()));
        }

        TartanAuthenticator auth = new TartanAuthenticator(userDAO,houseDAO);

        final TartanResource resource = new TartanResource(houses,
                homeDAO, Integer.parseInt(configuration.getHistoryTimer()));
        final ReportResource reportResource = new ReportResource(reportDAO);
        final SubResource subResource = new SubResource(subscriptionDAO);

        environment.jersey().register(reportResource);
        environment.jersey().register(resource);
        environment.jersey().register(subResource);
        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<TartanUser>()
                .setAuthenticator(auth)
                .buildAuthFilter()));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(TartanUser.class));

    }
}
