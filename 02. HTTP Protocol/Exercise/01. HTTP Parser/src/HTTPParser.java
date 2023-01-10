import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class HTTPParser {

    private static final Logger LOGGER = Logger.getLogger(HTTPParser.class.getName());

    public static final String HTTP_LINE_SEPARATOR = "\r\n";
    public static final String HEADER_SEPARATOR = ": ";
    public static final String REQUEST_METHOD = "method";
    public static final String REQUEST_RESOURCE = "resource";
    public static final String REQUEST_HTTP_VERSION = "version";
    public static final String HEADER_DATE = "Date";
    public static final String HEADER_HOST = "Host";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String AUTHORIZATION_PREFIX = "Basic ";
    public static final String HTTP_1_1 = "HTTP/1.1";
    public static final String KEY = "key";
    public static final String VALUE = "value";

    public static final String RESPONSE_LINE = HTTP_1_1 + " %d %d";
    public static final String RESPONSE_BODY_GET = "Greetings %s!";
    public static final String RESPONSE_BODY_POST = RESPONSE_BODY_GET + " You have successfully created %s with %s.";
    public static final String POST_RESPONSE_ITEMS_FORMAT = "%s - %s";
    public static final String POST_RESPONSE_ITEMS_DELIMITER = ", ";

    public static final Pattern REQUEST_LINE_PATTERN = Pattern.compile(
            String.format("^(?<%s>[A-Z]{3,7}) (?<%s>/[a-zA-Z0-9/]+) (?<%s>HTTP/[0-9.]+)$",
                    REQUEST_METHOD, REQUEST_RESOURCE, REQUEST_HTTP_VERSION));

    public static final Pattern BODY_PARAMS_PATTERN = Pattern.compile(
            String.format("&?(?<%s>[^ :]+)%s(?<%s>.+)$",
                    KEY, VALUE));

    public static final Pattern HEADER_PATTERN = Pattern.compile(
            String.format("^(?<%s>[^ :]+)%s(?<%s>.+)$",
                    KEY, HEADER_SEPARATOR, VALUE));

    public static final Pattern URLS_PATTERN = Pattern.compile("/[^ ]+");

    public static final Set<String> RESPONSE_HEADERS =
            Set.of(HEADER_DATE, HEADER_HOST, HEADER_CONTENT_TYPE);

    public static final Set<String> VALID_HEADERS =
            Set.of(HEADER_DATE, HEADER_HOST, HEADER_CONTENT_TYPE, HEADER_AUTHORIZATION);

    public static final Set<String> HTTP_VERSIONS = Set.of(HTTP_1_1);


}




































