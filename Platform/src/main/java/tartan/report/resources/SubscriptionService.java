package tartan.report.resources;

import org.checkerframework.checker.nullness.qual.Nullable;
import tartan.report.core.SubscriptionData;
import tartan.report.db.SubscriptionDAO;
import tartan.report.enums.ReportType;
import tartan.smarthome.auth.TartanUser;

import java.util.List;

public class SubscriptionService {

    private SubscriptionDAO subscriptionDAO;

    public SubscriptionService(SubscriptionDAO subscriptionDAO) {
        this.subscriptionDAO = subscriptionDAO;
    }

    /*
     * The user subscribes to a particular report type
     * @param TartanUser user, and the report type that the user subscribes to.
     */
    public boolean subscribe(TartanUser user,
                                                boolean reqLight,
                                                boolean reqHVAC,
                                                boolean reqEnergy){

        try {
            // convert type
            ReportType type = ReportType.getType(reqLight,reqHVAC,reqEnergy);
            long user_id = user.getId();

            // check if there is an existing report
            subscriptionDAO.deleteByUserID(user_id);

            SubscriptionData new_entry = new SubscriptionData(user, type);
            subscriptionDAO.create(new_entry);
            return true;
            } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    /*
     * Check what report did the user subscribe to
     * @param TartanUser user
     */
    public ReportType isSubscribed(TartanUser user){
        try {
            // convert type
            long user_id = user.getId();

            // check if there is an existing report
            List<SubscriptionData> result = subscriptionDAO.querySubscriptionByUserID(user_id);

            if (!result.isEmpty()) {
                return result.get(0).getReportType();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ReportType.NONE;
    }


}
