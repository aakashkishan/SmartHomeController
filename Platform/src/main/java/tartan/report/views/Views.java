package tartan.report.views;

import io.dropwizard.views.View;

public final class Views {

    public static View find(String name){
        return new InternalView(name);
    }

    private final static class InternalView extends View{

        protected InternalView(String templateName) {
            super(templateName);
        }
    }
}
