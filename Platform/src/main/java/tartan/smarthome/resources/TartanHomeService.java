package tartan.smarthome.resources;

import edu.cmu.iot.IoTControlManager;
import edu.cmu.iot.IoTValues;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tartan.smarthome.TartanHomeSettings;
import tartan.smarthome.core.TartanHome;
import tartan.smarthome.core.TartanHomeData;
import tartan.smarthome.core.TartanHomeValues;
import tartan.smarthome.core.TartanHouseData;
import tartan.smarthome.db.HomeDAO;

import java.time.LocalTime;
import java.util.Hashtable;
import java.util.Set;

/***
 * The service layer for the Tartan Home System. Additional inputs and control mechanisms should be accessed here.
 * Currently, this is mainly a proxy to make the existing hardware RESTful.
 */
public class TartanHomeService {

    // the controller for the house
    private IoTControlManager controller;

    // a logging system
    private static final Logger LOGGER = LoggerFactory.getLogger(TartanHomeService.class);

    // Home configuration parameters
    private String name;
    private long houseID;
    private String address;
    private Integer port;
    private String alarmDelay;
    private String alarmPasscode;
    private String targetTemp;
    private String user;
    private String password;

    // status parameters
    private HomeDAO homeDAO;
    private boolean authenticated;

    // historian parameters
    private Boolean logHistory;
    private int historyTimer = 2000;

    /**
     * Create a new Tartan Home Service
     * @param dao handle to a database
     */
    public TartanHomeService(HomeDAO dao) {
        this.homeDAO = dao;
    }

    /**
     * Initialize the settings
     * @param house the house settings
     * @param historyTimer historian delay
     */
    public void initializeSettings(TartanHouseData house, Integer historyTimer) {

        this.name = house.getHomeName();
        this.user = house.getUser().getUserName();
        this.password = house.getUser().getPassword();
        this.houseID = house.getHouseID();
        this.port = house.getPort();
        this.address = house.getAddress();
        this.authenticated = false;
        this.historyTimer = historyTimer*1000;
        this.logHistory = true;

        this.targetTemp = house.getTargetTemp();
        this.alarmDelay = house.getAlarmDelay();
        this.alarmPasscode = house.getAlarmPasscode();

        // Create and initialize the controller for this house
        this.controller = new IoTControlManager(user, password);
        controller.setTargetTemp(Integer.parseInt(targetTemp));
        controller.setAlarmPassCode(alarmPasscode);

        TartanHome temp = new TartanHome();
        temp.setAlarmDelay(alarmDelay);
        updateAlarmDelay(temp);

        LOGGER.info("House " + this.name + " configured");
    }

    /**
     * Stop logging history
     */
    public void stopHistorian() {
        this.logHistory = false;
    }

