package tartan.report.core;
import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import tartan.report.enums.ReportType;
import tartan.smarthome.auth.TartanUser;

import javax.persistence.*;

@Entity
@Table(name = "Subscription")
public class SubscriptionData {

    // Primary key for the table. Not meant to be used
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // the user_id for the user who has the data
    @ManyToOne
    @JoinColumn(name = "user_id")
    private TartanUser user;

    // number of hours the HVAC is on per day
    @Column(name = "type")
    private ReportType type;

    public @UnknownInitialization SubscriptionData(){

    }

    public SubscriptionData(TartanUser user, ReportType t) {
        this.user = user;
        this.type = t;
    }



    public long getId() {
        return id;
    }

    public TartanUser getUser() {
        return user;
    }

    public ReportType getReportType() {
        return type;
    }

}