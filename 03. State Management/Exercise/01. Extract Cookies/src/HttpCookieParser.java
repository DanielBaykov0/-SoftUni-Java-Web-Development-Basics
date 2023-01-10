import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HttpCookieParser {

    private static final Logger LOGGER = Logger.getLogger(HttpCookieParser.class.getName());

    private static final String HEADER_SEPARATOR = ": ";
    private static final String REQUEST_METHOD = "method";
    private static final String REQUEST_RESOURCE = "resource";
    private static final String REQUEST_HTTP_VERSION = "version";
    private static final String HEADER_DATE = "Date";
    private static final String HEADER_HOST = "Host";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_COOKIE = "Cookie";
    private static final String HTTP_1_1 = "HTTP/1.1";
    private static final String KEY = "key";
    private static final String VALUE = "value";

    private static final Pattern REQUEST_LINE_PATTERN = Pattern.compile(String.format(
            "^(?<%s>[A-Z]{3,7}) (?<%s>/[a-zA-Z0-9/]+) (?<%s>HTTP/[0-9.]+)$",
            REQUEST_METHOD, REQUEST_RESOURCE, REQUEST_HTTP_VERSION));

    private static final Pattern BODY_PARAMS_PATTERN = Pattern.compile(String.format(
            "&?(?<%s>[A-Za-z0-9]+)=(?<%s>[A-Za-z0-9]+)",
            KEY, VALUE));

    private static final Pattern HEADER_PATTERN = Pattern.compile(String.format(
            "^(?<%s>[^ :]+)%s(?<%s>.+)$",
            KEY, HEADER_SEPARATOR, VALUE));

    private static final Pattern COOKIES_PATTERN = Pattern.compile("; ");

    private static final Pattern COOKIES_PAIR_PATTERN = Pattern.compile("=");

    private static final Set<String> VALID_HEADERS = Set.of(
            HEADER_DATE, HEADER_HOST, HEADER_CONTENT_TYPE, HEADER_AUTHORIZATION, HEADER_COOKIE);

    private static final Set<String> HTTP_VERSIONS = Set.of(HTTP_1_1);

    private static final Set<String> HTTP_METHODS = Stream.of(HttpMethod.values())
            .map(Enum::name)
            .collect(Collectors.toUnmodifiableSet());



    private static Map<String, String> parseCookies(String cookiesStr) {
        Map<String, String> cookies = new LinkedHashMap<>();
        if (cookiesStr != null) {
            Arrays.stream(COOKIES_PATTERN.split(cookiesStr))
                    .map(COOKIES_PAIR_PATTERN::split)
                    .forEach(kvp -> cookies.put(kvp[0], kvp[1]));
        }
        return Collections.unmodifiableMap(cookies);
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

    private enum HttpMethod {GET, POST}
}
