package View;

import Camera.jrpicam.RPiCamera;
import Camera.jrpicam.exceptions.FailedToRunRaspistillException;

public class ViewManager {

    View currentView;
    private RPiCamera rPiCamera;

    public ViewManager() {
        try {
            rPiCamera = new RPiCamera("/home/pi/");
            rPiCamera.setBrightness(50);
            rPiCamera.setContrast(0);
        } catch (FailedToRunRaspistillException e) {
            e.printStackTrace();
        }
    }

    public View getCurrentView() {
        return currentView;
    }

    public void setCurrentView(View currentView) {
        this.currentView = currentView;
    }

    public RPiCamera getrPiCamera() {
        return rPiCamera;
    }

    public void setrPiCamera(RPiCamera rPiCamera) {
        this.rPiCamera = rPiCamera;
    }
}
