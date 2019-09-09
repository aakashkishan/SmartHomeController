package tartan.report.views;

import io.dropwizard.views.View;
import tartan.report.enums.ReportType;
import tartan.smarthome.auth.TartanUser;
import tartan.smarthome.core.TartanHome;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * A view of the house state.
 * @see <a href="https://www.dropwizard.io/1.0.0/docs/manual/views.html">Dropwizard Views</a>
 */

public class MonthlyReportView extends View {
    private TartanUser tartanUser;
    private String light;
    private String hvac;
    private String energy;

    public MonthlyReportView(TartanUser tartanUser, boolean light, boolean hvac, boolean energy) {
        super("monthly_report.ftl");
        this.tartanUser = tartanUser;
        this.light = String.valueOf(light);
        this.hvac = String.valueOf(hvac);
        this.energy = String.valueOf(energy);
    }

    public TartanUser getTartanUser() {
        return tartanUser;
    }

    public String getLight() {
        return light;
    }

    public String getHvac() {
        return hvac;
    }

    public String getEnergy() {
        return energy;
    }
}
