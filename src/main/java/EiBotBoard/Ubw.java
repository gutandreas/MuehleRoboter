// https://github.com/jancona/eibotboard am 26. Juni 2021

package EiBotBoard;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class Ubw implements UbwCommand {
    private static final Charset ASCII = StandardCharsets.US_ASCII;
    protected com.fazecast.jSerialComm.SerialPort serialPort = null;
    private BufferedOutputStream out;
    private SerialReader reader;
    private UbwCommand.TimerListener timerListener;

    public Ubw() {
        connectJSerialCom();
        if (serialPort == null)
            throw new UbwException("No serial port found to open", UbwException.ErrorCode.COMM_ERROR);
    }


    public void connectJSerialCom() {

        for (com.fazecast.jSerialComm.SerialPort s : com.fazecast.jSerialComm.SerialPort.getCommPorts()) {
            System.out.println("Angeschlossene USB-Devices: " + s);
            if (s.toString().contains("EiBot")) {
                serialPort = s;
                System.out.println("Verbunden mit: " + s);
                break;
            }
        }

        serialPort.openPort();
        serialPort.setBaudRate(9600);
        serialPort.setNumStopBits(com.fazecast.jSerialComm.SerialPort.ONE_STOP_BIT);
        serialPort.setParity(com.fazecast.jSerialComm.SerialPort.NO_PARITY);
        reader = new SerialReader(new InputStreamReader(serialPort.getInputStream()));
        out = new BufferedOutputStream(serialPort.getOutputStream());
    }

    public void closeJSerialCOM() {
        try {
            reader.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void configure(int dirA, int dirB, int dirC, int analogEnableCount) {
        validateByte(dirA);
        validateByte(dirB);
        validateByte(dirC);
        validateRange(analogEnableCount, 0, 13);
        execute("C," + dirA + "," + dirB + "," + dirC + "," + analogEnableCount);
    }

    @Override
    public void outputState(int portA, int portB, int portC) {
        validateByte(portA);
        validateByte(portB);
        validateByte(portC);
        execute("O," + portA + "," + portB + "," + portC);
        readResponse();
    }


    @Override
    public int[] inputState() {
        execute("I");
        String s = readResponse();
        return processState(s);
    }

    private int[] processState(String s) {
        try {
            String[] r = s.split(",");
            int[] response = new int[3];
            response[0] = Integer.parseInt(r[1]);
            response[1] = Integer.parseInt(r[2]);
            response[2] = Integer.parseInt(r[3]);
            return response;
        } catch (RuntimeException e) {
            throw new UbwException("Exception parsing response: '" + s + "'", e, UbwException.ErrorCode.RESPONSE_ERROR);
        }
    }

    @Override
    public String version() {
        execute("V");
        return readResponse();
    }

    @Override
    public void reset() {
        execute("R");
        readResponse();
    }

    @Override
    public void timerReadInputs(int timeBetweenPacketsInMilliseconds,
                                TimerMode mode, TimerListener listener) {
        validateRange(timeBetweenPacketsInMilliseconds, 0, 30000);
        if (timeBetweenPacketsInMilliseconds > 0) {
            timerListener = listener;
        } else {
            timerListener = null;
        }
        execute("T," + timeBetweenPacketsInMilliseconds + "," + mode.intValue());
        readResponse();
    }

    @Override
    public int[] sampleAnalogInputs() {
        execute("R");
        String s = readResponse();
        String[] r = s.split(",");
        int[] response = new int[r.length - 1];
        for (int i = 1; i < r.length; i++) {
            response[i - 1] = Integer.parseInt(r[i]);
        }
        return response;
    }

    @Override
    public int memoryRead(int address) {
        validateRange(address, 0, 4095);
        execute("MR," + address);
        return Integer.parseInt(readResponse().substring(3));
    }

    @Override
    public void memoryWrite(int address, int value) {
        validateRange(address, 0, 4095);
        validateByte(value);
        execute("MW," + address + "," + value);
        readResponse();
    }

    @Override
    public void pinDirection(Port port, int pin, PinDirection direction) {
        validateRange(pin, 0, 7);
        execute("PD," + port + "," + pin + "," + direction.intValue());
        readResponse();
    }

    @Override
    public boolean pinInput(Port port, int pin) {
        validateRange(pin, 0, 7);
        execute("PI," + port + "," + pin);
        return readResponse().substring(3).equals("1");
    }

    @Override
    public void pinOutput(Port port, int pin, boolean value) {
        validateRange(pin, 0, 7);
        execute("PI," + port + "," + pin + "," + (value ? "1" : "0"));
        readResponse();
    }

    @Override
    public void configure(int parameter, int value) {
        validateByte(parameter);
        execute("CU," + parameter + "," + value);
        readResponse();
    }

    @Override
    public void rcServoOutput(Port port, int pin, int value) {
        validateRange(pin, 0, 7);
        validateRange(value, 0, 11890);
        execute("RC," + port + "," + pin + "," + value);
        readResponse();
    }

    @Override
    public void bulkConfigure(int init, int waitMask, int waitDelay, int strobeMask, int strobeDelay) {
        validateByte(strobeMask);
        execute("BC," + init + "," + waitMask + "," + waitDelay + "," + strobeMask + "," + strobeDelay);
        readResponse();
    }

    @Override
    public void bulkOutput(byte[] byteStream) {
        execute("BO," + toHex(byteStream));
        readResponse();
    }

    @Override
    public void bulkStream(byte[] byteStream) {
        try {
            out.write(("BS," + byteStream.length + ",").getBytes(ASCII));
            out.write(byteStream);
            out.write(13);
        } catch (IOException e) {
            throw new UbwException("Exception writing command 'BS," + byteStream.length + ",' to device", e, UbwException.ErrorCode.COMM_ERROR);
        }
        readResponse();
    }


    protected void validateByte(int value) {
        validateRange(value, 0, 255);
    }

    protected void validateRange(long value, long l, long m) {
        if (value < l || value > m)
            throw new IllegalArgumentException("Value '" + value + "' must be between " + l + " and " + m + ".");

    }

    public void execute(String command) {
        try {
            System.out.println(command);
            out.write(command.getBytes(StandardCharsets.US_ASCII));
            out.write(13);
            out.flush();
        } catch (IOException e) {
            throw new UbwException("Exception writing command '" + command + "' to device", e, UbwException.ErrorCode.COMM_ERROR);
        }
    }

    protected String readResponse() {
        return reader.getLastLine();
    }

    static final char[] HEXES = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String toHex(byte[] raw) {
        if (raw == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw) {
            hex.append(HEXES[(b & 0xF0) >> 4]).append(HEXES[b & 0x0F]);
        }
        return hex.toString();
    }

    public class SerialReader implements SerialPortEventListener {
        private final InputStreamReader in;
        private final StringBuilder builder = new StringBuilder();
        private final BlockingQueue<String> lines = new LinkedBlockingQueue<String>();

        public SerialReader(InputStreamReader in) {
            this.in = in;
        }

        public void close() throws IOException {
            in.close();
        }

        public String getLastLine() {
            String line;
            try {
                line = lines.take();
            } catch (InterruptedException e) {
                throw new UbwException("Exception receiving data", e, UbwException.ErrorCode.COMM_ERROR);
            }
            if (line.startsWith("!") && line.length() >= 2)
                throw new UbwException("Error: " + line.substring(2), UbwException.ErrorCode.valueOf(line.substring(0, 2)));
            return line;
        }

        public void serialEvent(SerialPortEvent event) {
            if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                try {
                    int c = 0;
                    while (in.ready() && (c = in.read()) > -1) {
                        if (c == '\n' || c == '\r') {
                            processLine(builder.toString());
                            builder.setLength(0);
                            in.read();
                        } else {
                            builder.append((char) c);
                        }
                    }
                } catch (IOException e) {
                    throw new UbwException("Exception receiving data", e, UbwException.ErrorCode.COMM_ERROR);
                }
            }
        }

        private void processLine(String string) {
            System.out.println("line=" + string);
            char start = string.substring(0, 1).toUpperCase().charAt(0);
            if (timerListener != null && (start == 'I' || start == 'A')) {
                timerListener.timerResponse(TimerMode.fromChar(start), processState(string));
            }
            lines.add(string);
        }

    }

}
