package com.pocketplan.controller;

import com.pocketplan.model.User;
import com.pocketplan.service.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("register".equalsIgnoreCase(action)) {
            register(request, response);
        } else {
            login(request, response);
        }
    }

    private void register(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        boolean success =
                authService.register(name, email, password);

        if (success) {
            response.sendRedirect(
                    request.getContextPath() + "/login.jsp?registered=true"
            );
        } else {
            response.sendRedirect(
                    request.getContextPath() + "/register.jsp?error=true"
            );
        }
    }

    private void login(HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = authService.login(email, password);

        if (user != null) {

            HttpSession session = request.getSession();

            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());

            response.sendRedirect(
                    request.getContextPath() + "/dashboard"
            );

        } else {

            response.sendRedirect(
                    request.getContextPath() + "/login.jsp?error=true"
            );
        }
    }
}
