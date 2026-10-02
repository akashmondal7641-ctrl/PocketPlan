package com.pocketplan.controller;

import com.pocketplan.dao.BudgetDAO;
import com.pocketplan.dao.CategoryDAO;
import com.pocketplan.model.Budget;
import com.pocketplan.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/budgets")
public class BudgetServlet extends HttpServlet {

    private final BudgetDAO budgetDAO = new BudgetDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
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

        User user = (User) session.getAttribute("user");

        request.setAttribute(
                "budgets",
                budgetDAO.getBudgetsByUser(user.getId())
        );

        request.setAttribute(
                "categories",
                categoryDAO.getAllCategories()
        );

        request.getRequestDispatcher(
                "/budgets.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
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

        User user = (User) session.getAttribute("user");

        String action = request.getParameter("action");

        if ("delete".equalsIgnoreCase(action)) {

            int budgetId = Integer.parseInt(
                    request.getParameter("id")
            );

            budgetDAO.deleteBudget(
                    budgetId,
                    user.getId()
            );

        } else {

            double amount = Double.parseDouble(
                    request.getParameter("amount")
            );

            int month = Integer.parseInt(
                    request.getParameter("month")
            );

            int year = Integer.parseInt(
                    request.getParameter("year")
            );

            String categoryIdParam =
                    request.getParameter("categoryId");

            Integer categoryId = null;

            if (categoryIdParam != null &&
                    !categoryIdParam.isBlank()) {

                categoryId =
                        Integer.parseInt(categoryIdParam);
            }

            Budget budget = new Budget(
                    user.getId(),
                    categoryId,
                    amount,
                    month,
                    year
            );

            budgetDAO.addBudget(budget);
        }

        response.sendRedirect(
                request.getContextPath() + "/budgets"
        );
    }
}
