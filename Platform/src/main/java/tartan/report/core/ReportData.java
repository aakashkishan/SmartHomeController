package tartan.report.core;

import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import tartan.report.enums.ReportType;
import tartan.smarthome.auth.TartanUser;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Report")
public class ReportData {
    // Primary key for the table. Not meant to be used
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // the user_id for the user who create the report
    @ManyToOne
    @JoinColumn(name = "user_id")
    private TartanUser user;

    // number of hours the HVAC is on per day
    @Column(name = "type")
    private ReportType type;

    // the creation time
    @Temporal(TemporalType.DATE)
    @Column(name = "create_time", updatable = false)
    private Date create_date;

    // the name of the report
    @Column(name = "report_name")
    private String report_name;

    // number of hours the Light is on per day
    @Column(name = "lhod")
    private Integer Light_hour_of_day;

    // number of hours the Light is on the last week
    @Column(name = "ldow")
    private Integer Light_day_of_week;

    // number of hours the Light is on the last month
    @Column(name = "ldom")
    private Integer Light_day_of_month;

    // number of hours the HVAC is on per day
    @Column(name = "hhod")
    private Integer HVAC_hour_of_day;

    // number of hours the HVAC is on the last week
    @Column(name = "hdow")
    private Integer HVAC_day_of_week;

    // number of hours the HVAC is on the last month
    @Column(name = "hdom")
    private Integer HVAC_day_of_month;

    // total energy
    @Column(name = "energy")
    private Integer energy;

    public @UnknownInitialization ReportData() {
    }

    public ReportData(TartanUser user, ReportType type, Date create_date, String report_name, Integer light_hour_of_day, Integer light_day_of_week, Integer light_day_of_month, Integer HVAC_hour_of_day, Integer HVAC_day_of_week, Integer HVAC_day_of_month, Integer energy) {
        this.user = user;
        this.type = type;
        this.create_date = create_date;
        this.report_name = report_name;
        Light_hour_of_day = light_hour_of_day;
        Light_day_of_week = light_day_of_week;
        Light_day_of_month = light_day_of_month;
        this.HVAC_hour_of_day = HVAC_hour_of_day;
        this.HVAC_day_of_week = HVAC_day_of_week;
        this.HVAC_day_of_month = HVAC_day_of_month;
        this.energy = energy;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TartanUser getUser() {
        return user;
    }

    public void setUser(TartanUser user) {
        this.user = user;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getReport_name() {
        return report_name;
    }

    public void setReport_name(String report_name) {
        this.report_name = report_name;
    }

    public Integer getLight_hour_of_day() {
        return Light_hour_of_day;
    }

    public void setLight_hour_of_day(Integer light_hour_of_day) {
        Light_hour_of_day = light_hour_of_day;
    }

    public Integer getLight_day_of_week() {
        return Light_day_of_week;
    }

    public void setLight_day_of_week(Integer light_day_of_week) {
        Light_day_of_week = light_day_of_week;
    }

    public Integer getLight_day_of_month() {
        return Light_day_of_month;
    }

    public void setLight_day_of_month(Integer light_day_of_month) {
        Light_day_of_month = light_day_of_month;
    }

    public Integer getHVAC_hour_of_day() {
        return HVAC_hour_of_day;
    }

    public void setHVAC_hour_of_day(Integer HVAC_hour_of_day) {
        this.HVAC_hour_of_day = HVAC_hour_of_day;
    }

    public Integer getHVAC_day_of_week() {
        return HVAC_day_of_week;
    }

    public void setHVAC_day_of_week(Integer HVAC_day_of_week) {
        this.HVAC_day_of_week = HVAC_day_of_week;
    }

    public Integer getHVAC_day_of_month() {
        return HVAC_day_of_month;
    }

    public void setHVAC_day_of_month(Integer HVAC_day_of_month) {
        this.HVAC_day_of_month = HVAC_day_of_month;
    }

    public Integer getEnergy() {
        return energy;
    }

    public void setEnergy(Integer energy) {
        this.energy = energy;
    }
}
