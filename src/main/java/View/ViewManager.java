package View;

public class ViewManager {

    View currentView;

    public ViewManager(View currentView) {
        this.currentView = currentView;
    }

    public ViewManager() {
    }

    public View getCurrentView() {
        return currentView;
    }

    public void setCurrentView(View currentView) {
        this.currentView = currentView;
    }
}
