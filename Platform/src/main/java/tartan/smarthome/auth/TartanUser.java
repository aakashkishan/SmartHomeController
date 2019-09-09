package tartan.smarthome.auth;

import org.checkerframework.checker.tainting.qual.Untainted;
import tartan.smarthome.core.TartanHouseData;

import javax.persistence.*;
import java.security.Principal;
import java.util.List;

/**
 * Simple class to represent an Authenticated users
 */
@Entity
@Table(name = "User")
public class TartanUser implements Principal {
    // Users are identified by name and house

    // Primary key for the table
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "user_name", nullable = false)
    private String userName=null;

    @Column(name = "password", nullable = false)
    private String password = null;

    @Transient
    private List<TartanHouseData> houses;


    public TartanUser(){}

    /**
     * Create a new TartanUser. Note that this is the only place to set name and house
     * @param name The user name
     */
    public TartanUser(long id, String name, String passwd) {
        this.id = id;
        this.userName = name;
        this.password = passwd;
    }

    public long getId() {
        return id;
    }

    public void setId(long id){
        this.id = id;
    }


    public @Untainted String getIDString(){
        return ""+id;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public List<TartanHouseData> getHouses() {
        return houses;
    }

    public void setHouses(List<TartanHouseData> houses) {
        this.houses = houses;
    }

    @Override
    public String getName() {
        return this.userName+"@"+this.id;
    }

    public long getFeature() {
        if(this.id % 2 == 1)
            return 1;
        else
            return 2;
    }
}
