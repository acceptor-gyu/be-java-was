package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestParser;
import util.SingletonContainer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestHeader {

    Logger logger = LoggerFactory.getLogger(HttpRequestHeader.class);
    private Map<String, String> httpRequestHeader;

    public HttpRequestHeader(BufferedReader br) throws IOException {
        this.httpRequestHeader = new HashMap<>();

        String line = br.readLine();

        logger.debug("request first line = {}", line);

        if (line != null) {
            String[] parsedUrl = RequestParser.separateUrls(line);
            String httpMethod = parsedUrl[0];
            String resourceUrl = parsedUrl[1];

            String returnUrl = "/index.html";
            if (resourceUrl.startsWith("/user")) {
                returnUrl = SingletonContainer.getUserController().mapToFunctions(httpMethod, resourceUrl);
            }

            saveHeaderNameAndValue("httpMethod", httpMethod);
            saveHeaderNameAndValue("resourceUrl", resourceUrl);
            saveHeaderNameAndValue("returnUrl", returnUrl);
        }

        while (!line.equals("")) {
            line = br.readLine();
            String[] nameAndValue = line.split(": ");
            if (nameAndValue.length == 2) {
                saveHeaderNameAndValue(nameAndValue[0], nameAndValue[1]);
                logger.info("name = [{}], value = [{}]", nameAndValue[0], nameAndValue[1]);
            }
        }
    }

    private void saveHeaderNameAndValue(String name, String value) {
        httpRequestHeader.put(name, value);
    }

    public String getValueByName(String name) {
        return httpRequestHeader.get(name);
    }

    public String getExtension() {
        String fileName = httpRequestHeader.get("returnUrl");
        int index = fileName.lastIndexOf(".");
        return fileName.substring(index + 1);
    }
}
