package mobi.newsound.controllers;

import com.google.gson.JsonObject;
import mobi.newsound.database.AuthContext;
import mobi.newsound.database.DataAccess;
import mobi.newsound.utils.RESTRoute;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import spark.Request;
import spark.Response;

import java.io.File;
import java.io.OutputStream;
import java.util.Date;

import static mobi.newsound.utils.Config.config;

public class ExportReportsByDateController implements RESTRoute {

    static {
        String src = config.get("RF_REPORT_SOURCE").getAsString();
        String bin = config.get("RF_REPORT_BINARY").getAsString();
        try {
            JasperCompileManager.compileReportToFile(new File(src).getPath(), new File(bin).getPath());
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object handle(Request request, Response response, JsonObject body) throws Exception {
        try {
            String id = request.headers("id");
            String sessionToken = request.headers("sessionToken");

            long from_l = Long.parseLong(request.headers("from"));
            long to_l = Long.parseLong(request.headers("to"));

            Date from = new Date(from_l);
            Date to = new Date(to_l);

            AuthContext context = new AuthContext(id, sessionToken);
            OutputStream os = response.raw().getOutputStream();

            try(DataAccess db = DataAccess.getInstance()) {
                assert db != null;

                db.getReportsExportByDate(context, from, to, os);
                response.raw().setContentType("application/pdf");

                return os;
            } catch (DataAccess.DSException e) {
                return e.getMessage();
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
