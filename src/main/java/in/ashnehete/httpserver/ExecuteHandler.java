package in.ashnehete.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import in.ashnehete.httpserver.utils.GenericUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.List;
import java.util.stream.IntStream;

public class ExecuteHandler implements HttpHandler {

    /**
     * Keys for JSON
     */
    public enum Keys {
        /**
         * Request Keys
         */
        QUERY("query"), DATABASE("database"), HOST("host"),

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

            String query = body.getString(Keys.QUERY.toString());
            String database = body.getString(Keys.DATABASE.toString());
            String host = body.getString(Keys.HOST.toString());

            String connectionUrl = this.getConnectionUrl(host, database);
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection(connectionUrl, "root", null);

            if (connect != null) {
                Statement statement = connect.createStatement();
                boolean executed = statement.execute(query);
                response.put(Keys.EXECUTED.toString(), true);
                if (executed) {
                    ResultSet rs = statement.getResultSet();
                    JSONArray result = this.getJsonFromResultSet(rs);
                    response.put(Keys.RESULT.toString(), result);
                }
            } else {
                response.put(Keys.EXECUTED.toString(), false);
            }

            res = response.toString();
            exchange.sendResponseHeaders(200, res.length());
        } catch (JSONException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            res = e.toString();
            exchange.sendResponseHeaders(400, res.length());
        }

        OutputStream os = exchange.getResponseBody();
        os.write(res.getBytes());
        os.close();
    }

    private String getConnectionUrl(String host, String database) {
        return "jdbc:mysql://" + host + "/" + database + "?useSSL=false";
    }

    private JSONArray getJsonFromResultSet(ResultSet resultSet) throws SQLException {
        ResultSetMetaData md = resultSet.getMetaData();
        int numCols = md.getColumnCount();
        List<String> colNames = IntStream.range(0, numCols).mapToObj(i -> {
            try {
                return md.getColumnName(i + 1);
            } catch (SQLException e) {
                e.printStackTrace();
                return "?";
            }
        }).toList();

        JSONArray result = new JSONArray();
        while (resultSet.next()) {
            JSONObject row = new JSONObject();
            colNames.forEach(cn -> {
                try {
                    row.put(cn, resultSet.getObject(cn));
                } catch (JSONException | SQLException e) {
                    e.printStackTrace();
                }
            });
            result.put(row);
        }
        return result;
    }
}
