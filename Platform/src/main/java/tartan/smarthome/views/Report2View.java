package tartan.smarthome.views;

import io.dropwizard.views.View;

/**
 * A view of the house state.
 * @see <a href="https://www.dropwizard.io/1.0.0/docs/manual/views.html">Dropwizard Views</a>
 */
public class Report2View extends View {

    /**
     * Create a new view
     */
    public Report2View() {
        super("report2.ftl");
    }

}
