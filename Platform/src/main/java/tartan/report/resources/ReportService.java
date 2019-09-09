package tartan.report.resources;

import org.checkerframework.checker.initialization.qual.Initialized;
import org.checkerframework.checker.tainting.qual.Tainted;
import org.checkerframework.checker.tainting.qual.Untainted;
import tartan.report.core.ReportData;
import tartan.report.core.ReportQueryResult;
import tartan.report.db.ReportDAO;
import tartan.report.utils.Extractor;
import tartan.report.enums.ReportType;
import tartan.smarthome.auth.TartanUser;

import java.text.SimpleDateFormat;
import java.util.*;

public class ReportService {
    private ReportDAO reportDAO;
    private SimpleDateFormat format;

    ReportService(@Initialized ReportDAO reportDAO) {
        this.reportDAO = reportDAO;
        this.format = new SimpleDateFormat("yyy-MM-dd");
    }

    public Boolean delete(@Tainted String report_id) {
        try {
            long id = Long.parseLong(report_id);
            return reportDAO.deleteByReportID(id);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /*
     * Query for a certain report
     * @param date the string format is yyyy-MM-dd
     */
    public ReportData getReports(@Untainted String userid,
                                       Date date,
                                       @Tainted String filename,
                                       boolean reqLight,
                                       boolean reqHVAC,
                                       boolean reqEnergy){

        // convert type
        long user_id = Long.parseLong(userid);
        ReportType type = ReportType.getType(reqLight,reqHVAC,reqEnergy);
        // check if there is an existing report
        List<ReportData> result = reportDAO.queryReportByUserID_Date_Type(user_id, date, type);

        if (!result.isEmpty()) {
            // there is existing report item
            // only get the first item
            return result.get(0);
        }

        // else generate a new report item
        else {

            TartanUser user = new TartanUser();
            user.setId(user_id);

            int Light_hour_of_day = 0,
                    Light_day_of_week = 0,
                    Light_day_of_month= 0,
                    HVAC_hour_of_day= 0,
                    HVAC_day_of_week= 0,
                    HVAC_day_of_month= 0,
                    energy= 0;

            // the processing should be called by calculate the home data
            // ! now just render random data
            if (reqLight) {
                Light_hour_of_day = get_light_hour_of_day();
                Light_day_of_week = get_light_day_of_week();
                Light_day_of_month = get_light_day_of_month();
            }
            if (reqHVAC) {
                HVAC_hour_of_day = get_HVAC_hour_of_day();
                HVAC_day_of_week = get_HVAC_day_of_week();
                HVAC_day_of_month = get_HVAC_day_of_month();
            }
            if (reqEnergy) {
                energy = get_energy();
            }

            ReportData new_report = new ReportData(user, type, date, filename,
                    Light_hour_of_day, Light_day_of_week, Light_day_of_month,
                    HVAC_hour_of_day, HVAC_day_of_week, HVAC_day_of_month,
                    energy);
            reportDAO.create(new_report);
            return new_report;
        }

    }

    public List<ReportQueryResult> wrapSearchResult(List<ReportData> data){
        List<ReportQueryResult> result = new ArrayList<ReportQueryResult>(data.size());

        for(ReportData report: data){
            List<Map<String,Object>> sections = new ArrayList<Map<String,Object>>();
            if(ReportType.isShowLight(report.getType())){
                sections.add(Extractor.extractLight(report));
            }
            if(ReportType.isShowHVAC(report.getType())){
                sections.add(Extractor.extractHVAC(report));
            }
            if(ReportType.isShowEnergy(report.getType())){
                sections.add(Extractor.extractEnergy(report));
            }
            String date = this.format.format(report.getCreate_date());
            result.add(new ReportQueryResult(report.getId(),date,report.getReport_name(),sections));
        }
        return result;
    }

    public List<ReportQueryResult> searchReportByName(long userid, @Tainted String filename){
        List<ReportData> qresult = this.reportDAO.searchReportByUserID_Filename(userid,filename);
        return wrapSearchResult(qresult);
    }

    public List<ReportQueryResult> searchReportByDate(long userid, Date date){
        List<ReportData> qresult = this.reportDAO.searchReportByUserID_Date(userid, date);
        return wrapSearchResult(qresult);
    }


    private int get_light_hour_of_day() {
        return new Random().nextInt(24);
    }

    private int get_light_day_of_week() {
        return new Random().nextInt(7);
    }

    private int get_light_day_of_month() {
        return new Random().nextInt(30);
    }
    private int get_HVAC_hour_of_day() {
        return new Random().nextInt(24);
    }

    private int get_HVAC_day_of_week() {
        return new Random().nextInt(7);
    }

    private int get_HVAC_day_of_month() {
        return new Random().nextInt(30);
    }

    private int get_energy() {
        return new Random().nextInt(1000);
    }
}