    /**
     * Start a thread to log house history on a delay
     */
    public void startHistorian() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (logHistory) {
                    TartanHomeData h = new TartanHomeData(getState());
                    LOGGER.info("Logging " + name + "@" + address + " state");
                    logHistory(h);

                    try {
                        Thread.sleep(historyTimer);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();
    }

    /**
     * Save the current state of the house
     * @param tartanHomeData the current state in a Hibernate-aware format
     */
    @UnitOfWork
    private void logHistory(TartanHomeData tartanHomeData) {
        homeDAO.create(tartanHomeData);
    }

    /**
     * Get the name for this house
     * @return the house name
     */
    public String getName() {
        return name;
    }

    public long getHouseID() {
        return houseID;
    }

    public Boolean authenticate(String user, String pass) {
        this.authenticated = (this.user.equals(user) && this.password.equals(pass));
        return this.authenticated;
    }

    /**
     * Get the house address
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Get the house port
     * @return the port
     */
    public Integer getPort() {
        return port;
    }

    /**
     *  Get the house conncected state
     * @return true if connected; false otherwise
     */
    public Boolean isConnected() {
        return controller.isConnected();
    }

    /**
     * Convert humidifier state
     * @param tartanHome the home
     *  @return true if on; false if off; otherwise null
     */
    private Boolean toIoTHumdifierState(TartanHome tartanHome) {
        if (tartanHome.getHumidifier().equals(TartanHomeValues.OFF)) return false;
        else if (tartanHome.getHumidifier().equals(TartanHomeValues.ON)) return true;
        return null;
    }

    /**
     * Convert light state
     * @param tartanHome the home
     * @return true if on; false if off; otherwise null
     */
    private Boolean toIoTLightState(TartanHome tartanHome) {
        if (tartanHome.getLight().equals(TartanHomeValues.OFF)) return false;
        else if (tartanHome.getLight().equals(TartanHomeValues.ON)) return true;
        return null;
    }

    /**
     * Convert alarm armed state
     * @param tartanHome the home
     * @return true if armed; false if disarmed; otherwise null
     */
    private Boolean toIoTAlarmArmedState(TartanHome tartanHome) {
        if (tartanHome.getAlarmArmed().equals(TartanHomeValues.DISARMED)) return false;
        else if (tartanHome.getAlarmArmed().equals(TartanHomeValues.ARMED)) return true;
        return null;
    }

    /**
     * Convert alarm delay
     * @param tartanHome the home
     * @return the converted delay
     */
    private Integer toIoTAlarmDelay(TartanHome tartanHome) {
        return Integer.parseInt(tartanHome.getAlarmDelay());
    }

    /**
     * Convert alarm passcode
     * @param tartanHome the home
     * @return the passcode
     */
    private String toIoTPasscode(TartanHome tartanHome) {
        return tartanHome.getAlarmPasscode();
    }

    /**
     * Convert door state
     * @param tartanHome the home
     * @return true if open; false if closed' otherwise null
     */
    private Boolean toIoTDoorState(TartanHome tartanHome) {
        if (tartanHome.getDoor().equals(TartanHomeValues.CLOSED)) return false;
        else if (tartanHome.getDoor().equals(TartanHomeValues.OPEN)) return true;
        return null;
    }

    /**
     * Convert proximity state
     * @param tartanHome the home
     * @return true if occupied; false if empty; otherwise null
     */
    private Boolean toIoTProximityState(TartanHome tartanHome) {
        if (tartanHome.getProximity().equals(TartanHomeValues.OCCUPIED)) return true;
        else if (tartanHome.getProximity().equals(TartanHomeValues.EMPTY)) return false;
        return null;
    }

    /**
     * Convert alarm active state
     * @param tartanHome the home
     * @return true if active; false if inactive; otherwise null
     */
    private Boolean toIoTAlarmActiveState(TartanHome tartanHome) {
        if (tartanHome.getAlarmActive().equals(TartanHomeValues.ACTIVE)) return true;
        else if (tartanHome.getAlarmActive().equals(TartanHomeValues.INACTIVE)) return false;
        return null;
    }

    /**
     * Convert heater state
     * @param tartanHome the home
     * @return true if on; false if off; otherwise null
     */
    private Boolean toIoTHeaterState(TartanHome tartanHome) {
        if (tartanHome.getHvacMode().equals(TartanHomeValues.HEAT)) {
            if (tartanHome.getHvacState().equals(TartanHomeValues.ON)) {
                return true;
            } else if (tartanHome.getHvacState().equals(TartanHomeValues.OFF)) {
                return false;
            }
        }
        return null;
    }

    /**
     * Convert chiller state
     * @param tartanHome the home
     * @return true if on; false if off; otherwise null
     */
    private Boolean toIoTChillerState(TartanHome tartanHome) {
        if (tartanHome.getHvacMode().equals(TartanHomeValues.COOL)) {
            if (tartanHome.getHvacState().equals(TartanHomeValues.ON)) {
                return true;
            } else if (tartanHome.getHvacState().equals(TartanHomeValues.OFF)) {
                return false;
            }
        }
        return null;
    }

    /**
     * Convert target temperature state
     * @param tartanHome the home
     * @return converted target temperature
     */
    private Integer toIoTTargetTempState(TartanHome tartanHome) {
        return Integer.parseInt(tartanHome.getTargetTemp());
    }

    /**
     * Convert HVAC mode state
     * @param tartanHome the home
     * @return Heater, Chiller; or null
     */
    private String toIoTHvacModeState(TartanHome tartanHome) {
        if (tartanHome.getHvacMode().equals(TartanHomeValues.HEAT)) return "Heater";
        else if (tartanHome.getHvacMode().equals(TartanHomeValues.COOL)) return "Chiller";
        return null;
    }

    /**
     * Convert lock state
     * @param tartanHome the home
     * @return true if locked; false if unlocked; otherwise null
     */
    private Boolean toIoTLockState(TartanHome tartanHome) {
        if (tartanHome.getLockState().equals(TartanHomeValues.ON)) return true;
        else if (tartanHome.getLockState().equals(TartanHomeValues.OFF)) return false;
        return null;
    }

    /**
     * Convert registered received signal
     * @param tartanHome the home
     * @return true if registered; false if unregistered; otherwise null
     */
    private Boolean toIoTRegisteredReceived(TartanHome tartanHome) {
        if (tartanHome.getRegisterReceived().equals(TartanHomeValues.ON)) return true;
        else if (tartanHome.getRegisterReceived().equals(TartanHomeValues.OFF)) return false;
        return null;
    }

    /**
     * Convert arrive state
     * @param tartanHome the home
     * @return true if arriving; false if not arriving; otherwise null
     */
    private Boolean toIoTArrivingState(TartanHome tartanHome) {
        if (tartanHome.getArriveState().equals(TartanHomeValues.ON)) return true;
        else if (tartanHome.getArriveState().equals(TartanHomeValues.OFF)) return false;
        return null;
    }

    /**
     * Convert intruder state
     * @param tartanHome the home
     * @return true if has intruder; false if not has intruder; otherwise null
     */
    private Boolean toIoTPossibleIntruderState(TartanHome tartanHome) {
        if (tartanHome.getIntruderState().equals(TartanHomeValues.ON)) return true;
        else if (tartanHome.getIntruderState().equals(TartanHomeValues.OFF)) return false;
        return null;
    }

    /**
     * Convert all clear signal
     * @param tartanHome the home
     * @return true if all clear; false if not all clear; otherwise null
     */
    private Boolean toIoTAllClear(TartanHome tartanHome) {
        if (tartanHome.getAllClear().equals(TartanHomeValues.ON)) return true;
        else if (tartanHome.getAllClear().equals(TartanHomeValues.OFF)) return false;
        return null;
    }

    /**
     * Convert the start time
     * @param tartanHome the home
     * @return converted start time
     */
    private LocalTime toIoTStartTime(TartanHome tartanHome) {
        return LocalTime.parse(tartanHome.getStartTime());
    }

    /**
     * Convert the end time
     * @param tartanHome the home
     * @return converted end time
     */
    private LocalTime toIoTEndTime(TartanHome tartanHome) {
        return LocalTime.parse(tartanHome.getEndTime());
    }

    /**
     * Set the house state in the hardware
     * @param h the new state
     * @return true
     */
    public Boolean setState(TartanHome h) {
        synchronized (controller) {
            controller.processStateUpdate(toIotState(h));
            if (h.getAlarmDelay()!=null) {
                updateAlarmDelay(h);
            }
            if (h.getTargetTemp()!=null) {
                targetTemp = h.getTargetTemp();
            }
        }
        return true;
    }



    /**
     * Fetch the current state of the house
     * @return the current state
     */
    public TartanHome getState() {

        TartanHome tartanHome = new TartanHome();

        Hashtable<String, Object> state = null;
        synchronized (controller) {
            state = controller.getCurrentState();
        }
        Set<String> keys = state.keySet();
        for (String key : keys) {

            if (key.equals(IoTValues.TEMP_READING)) {
                tartanHome.setTemperature(String.valueOf(state.get(key)));
            } else if (key.equals(IoTValues.HUMIDITY_READING)) {
                tartanHome.setHumidity(String.valueOf(state.get(key)));
            }
            else if (key.equals(IoTValues.TARGET_TEMP)) {
                tartanHome.setTargetTemp(String.valueOf(state.get(key)));
            }
            else if (key.equals(IoTValues.HUMIDIFIER_STATE)) {
                Boolean humidifierState = (Boolean)state.get(key);
                if (humidifierState) {
                    tartanHome.setHumidifier(String.valueOf(TartanHomeValues.ON));
                } else {
                    tartanHome.setHumidifier(String.valueOf(TartanHomeValues.OFF));
                }
            } else if (key.equals(IoTValues.DOOR_STATE)) {
                Boolean doorState = (Boolean)state.get(key);
                if (doorState) {
                    tartanHome.setDoor(TartanHomeValues.OPEN);
                } else {
                    tartanHome.setDoor(TartanHomeValues.CLOSED);
                }
            } else if (key.equals(IoTValues.LIGHT_STATE)) {
                Boolean lightState = (Boolean)state.get(key);
                if (lightState) {
                    tartanHome.setLight(TartanHomeValues.ON);
                } else {
                    tartanHome.setLight(TartanHomeValues.OFF);
                }
            } else if (key.equals(IoTValues.PROXIMITY_STATE)) {
                Boolean proxState = (Boolean)state.get(key);
                if (proxState) {
                    tartanHome.setProximity(TartanHomeValues.OCCUPIED);
                } else {
                    tartanHome.setProximity(TartanHomeValues.EMPTY);
                }
            } else if (key.equals(IoTValues.ALARM_STATE)) {
                Boolean alarmState = (Boolean)state.get(key);
                if (alarmState) {
                    tartanHome.setAlarmArmed(TartanHomeValues.ARMED);
                } else {
                    tartanHome.setAlarmArmed(TartanHomeValues.DISARMED);
                }
            }
            else if (key.equals(IoTValues.ALARM_ACTIVE)) {
                Boolean alarmActiveState = (Boolean)state.get(key);
                if (alarmActiveState) {
                    tartanHome.setAlarmActive(TartanHomeValues.ACTIVE);
                } else {
                    tartanHome.setAlarmActive(TartanHomeValues.INACTIVE);
                }

            } else if (key.equals(IoTValues.HVAC_MODE)) {
                if (state.get(key).equals("Heater")) {
                    tartanHome.setHvacMode(TartanHomeValues.HEAT);
                } else if (state.get(key).equals("Chiller")) {
                    tartanHome.setHvacMode(TartanHomeValues.COOL);
                }

                // If either heat or chill is on then the hvac is on
                String heaterState = String.valueOf(state.get(IoTValues.HEATER_STATE));
                String chillerState = String.valueOf(state.get(IoTValues.CHILLER_STATE));

                if (heaterState.equals("true") || chillerState.equals("true")) {
                    tartanHome.setHvacState(TartanHomeValues.ON);

                } else {
                    tartanHome.setHvacState(TartanHomeValues.OFF);
                }
            } else if (key.equals(IoTValues.LOCK_STATE)) {
                Boolean lockState = (Boolean)state.get(key);
                if (lockState) {
                    tartanHome.setLockState(TartanHomeValues.ON);
                } else {
                    tartanHome.setLockState(TartanHomeValues.OFF);
                }
            } else if (key.equals(IoTValues.REGISTERED_RECEIVED)) {
                Boolean registerReceived = (Boolean)state.get(key);
                if (registerReceived) {
                    tartanHome.setRegisterReceived(TartanHomeValues.ON);
                } else {
                    tartanHome.setRegisterReceived(TartanHomeValues.OFF);
                }
            } else if (key.equals(IoTValues.ARRIVING_STATE)) {
                Boolean arriveState = (Boolean)state.get(key);
                if (arriveState) {
                    tartanHome.setArriveState(TartanHomeValues.ON);
                } else {
                    tartanHome.setArriveState(TartanHomeValues.OFF);
                }
            } else if (key.equals(IoTValues.POSSIBLE_INTRUDER_STATE)) {
                Boolean intruderState = (Boolean)state.get(key);
                if (intruderState) {
                    tartanHome.setIntruderState(TartanHomeValues.ON);
                } else {
                    tartanHome.setIntruderState(TartanHomeValues.OFF);
                }
            } else if (key.equals(IoTValues.ALL_CLEAR)) {
                Boolean allClear = (Boolean)state.get(key);
                if (allClear) {
                    tartanHome.setAllClear(TartanHomeValues.ON);
                } else {
                    tartanHome.setAllClear(TartanHomeValues.OFF);
                }
            } else if (key.equals(IoTValues.NIGHT_START)) {
                tartanHome.setStartTime(String.valueOf(state.get(key)));
            } else if (key.equals(IoTValues.NIGHT_END)) {
                tartanHome.setEndTime(String.valueOf(state.get(key)));
            }
        }


        tartanHome.setName(this.name);
        tartanHome.setHouseID(this.houseID);
        tartanHome.setAddress(this.address);

        tartanHome.setTargetTemp(this.targetTemp);
        tartanHome.setAlarmDelay(this.alarmDelay);

        tartanHome.setEventLog(controller.getLogMessages());
        tartanHome.setAuthenticated(String.valueOf(this.authenticated));

        return tartanHome;
    }

    /**
     * Convert the state to a format suitable for the hardware
     * @param tartanHome the state
     * @return a map of settings appropriate for the hardware
     */
    private Hashtable<String, Object> toIotState(TartanHome tartanHome) {
        Hashtable<String, Object> state = new Hashtable<String, Object>();

        if (tartanHome.getTargetTemp()!=null){
            state.put(IoTValues.TARGET_TEMP, toIoTTargetTempState(tartanHome));
        }
        if (tartanHome.getDoor()!=null) {
            state.put(IoTValues.DOOR_STATE, toIoTDoorState(tartanHome));
        }
        if (tartanHome.getLight()!=null) {
            state.put(IoTValues.LIGHT_STATE, toIoTLightState(tartanHome));
        }
        if (tartanHome.getProximity()!=null) {
            state.put(IoTValues.PROXIMITY_STATE, toIoTProximityState(tartanHome));
        }

        if (tartanHome.getHumidifier()!=null) {
            state.put(IoTValues.HUMIDIFIER_STATE, toIoTHumdifierState(tartanHome));
        }
        if (tartanHome.getAlarmActive()!=null) {
            state.put(IoTValues.ALARM_ACTIVE, toIoTAlarmActiveState(tartanHome));
        }
        // entering a passcode also disables the alarm
        if (tartanHome.getAlarmPasscode()!=null) {
            state.put(IoTValues.PASSCODE, toIoTPasscode(tartanHome));
            tartanHome.setAlarmArmed(TartanHomeValues.DISARMED);
            state.put(IoTValues.ALARM_STATE, toIoTAlarmArmedState(tartanHome));
        }
        else {
            if (tartanHome.getAlarmArmed() != null) {
                state.put(IoTValues.ALARM_STATE, toIoTAlarmArmedState(tartanHome));
            }
        }
        if (tartanHome.getAlarmDelay()!=null) {
            updateAlarmDelay(tartanHome);
        }

        if (tartanHome.getHvacMode()!=null) {
            if (tartanHome.getHvacMode().equals(TartanHomeValues.HEAT)) {
                state.put(IoTValues.HVAC_MODE, "Heater");
            }
            if (tartanHome.getHvacMode().equals(TartanHomeValues.COOL)) {
                state.put(IoTValues.HVAC_MODE, "Chiller");
            }
        }

        if (tartanHome.getLockState()!=null) {
            state.put(IoTValues.LOCK_STATE, toIoTLockState(tartanHome));
        }

        if (tartanHome.getRegisterReceived()!=null) {
            state.put(IoTValues.REGISTERED_RECEIVED, toIoTRegisteredReceived(tartanHome));
        }

        if (tartanHome.getArriveState()!=null) {
            state.put(IoTValues.ARRIVING_STATE, toIoTArrivingState(tartanHome));
        }

        if (tartanHome.getIntruderState()!=null) {
            state.put(IoTValues.POSSIBLE_INTRUDER_STATE, toIoTPossibleIntruderState(tartanHome));
        }

        if (tartanHome.getAllClear()!=null) {
            state.put(IoTValues.ALL_CLEAR, toIoTAllClear(tartanHome));
        }

        if (tartanHome.getStartTime()!=null) {
            state.put(IoTValues.NIGHT_START, toIoTStartTime(tartanHome));
        }

        if (tartanHome.getEndTime()!=null) {
            state.put(IoTValues.NIGHT_END, toIoTEndTime(tartanHome));
        }

        return state;
    }

    /**
     * Connect to the house
     * @throws TartanHomeConnectException exception passed when connect fails
     */
    public void connect() throws TartanHomeConnectException {
        if (controller.isConnected() == false) {
            if (!controller.connectToHouse(this.address, this.port, this.user, this.password)) {
                throw new TartanHomeConnectException();
            }
        }
    }

    /**
     * Update the alarm delay
     * @param h the new state with updated delay
     */
    private void updateAlarmDelay(TartanHome h) {
        Hashtable<String, Object> stateUpdate = new Hashtable<String, Object>();
        stateUpdate.put(IoTValues.ALARM_DELAY, Integer.parseInt(h.getAlarmDelay()));
        controller.updateSettings(stateUpdate);
        alarmDelay = h.getAlarmDelay();
    }
}