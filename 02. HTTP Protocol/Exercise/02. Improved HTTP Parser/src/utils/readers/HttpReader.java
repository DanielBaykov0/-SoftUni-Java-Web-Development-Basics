package utils.readers;

import java.io.IOException;

public interface HttpReader {

    String readLine() throws IOException;

    String readHttpRequest() throws IOException;
}
