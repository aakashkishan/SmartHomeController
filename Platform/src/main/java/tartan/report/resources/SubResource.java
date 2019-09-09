package tartan.report.resources;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.views.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tartan.report.core.ReportData;
import tartan.report.core.ReportQueryResult;
import tartan.report.core.ResponseResult;
import tartan.report.db.ReportDAO;
import tartan.report.db.SubscriptionDAO;
import tartan.report.enums.ParamErrorType;
import tartan.report.enums.ReportType;
import tartan.report.views.MonthlyReportView;
import tartan.report.views.Views;
import tartan.report.utils.Extractor;
import tartan.smarthome.auth.TartanUser;
import tartan.smarthome.resources.TartanHomeService;
import tartan.smarthome.resources.TartanResource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * The resource class implements the HTTP handlers via Jersey.
 *  @see <a href="https://www.dropwizard.io/1.0.0/docs/getting-started.html#creating-a-resource-class">Dropwizard Resouces</a>
 */
@Path("/smarthome")
@Produces(MediaType.APPLICATION_JSON)
public class SubResource {

    private SubscriptionService subService;

    public SubResource(SubscriptionDAO subDAO) {
        this.subService = new SubscriptionService(subDAO);
    }

    /**
     * Fetch the current house state via HTTP GET. Managed by Jersey
     * @return a view of the house or null
     */
    @GET
    @Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    @Path("/subscribe")
    @Timed
    @UnitOfWork
    public View s_report() {
        return Views.find("subscribe.ftl");
    }

    /**
     * Fetch the current house state via HTTP GET. Managed by Jersey
     * @return a view of the house or null
     */
    @GET
    @Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_JSON})
    @Path("/monthly_report")
    @Timed
    @UnitOfWork
    public View m_report(@Auth TartanUser user) {
        // There are better ways to check authorization, but this works fine
        ReportType type = subService.isSubscribed(user);

        return new MonthlyReportView(user, ReportType.isShowLight(type), ReportType.isShowHVAC(type), ReportType.isShowEnergy(type));
    }

    /**
     * update the house state via a HTTP POST. Managed by Jersey
     * @return either HTTP OK or UNAUTHORIZED
     */
    @GET
    @Path("/subscribe/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed
    public boolean update(@Auth TartanUser user,
                           @QueryParam("light") Boolean light,
                           @QueryParam("hvac") Boolean hvac,
                           @QueryParam("energy") Boolean energy) {
        System.out.println("Printing... Username: " + user.getUserName() + " Light:"+light+ " HVAC:"+hvac+" Energy:"+energy);
        subService.subscribe(user, light, hvac, energy);
        return true;
    }
}