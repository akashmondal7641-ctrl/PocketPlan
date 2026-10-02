package com.pocketplan.controller;

import com.pocketplan.dao.SavingsGoalDAO;
import com.pocketplan.model.SavingsGoal;
import com.pocketplan.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/savings")
public class SavingsServlet extends HttpServlet {

    private final SavingsGoalDAO savingsGoalDAO =
            new SavingsGoalDAO();

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
                "goals",
                savingsGoalDAO.getGoalsByUser(user.getId())
        );

        request.getRequestDispatcher(
                "/savings.jsp"
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

            int goalId = Integer.parseInt(
                    request.getParameter("id")
            );

            savingsGoalDAO.deleteGoal(
                    goalId,
                    user.getId()
            );

        } else if ("update".equalsIgnoreCase(action)) {

            int goalId = Integer.parseInt(
                    request.getParameter("id")
            );

            double savedAmount = Double.parseDouble(
                    request.getParameter("savedAmount")
            );

            savingsGoalDAO.updateSavedAmount(
                    goalId,
                    user.getId(),
                    savedAmount
            );

        } else {

            String name = request.getParameter("name");

            double targetAmount = Double.parseDouble(
                    request.getParameter("targetAmount")
            );

            double savedAmount = 0;

            String savedParam =
                    request.getParameter("savedAmount");

            if (savedParam != null &&
                    !savedParam.isBlank()) {

                savedAmount =
                        Double.parseDouble(savedParam);
            }

            String targetDateParam =
                    request.getParameter("targetDate");

            LocalDate targetDate = null;

            if (targetDateParam != null &&
                    !targetDateParam.isBlank()) {

                targetDate =
                        LocalDate.parse(targetDateParam);
            }

            SavingsGoal goal = new SavingsGoal(
                    user.getId(),
                    name,
                    targetAmount,
                    savedAmount,
                    targetDate
            );

            savingsGoalDAO.addGoal(goal);
        }

        response.sendRedirect(
                request.getContextPath() + "/savings"
        );
    }
}
