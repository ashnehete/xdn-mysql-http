package in.ashnehete.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import in.ashnehete.httpserver.utils.GenericUtils;
import in.ashnehete.httpserver.utils.MySQLDump;
import in.ashnehete.httpserver.utils.ProcessRuntime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

public class RestoreHandler implements HttpHandler {

    /**
     * Keys for JSON
     */
    public enum Keys {
        /**
         * Request Keys
         */
        HOST("host"), SRC("src"),

        /**
         * Response Keys
         */
        EXECUTED("executed"), RESULT("result");

        public final String label;

        Keys(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return this.label;
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        GenericUtils.logRequest(exchange);
        JSONObject response = new JSONObject();
        String res;

        try {
            JSONObject body = GenericUtils.getJSONObjectFromBody(exchange.getRequestBody());
            String host = body.getString(Keys.HOST.toString());
            String src = body.getString(Keys.SRC.toString());

            boolean execute = ProcessRuntime.run(MySQLDump.getMySQLRestoreCommand(host, "root", null), src);
            response.put(Keys.EXECUTED.toString(), execute);
            response.put(Keys.RESULT.toString(), src);

            res = response.toString();
            exchange.sendResponseHeaders(200, res.length());
        } catch (JSONException e) {
            e.printStackTrace();
            res = e.toString();
            exchange.sendResponseHeaders(400, res.length());
        }

        OutputStream os = exchange.getResponseBody();
        os.write(res.getBytes());
        os.close();
    }
}
