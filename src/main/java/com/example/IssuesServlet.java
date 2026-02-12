package com.example;

import java.io.IOException;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class IssuesServlet extends HttpServlet {

  private static String escapeJson(String s) {
    if (s == null) return "";
    return s.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "");
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    resp.setContentType("application/json; charset=UTF-8");

    String host = System.getenv("DB_HOST");
    String port = System.getenv("DB_PORT");
    String db   = System.getenv("DB_NAME");
    String user = System.getenv("DB_USER");
    String pass = System.getenv("DB_PASSWORD");

    String url = "jdbc:postgresql://" + host + ":" + port + "/" + db;

    try (Connection conn = DriverManager.getConnection(url, user, pass);
         PreparedStatement ps = conn.prepareStatement(
           "SELECT id, category, severity, description, lat, lng FROM issues ORDER BY id DESC"
         );
         ResultSet rs = ps.executeQuery()) {

      StringBuilder json = new StringBuilder();
      json.append("[");

      boolean first = true;
      while (rs.next()) {
        if (!first) json.append(",");
        first = false;

        json.append("{")
            .append("\"id\":").append(rs.getInt("id")).append(",")
            .append("\"category\":\"").append(escapeJson(rs.getString("category"))).append("\",")
            .append("\"severity\":").append(rs.getInt("severity")).append(",")
            .append("\"description\":\"").append(escapeJson(rs.getString("description"))).append("\",")
            .append("\"lat\":").append(rs.getDouble("lat")).append(",")
            .append("\"lng\":").append(rs.getDouble("lng"))
            .append("}");
      }

      json.append("]");
      resp.getWriter().print(json.toString());

    } catch (Exception e) {
      resp.setStatus(500);
      resp.getWriter().print("{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
    }
  }
}
