package tartan.report.utils;

import tartan.report.core.ReportData;

import java.util.HashMap;
import java.util.Map;

public class Extractor {

    public static Map<String,Object> extractLight(ReportData report){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("type","light");
        map.put("hours_of_day",report.getLight_hour_of_day());
        map.put("day_of_week",report.getLight_day_of_week());
        map.put("day_of_month",report.getLight_day_of_month());
        return map;
    }

    public static Map<String,Object> extractHVAC(ReportData report){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("type","HVAC");
        map.put("hours_of_day",report.getHVAC_hour_of_day());
        map.put("day_of_week",report.getHVAC_day_of_week());
        map.put("day_of_month",report.getHVAC_day_of_month());
        return map;
    }

    public static Map<String,Object> extractEnergy(ReportData report){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("type","energy");
        map.put("total",report.getEnergy());
        return map;
    }
}
