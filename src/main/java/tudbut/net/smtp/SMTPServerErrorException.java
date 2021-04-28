package tudbut.net.smtp;

public class SMTPServerErrorException extends RuntimeException {
    public SMTPServerErrorException(String code) {
        super("Code: " + code);
    }
}
