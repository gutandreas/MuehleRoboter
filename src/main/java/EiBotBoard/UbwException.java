// https://github.com/jancona/eibotboard am 26. Juni 2021

package EiBotBoard;


/**
 * Exception that wraps the returned error status from the UBW/EBB
 * <p>
 * Note that errors other than TX_BUFFER_OVERRUN and RX_BUFFER_OVERRUN, probably indicate an API bug
 *
 * @author Jim Ancona jim@anconafamily.com
 */
public class UbwException extends RuntimeException {
    enum ErrorCode {
        UNUSED_1("!0"),
        UNUSED_2("!1"),
        TX_BUFFER_OVERRUN("!2"),
        RX_BUFFER_OVERRUN("!3"),
        MISSING_PARAMETER("!4"),
        NEED_COMMA_NEXT("!5"),
        INVALID_PARAMETER_VALUE("!6"),
        EXTRA_PARAMETER("!7"),
        UNKNOWN_COMMAND("!8"),
        COMM_ERROR("CE"),
        RESPONSE_ERROR("RE");

        private final String stringValue;

        ErrorCode(String stringValue) {
            this.stringValue = stringValue;
        }

        public String stringValue() {
            return stringValue;
        }

        public static ErrorCode fromStringValue(String value) {
            for (ErrorCode errorCode : ErrorCode.class.getEnumConstants()) {
                if (errorCode.stringValue().equals(value))
                    return errorCode;
            }
            return null;
        }
    }

    private final ErrorCode errorCode;

    public UbwException(String message) {
        super(message.substring(message.indexOf(' ') + 1));
        errorCode = ErrorCode.fromStringValue(message.substring(0, message.indexOf(' ')));
    }

    public UbwException(String message, Throwable t, ErrorCode ec) {
        super(message, t);
        errorCode = ec;
    }

    public UbwException(String message, ErrorCode ec) {
        super(message);
        errorCode = ec;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
