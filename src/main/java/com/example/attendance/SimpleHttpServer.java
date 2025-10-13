package com.example.attendance;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.sql.*;

public class SimpleHttpServer {
    private HttpServer server;

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/scan", new ScanHandler());
        server.start();
        System.out.println("🌍 Server running at http://localhost:8000/scan");
    }

    private static class ScanHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            String query = ex.getRequestURI().getQuery();
            String res;
            if (query != null && query.startsWith("id=")) {
                String id = query.substring(3);
                String studentName = getStudentName(id);
                if (studentName != null) {
                    DatabaseManager.saveAttendance(id, studentName, "SESSION-1");
                    res = "<h1>✅ Marked Present: " + studentName + "</h1>";
                } else {
                    res = "<h1>❌ Invalid Student ID</h1>";
                }
            } else {
                res = "<form method='get'><h2>QR Attendance</h2>" +
                        "<input name='id' placeholder='Enter ID'>" +
                        "<button>Submit</button></form>";
            }
            ex.sendResponseHeaders(200, res.getBytes().length);
            try (OutputStream os = ex.getResponseBody()) {
                os.write(res.getBytes());
            }
        }

        private String getStudentName(String id) {
            String sql = "SELECT username FROM users WHERE username=?";
            try (Connection conn = DatabaseManager.connect();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) return rs.getString("username");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
