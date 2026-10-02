package com.pocketplan.controller;

import com.pocketplan.dao.TransactionDAO;
import com.pocketplan.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final TransactionDAO transactionDAO =
            new TransactionDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null ||
                session.getAttribute("user") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login.jsp"
            );

            return;
        }

        User user =
                (User) session.getAttribute("user");

        int userId = user.getId();

        double income =
                transactionDAO.getTotalIncome(userId);

        double expense =
                transactionDAO.getTotalExpense(userId);

        double balance = income - expense;

        request.setAttribute("income", income);
        request.setAttribute("expense", expense);
        request.setAttribute("balance", balance);

        request.setAttribute(
                "recentTransactions",
                transactionDAO.getRecentTransactions(
                        userId,
                        5
                )
        );

        request.getRequestDispatcher(
                "/dashboard.jsp"
        ).forward(request, response);
    }
}
