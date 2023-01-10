import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            String.format("&?(?<%s>[A-Za-z0-9]+)=(?<%s>[A-Za-z0-9]+)",
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

    private static final Set<String> HTTP_METHODS = Stream.of(HttpMethod.values())
            .map(Enum::name)
            .collect(Collectors.toUnmodifiableSet());

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            Set<String> urls = parseUrls(reader);
            System.out.println(urls);

            Map<String, String> request = parseRequestLine(reader);
            System.out.println(request);

            Map<String, String> headers = parseHeaders(reader);
            System.out.println(headers);

        } catch (IOException | IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "ERROR", e);
        }
    }

    private static Set<String> parseUrls(BufferedReader reader) throws IOException {
        Set<String> urls = new HashSet<>();

        String urlsLine = reader.readLine();
        Matcher matcher = URLS_PATTERN.matcher(urlsLine);
        while (matcher.find()) {
            urls.add(matcher.group());
        }

        return Collections.unmodifiableSet(urls);
    }

    private static Map<String, String> parseRequestLine(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        Matcher matcher = REQUEST_LINE_PATTERN.matcher(requestLine);

        if (!matcher.matches() ||
                !HTTP_VERSIONS.contains(matcher.group(REQUEST_HTTP_VERSION)) ||
                !HTTP_METHODS.contains(matcher.group(REQUEST_METHOD))) {
            throw new IllegalArgumentException("Invalid Request Line: " + requestLine);
        }

        String httpVersion = matcher.group(REQUEST_HTTP_VERSION);
        String httpMethod = matcher.group(REQUEST_METHOD);
        String resource = matcher.group(REQUEST_RESOURCE);

        return Map.of(REQUEST_METHOD, httpMethod,
                REQUEST_RESOURCE, resource,
                REQUEST_HTTP_VERSION, httpVersion);
    }

    private static Map<String, String> parseHeaders(BufferedReader reader) throws IOException {
        Map<String, String> headers = new LinkedHashMap<>();
        String header;

        while ((header = reader.readLine()) != null && header.length() > 0) {
            Matcher matcher = HEADER_PATTERN.matcher(header);
            if (matcher.matches() && VALID_HEADERS.contains(matcher.group(KEY))) {
                String key = matcher.group(KEY);
                String value = matcher.group(VALUE);
                headers.put(key, value);
            }
        }

        return Collections.unmodifiableMap(headers);
    }

    private enum HttpMethod {GET, POST}
}





































