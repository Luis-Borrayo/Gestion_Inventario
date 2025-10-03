package com.luisborrayo.gestion_inventario.tupackage;


import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/temas")
public class TemasInteresServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Temas de Interés</h1>");
        out.println("<ul>");
        out.println("<li>Programación</li>");
        out.println("<li>Videojuegos</li>");
        out.println("<li>Tecnología</li>");
        out.println("</ul>");
        out.println("</body></html>");
    }
}
