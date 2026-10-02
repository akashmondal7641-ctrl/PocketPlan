package com.pocketplan.controller;

import com.pocketplan.dao.TransactionDAO;
import com.pocketplan.model.Transaction;
import com.pocketplan.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/transactions")
public class TransactionServlet extends HttpServlet {

    private final TransactionDAO transactionDAO =
            new TransactionDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
                session.getAttribute("user") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login.jsp"
            );

            return;
        }

        User user =
                (User) session.getAttribute("user");

        request.setAttribute(
                "transactions",
                transactionDAO.getTransactionsByUser(
                        user.getId()
                )
        );

        request.getRequestDispatcher(
                "/transactions.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
                session.getAttribute("user") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login.jsp"
            );

            return;
        }

        User user =
                (User) session.getAttribute("user");

        String action = request.getParameter("action");

        if ("delete".equalsIgnoreCase(action)) {

            deleteTransaction(request, response, user);

        } else {

            addTransaction(request, response, user);
        }
    }

    private void addTransaction(
            HttpServletRequest request,
            HttpServletResponse response,
            User user)
            throws IOException {

        try {

            String type = request.getParameter("type");

            double amount = Double.parseDouble(
                    request.getParameter("amount")
            );

            String description =
                    request.getParameter("description");

            String dateString =
                    request.getParameter("transactionDate");

            LocalDate date;

            if (dateString == null ||
                    dateString.isBlank()) {

                date = LocalDate.now();

            } else {

                date = LocalDate.parse(dateString);
            }

            String categoryString =
                    request.getParameter("categoryId");

            Integer categoryId = null;

            if (categoryString != null &&
                    !categoryString.isBlank()) {

                categoryId =
                        Integer.parseInt(categoryString);
            }

            if (!"INCOME".equals(type) &&
                    !"EXPENSE".equals(type)) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/transactions?error=type"
                );

                return;
            }

            if (amount <= 0) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/transactions?error=amount"
                );

                return;
            }

            Transaction transaction =
                    new Transaction(
                            user.getId(),
                            categoryId,
                            type,
                            amount,
                            description,
                            date
                    );

            transactionDAO.addTransaction(transaction);

            response.sendRedirect(
                    request.getContextPath()
                            + "/transactions"
            );

        } catch (Exception e) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/transactions?error=true"
            );
        }
    }

    private void deleteTransaction(
            HttpServletRequest request,
            HttpServletResponse response,
            User user)
            throws IOException {

        try {

            int transactionId =
                    Integer.parseInt(
                            request.getParameter("id")
                    );

            transactionDAO.deleteTransaction(
                    transactionId,
                    user.getId()
            );

        } catch (Exception e) {

            System.err.println(
                    "Delete error: " + e.getMessage()
            );
        }

        response.sendRedirect(
                request.getContextPath()
                        + "/transactions"
        );
    }
}
