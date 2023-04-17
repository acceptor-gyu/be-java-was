package request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class HttpRequestLine {

    private Logger log = LoggerFactory.getLogger(getClass());
    private HashMap<String, String> httpRequestLineMap;

    public HttpRequestLine(BufferedReader br) throws IOException {

        httpRequestLineMap = new HashMap<>();
        String requestLine = br.readLine();

        if (requestLine != null) {
            String[] parsedUrl = RequestParser.separateUrls(requestLine);
            String httpMethod = parsedUrl[0];
            String resourceUrl = parsedUrl[1];
            String httpVersion = parsedUrl[2];

            if (resourceUrl.equals("/")) {
                resourceUrl = "/index.html";
            }

            log.info("http method={} resourceUrl={}", httpMethod, resourceUrl);

            saveRequestLineNameAndValue("httpMethod", httpMethod);
            saveRequestLineNameAndValue("resourceUrl", resourceUrl);
            saveRequestLineNameAndValue("httpVersion", httpVersion);
        }
    }

    public String getValueByNameInRequestLine(String name) {
        return httpRequestLineMap.get(name);
    }

    public String getExtension() {
        String fileName = httpRequestLineMap.get("resourceUrl");
        int index = fileName.lastIndexOf(".");
        return fileName.substring(index + 1);
    }

    private void saveRequestLineNameAndValue(String name, String value) {
        httpRequestLineMap.put(name, value);
    }
}
