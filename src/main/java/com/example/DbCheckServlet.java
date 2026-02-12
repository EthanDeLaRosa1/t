package com.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DbCheckServlet extends HttpServlet {

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    private static String page(String title, String innerHtml) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!doctype html>");
        sb.append("<html lang=\"en\">");
        sb.append("<head>");
        sb.append("<meta charset=\"utf-8\"/>");
        sb.append("<meta name=\"viewport\" content=\"width=device-width,initial-scale=1\"/>");
        sb.append("<title>").append(esc(title)).append("</title>");
        sb.append("<link rel=\"stylesheet\" href=\"/css/style.css\">");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<div class=\"wrap\">");

        sb.append("<div class=\"topbar\">");
        sb.append("<div class=\"brand\">");
        sb.append("<div class=\"logo\">CI</div>");
        sb.append("<div>");
        sb.append("<div class=\"h1\">City Infrastructure Issue Tracker</div>");
        sb.append("<div class=\"sub\">Dockerized Tomcat + Postgres demo</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("<a class=\"chip\" href=\"/\"><- Back</a>");
        sb.append("</div>");

        sb.append("<div class=\"card\">");
        sb.append(innerHtml);
        sb.append("</div>");

        sb.append("</div>");
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html; charset=UTF-8");

        String host = System.getenv("DB_HOST");
        String port = System.getenv("DB_PORT");
        String db   = System.getenv("DB_NAME");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASSWORD");

        if (host == null || port == null || db == null || user == null || pass == null) {
            resp.setStatus(500);
            String inner = ""
                + "<h2>DB config missing</h2>"
                + "<p class=\"muted\">One or more environment variables are missing.</p>"
                + "<pre><code>"
                + "DB_HOST=" + esc(host) + "\n"
                + "DB_PORT=" + esc(port) + "\n"
                + "DB_NAME=" + esc(db) + "\n"
                + "DB_USER=" + esc(user) + "\n"
                + "DB_PASSWORD=" + (pass == null ? "null" : "****") + "\n"
                + "</code></pre>";
            resp.getWriter().print(page("DB Config Missing", inner));
            return;
        }

        String url = "jdbc:postgresql://" + host + ":" + port + "/" + db;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            resp.setStatus(500);
            String inner = ""
                + "<h2>Postgres driver missing</h2>"
                + "<p class=\"muted\">The JDBC driver is not on Tomcat's runtime classpath.</p>"
                + "<pre><code>" + esc(e.toString()) + "</code></pre>";
            resp.getWriter().print(page("Driver Missing", inner));
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement("SELECT 1");
             ResultSet rs = ps.executeQuery()) {

            rs.next();
            int one = rs.getInt(1);

            String inner = ""
                + "<div class=\"status ok\"><span class=\"dot\"></span>Database Connected</div>"
                + "<p class=\"muted\">Connection succeeded and <code>SELECT 1</code> returned <b>" + one + "</b>.</p>"
                + "<pre><code>" + esc(url) + "</code></pre>";

            resp.getWriter().print(page("DB OK", inner));

        } catch (Exception e) {
            resp.setStatus(500);
            String inner = ""
                + "<div class=\"status bad\"><span class=\"dot\"></span>Database Error</div>"
                + "<p class=\"muted\">Tomcat reached the endpoint, but the DB threw an exception.</p>"
                + "<pre><code>" + esc(e.getClass().getSimpleName()) + ": " + esc(e.getMessage()) + "</code></pre>"
                + "<pre><code>" + esc(url) + "</code></pre>";

            resp.getWriter().print(page("DB Failed", inner));
        }
    }
}
