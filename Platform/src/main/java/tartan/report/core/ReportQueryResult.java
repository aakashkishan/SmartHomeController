package tartan.report.core;

import org.checkerframework.checker.initialization.qual.UnknownInitialization;

import java.util.List;
import java.util.Map;

public class ReportQueryResult{
    private long id;
    private String date;
    private String name;
    private List<Map<String,Object>> sections;

    public @UnknownInitialization ReportQueryResult(){
    }

    public ReportQueryResult(long id, String date, String name, List<Map<String,Object>> sections) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.sections = sections;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<String,Object>> getSections() {
        return sections;
    }

    public void setSections(List<Map<String,Object>> sections) {
        this.sections = sections;
    }
}
