package tartan.smarthome.core;


import tartan.smarthome.auth.TartanUser;

import javax.persistence.*;

/**
 * Represents a database table for house information
 * REMIND the house is for the identical house info, whereas the home is for the real-time home data
 */
@Entity
@Table(name = "House")
public class TartanHouseData {

    // Primary key for the table
    @Id
    @Column(name = "house_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long houseID;

    @Column(name = "home_name", nullable = false)
    private String homeName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private TartanUser user;

    @Column(name="address")
    private String address;

    @Column(name="port")
    private int port;

    @Column(name = "target_temp")
    private String targetTemp;

    @Column(name = "alarm_delay")
    private String alarmDelay;

    @Column(name = "alarm_passcode")
    private String alarmPasscode;


    public TartanHouseData(){}

    /**
     * Create a new data set from a TartanHouse model
     * @param user the id of the user that owns the home
     * @param houseID the id of house
     * @param homeName the name of house
     */
    public TartanHouseData(long houseID, String homeName, TartanUser user) {
        this.houseID = houseID;
        this.homeName = homeName;
        this.user = user;
    }

    public long getHouseID() {
        return houseID;
    }

    public String getHomeName() {
        return homeName;
    }

    public TartanUser getUser() {
        return user;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getTargetTemp() {
        return targetTemp;
    }

    public String getAlarmDelay() {
        return alarmDelay;
    }

    public String getAlarmPasscode() {
        return alarmPasscode;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
