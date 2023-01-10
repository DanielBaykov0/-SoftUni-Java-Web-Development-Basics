package utils.handlers;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

public class ResponseHandler implements Handler {

    private static final Pattern URLS_PATTERN = Pattern.compile("/[^ ]+");

    private static final String HEADER_DATE = "Date";
    private static final String HEADER_HOST = "Host";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String AUTHORIZATION_PREFIX = "Basic ";

    private static final Set<String> RESPONSE_HEADERS = Set.of(HEADER_DATE, HEADER_HOST, HEADER_CONTENT_TYPE);

    private static final String RESPONSE_BODY_GET = "Greetings %s!";
    private static final String RESPONSE_BODY_POST = RESPONSE_BODY_GET + " You have successfully created %s with %s.";
    private static final String RESPONSE_BODY_POST_ITEMS_FORMAT = "%s - %s";
    private static final String RESPONSE_BODY_POST_ITEMS_DELIMITER = ", ";
    private static final String RESPONSE_BODY_NOT_FOUND = "The requested functionality was not found.";
    private static final String RESPONSE_BODY_UNAUTHORIZED =
            "You are not authorized to access the requested functionality.";
    private static final String RESPONSE_BODY_BAD_REQUEST =
            "There was an error with the requested functionality due to malformed request.";

    @Override
    public void handle() throws IOException {

    }
}
