package com.luisborrayo.gestion_inventario.tupackage;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/biografia")
public class BiografiaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Mi Biografía</h1>");
        out.println("<p>Aquí va tu biografía personal...</p>");
        out.println("</body></html>");
    }
}
