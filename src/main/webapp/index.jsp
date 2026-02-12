<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Tomcat Demo App</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f6f8;
            margin: 0;
        }

        /* Top Navigation */
        .navbar {
            background-color: #111827;
            padding: 15px 40px;
        }

        .navbar a {
            color: white;
            text-decoration: none;
            margin-right: 25px;
            font-weight: bold;
            font-size: 14px;
        }

        .navbar a:hover {
            text-decoration: underline;
        }

        /* Main Container */
        .container {
            max-width: 900px;
            margin: 50px auto;
            background: white;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 8px 20px rgba(0,0,0,0.08);
        }

        h1 {
            margin-top: 0;
            font-size: 28px;
        }

        .subtitle {
            color: #6b7280;
            margin-bottom: 25px;
        }

        ul {
            line-height: 1.8;
        }

        .buttons {
            margin-top: 30px;
        }

        .btn {
            display: inline-block;
            padding: 12px 20px;
            margin-right: 12px;
            text-decoration: none;
            background-color: #2563eb;
            color: white;
            border-radius: 8px;
            font-size: 14px;
            transition: all 0.2s ease;
        }

        .btn:hover {
            background-color: #1e40af;
            transform: translateY(-2px);
        }

        footer {
            text-align: center;
            padding: 25px;
            color: #6b7280;
            font-size: 13px;
        }
    </style>
</head>

<body>

<!-- Navigation Bar -->
<div class="navbar">
    <a href="/">Home</a>
    <a href="hello">Test Servlet</a>
    <a href="users">View Users</a>
    <a href="add-user.jsp">Add User</a>
</div>

<!-- Main Content -->
<div class="container">
    <h1>Tomcat + PostgreSQL Demo</h1>
    <p class="subtitle">
        A containerized Java web application built with Docker, Maven, and PostgreSQL.
    </p>

    <h3>Tech Stack</h3>
    <ul>
        <li>Apache Tomcat 10</li>
        <li>PostgreSQL 15</li>
        <li>Maven (WAR packaging)</li>
        <li>Docker Multi-Stage Build</li>
        <li>Docker Compose Networking</li>
    </ul>

    <div class="buttons">
        <a href="hello" class="btn">Test Database Connection</a>
        <a href="users" class="btn">View Demo Users</a>
        <a href="add-user.jsp" class="btn">Add New User</a>
    </div>
</div>

<footer>
    Built by Ethan DeLaRosa • 2026 • Dockerized Java Demo
</footer>

</body>
</html>
