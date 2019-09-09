package tartan.report.resources;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.views.View;
import org.checkerframework.checker.tainting.qual.Tainted;
import org.checkerframework.checker.tainting.qual.Untainted;
import org.checkerframework.checker.initialization.qual.Initialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tartan.report.core.ReportData;
import tartan.report.core.ReportQueryResult;
import tartan.report.core.ResponseResult;
import tartan.report.db.ReportDAO;
import tartan.report.enums.ParamErrorType;
import tartan.report.views.Views;
import tartan.report.utils.Extractor;
import tartan.smarthome.auth.TartanUser;
import tartan.smarthome.resources.TartanHomeService;
import tartan.smarthome.resources.TartanResource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * The resource class implements the HTTP handlers via Jersey.
 *  @see <a href="https://www.dropwizard.io/1.0.0/docs/getting-started.html#creating-a-resource-class">Dropwizard Resouces</a>
 */
@Path("/smarthome/report")
@Produces(MediaType.APPLICATION_JSON)
public class ReportResource {

    private ReportService reportService;
    private SimpleDateFormat format;
    private static final Logger LOGGER = LoggerFactory.getLogger(TartanResource.class);

    // There is one service per home
    //private ArrayList<TartanHomeService> services;

    public ReportResource(@Initialized ReportDAO reportDAO) {
        this.reportService = new ReportService(reportDAO);
        this.format = new SimpleDateFormat("yyy-MM-dd");
    }


    @GET
    @Path("/query")
    @Consumes(MediaType.TEXT_HTML)
    @Produces(MediaType.TEXT_HTML)
    public View queryIndex(@Auth TartanUser user){
        return Views.find("report.ftl");
    }

    @GET
    @Path("/query/api")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseResult<ReportQueryResult> query(@Auth TartanUser user,
                                   @QueryParam("filename") @Tainted String filename,
                                   @QueryParam("light") Boolean light,
                                   @QueryParam("HVAC") Boolean hvac,
                                   @QueryParam("energy") Boolean energy,
                                   @QueryParam("date") @Tainted String date) throws ParseException {


        if(ParamErrorType.errorType(filename) != ParamErrorType.NONE){
            return new ResponseResult<>(false,"Error: incorrect or null file name",null);
        }
        if(ParamErrorType.errorType(date) != ParamErrorType.NONE){
            return new ResponseResult<>(false,"Error: incorrect or null date",null);
        }
        if(ParamErrorType.isValidDate(date) == ParamErrorType.INVALID_DATE){
            return new ResponseResult<>(true, "Error: invalid date", null);
        }
        if(ParamErrorType.errorType(light) != ParamErrorType.NONE){
            return new ResponseResult<>(false,"Error: incorrect or null light parameter",null);
        }
        if(ParamErrorType.errorType(hvac) != ParamErrorType.NONE){
            return new ResponseResult<>(false,"Error: incorrect or null hvac parameter",null);
        }
        if(ParamErrorType.errorType(energy) != ParamErrorType.NONE){
            return new ResponseResult<>(false,"Error: incorrect or null energy parameter",null);
        }

        Date dateobj = this.format.parse(date);
        ReportData report =  reportService.getReports(user.getIDString(), dateobj, filename, light, hvac, energy);
        List<Map<String,Object>> sections = new ArrayList<>();
        if (light) {
            sections.add(Extractor.extractLight(report));
        }
        if (hvac) {
            sections.add(Extractor.extractHVAC(report));
        }
        if (energy) {
            sections.add(Extractor.extractEnergy(report));
        }

        ReportQueryResult result = new ReportQueryResult(report.getId(), date, filename, sections);
        return new ResponseResult<>(true,"success",result);
    }

    @GET
    @Path("/search")
    @Produces(MediaType.TEXT_HTML)
    public View searchIndex(@Auth TartanUser user) {
        return Views.find("search_report.ftl");
    }

    @GET
    @Path("/search/name_api")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseResult<List<ReportQueryResult>> searchByName(@Auth TartanUser user,
                                                                @QueryParam("filename") String filename) {

        if(ParamErrorType.errorType(filename) != ParamErrorType.NONE){
            return new ResponseResult<>(false,"Error: incorrect or null file name",null);
        }

        long userId = user.getId();
        List<ReportQueryResult> result = reportService.searchReportByName(userId,filename);
        return new ResponseResult<>(true,"success",result);
    }

    @GET
    @Path("/search/date_api")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseResult<List<ReportQueryResult>> searchByDate(@Auth TartanUser user,
                                                                @QueryParam("date") String date) throws ParseException {
        long userId = user.getId();
        Date dateobj = null;

        if(ParamErrorType.errorType(date) != ParamErrorType.NONE){
            return new ResponseResult<>(false,"Error: incorrect or null date",null);
        }
        if(ParamErrorType.isValidDate(date) == ParamErrorType.INVALID_DATE){
            return new ResponseResult<>(true, "Error: invalid date", null);
        }

        dateobj = this.format.parse(date);

        List<ReportQueryResult> result = reportService.searchReportByDate(userId,dateobj);;
        return new ResponseResult<>(true,"success",result);
    }

    @GET
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseResult<Object> delete(@QueryParam("report_id") String id) {
        if (reportService.delete(id)) {
            return new ResponseResult<>(true,"successfully deleted!",null);
        }
        return new ResponseResult<>(false,"deletion failed!",null);
    }
}

