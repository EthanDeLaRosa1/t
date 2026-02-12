package com.example;

import java.io.IOException;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CreateIssueServlet extends HttpServlet {

  private static String escapeJson(String s) {
    if (s == null) return "";
    return s.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "");
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    resp.setContentType("application/json; charset=UTF-8");

    String category = req.getParameter("category");
    String severityStr = req.getParameter("severity");
    String description = req.getParameter("description");
    String latStr = req.getParameter("lat");
    String lngStr = req.getParameter("lng");

    if (category == null || severityStr == null || description == null || latStr == null || lngStr == null) {
      resp.setStatus(400);
      resp.getWriter().print("{\"error\":\"missing parameters\"}");
      return;
    }

    int severity;
    double lat, lng;
    try {
      severity = Integer.parseInt(severityStr);
      lat = Double.parseDouble(latStr);
      lng = Double.parseDouble(lngStr);
    } catch (Exception e) {
      resp.setStatus(400);
      resp.getWriter().print("{\"error\":\"invalid number format\"}");
      return;
    }

    String host = System.getenv("DB_HOST");
    String port = System.getenv("DB_PORT");
    String db   = System.getenv("DB_NAME");
    String user = System.getenv("DB_USER");
    String pass = System.getenv("DB_PASSWORD");

    String url = "jdbc:postgresql://" + host + ":" + port + "/" + db;

    try (Connection conn = DriverManager.getConnection(url, user, pass);
         PreparedStatement ps = conn.prepareStatement(
           "INSERT INTO issues (category, severity, description, lat, lng) VALUES (?, ?, ?, ?, ?) RETURNING id"
         )) {

      ps.setString(1, category);
      ps.setInt(2, severity);
      ps.setString(3, description);
      ps.setDouble(4, lat);
      ps.setDouble(5, lng);

      ResultSet rs = ps.executeQuery();
      rs.next();
      int id = rs.getInt(1);

      resp.getWriter().print("{\"ok\":true,\"id\":" + id + "}");

    } catch (Exception e) {
      resp.setStatus(500);
      resp.getWriter().print("{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
    }
  }
}
