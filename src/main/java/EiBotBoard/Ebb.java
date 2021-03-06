// https://github.com/jancona/eibotboard am 26. Juni 2021

package EiBotBoard;

public class Ebb extends Ubw implements EbbCommand {

    public Ebb() {
    }


    @Override
    public int[] queryCurrent() {
        execute("QC");
        String[] response = readResponse().split(",");
        int[] ret = new int[2];
        int i = 0;
        for (String r : response) {
            ret[i++] = Integer.parseInt(r);
        }
        readResponse();
        return ret;
    }

    @Override
    public void nodeCountIncrement() {
        execute("NI");
        readResponse();
    }

    @Override
    public void nodeCountDecrement() {
        execute("ND");
        readResponse();
    }

    @Override
    public void bootLoad() {
        execute("BL");
        readResponse();
    }

    @Override
    public void setNodeCount(long value) {
        validateRange(value, 0L, ((long) 1 << 32) - 1L);
        execute("SN," + value);
        readResponse();
    }

    @Override
    public void setLayer(int layer) {
        validateByte(layer);
        execute("SL," + layer);
        readResponse();
    }

    @Override
    public int queryLayer() {
        execute("QL");
        String s = readResponse();
        readResponse();
        return Integer.parseInt(s);
    }

    @Override
    public long queryNodeCount() {
        // Note: this will fail for unsigned values larger than Long.MAX_VALUE
        execute("QN");
        String s = readResponse();
        readResponse();
        return Long.parseLong(s);
    }

    @Override
    public boolean queryButton() {
        execute("QB");
        String s = readResponse();
        readResponse();
        return s.equals("1");
    }

    @Override
    public boolean queryPen() {
        execute("QP");
        String s = readResponse();
        readResponse();
        return s.equals("1");
    }

    @Override
    public void togglePen() {
        execute("TP");
        readResponse();
    }

    @Override
    public void togglePen(int duration) {
        validateRange(duration, 0, 65535);
        execute("TP," + duration);
        readResponse();
    }

    @Override
    public void stepperMotorMove(int duration, int axis1, int axis2) {

        validateRange(duration, 0, 65535);
        validateRange(axis1, -60000, 60000);
        validateRange(axis2, -60000, 60000);
        execute("SM," + duration + "," + axis1 + "," + axis2);
    }


    @Override
    public void setPenState(boolean state) {
        execute("SP," + (state ? "1" : "0"));
    }

    @Override
    public void setPenState(boolean state, int duration) {
        validateRange(duration, 0, 65535);
        execute("SP," + (state ? "1," : "0,") + duration);
        readResponse();
    }

    @Override
    public void enableMotor(int motor1, int motor2) {
        validateRange(motor1, 0, 5);
        validateRange(motor2, 0, 1);
        if (motor1 > 1)
            execute("EM," + motor1);
        else
            execute("EM," + motor1 + "," + motor2);
        readResponse();
    }

    @Override
    public void servoModeConfigure(int value1, int value2) {
        validateByte(value1);
        validateRange(value2, 0, 65535);
        execute("SC," + value1 + "," + value2);
        readResponse();
    }

    @Override
    public void rcServoOutput(int channel, int duration, int output_pin, int rate) {
        validateRange(channel, 0, 7);
        validateRange(duration, 0, 32000);
        validateRange(output_pin, 0, 24);
        execute("S2," + channel + "," + duration + "," + output_pin + "," + rate);
        readResponse();
    }

}
