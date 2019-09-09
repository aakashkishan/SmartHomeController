package tartan.smarthome.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.util.Pair;
import tartan.smarthome.TartanHomeConfiguration;
import tartan.smarthome.TartanHomeSettings;
import tartan.smarthome.db.HouseDAO;
import tartan.smarthome.db.UserDAO;

/***
 * Authentication class for the Tartan Home System. A simple username and password is required
 */
public class TartanAuthenticator implements Authenticator<BasicCredentials, TartanUser> {
    private UserDAO userDAO;
    private HouseDAO houseDAO;

    /**
     * Empty constructor
     */
    public TartanAuthenticator(UserDAO userDAO,HouseDAO houseDAO) {
        this.userDAO = userDAO;
        this.houseDAO = houseDAO;
    }


    /**
     * Authenticate the user
     * @param credentials the u∂ser login information
     * @return the authenticated user on sucess
     * @throws AuthenticationException failed authentication
     */
    @Override
    public Optional<TartanUser> authenticate(BasicCredentials credentials) throws AuthenticationException {

        TartanUser tartanUser = userDAO.query(credentials.getUsername());
        if (tartanUser != null && tartanUser.getPassword().equals(credentials.getPassword())){
            tartanUser.setHouses(houseDAO.queryHousesByUserID(tartanUser.getId()));
            return Optional.of(tartanUser);
        }
        return Optional.empty();
    }
}
