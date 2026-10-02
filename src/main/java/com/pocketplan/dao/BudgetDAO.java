package com.pocketplan.dao;

import com.pocketplan.model.Budget;
import com.pocketplan.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BudgetDAO {

    public boolean addBudget(Budget budget) {
        String sql = """
                INSERT INTO budgets
                (user_id, category_id, amount, month, year)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, budget.getUserId());

            if (budget.getCategoryId() != null) {
                ps.setInt(2, budget.getCategoryId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            ps.setDouble(3, budget.getAmount());
            ps.setInt(4, budget.getMonth());
            ps.setInt(5, budget.getYear());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Add budget error: " + e.getMessage());
            return false;
        }
    }

    public List<Budget> getBudgetsByUser(int userId) {
        List<Budget> budgets = new ArrayList<>();

        String sql = """
                SELECT
                    b.id,
                    b.user_id,
                    b.category_id,
                    c.name AS category_name,
                    b.amount,
                    b.month,
                    b.year,
                    COALESCE(
                        (
                            SELECT SUM(t.amount)
                            FROM transactions t
                            WHERE t.user_id = b.user_id
                              AND t.category_id = b.category_id
                              AND t.type = 'EXPENSE'
                              AND MONTH(t.transaction_date) = b.month
                              AND YEAR(t.transaction_date) = b.year
                        ),
                        0
                    ) AS spent
                FROM budgets b
                LEFT JOIN categories c
                    ON b.category_id = c.id
                WHERE b.user_id = ?
                ORDER BY b.year DESC, b.month DESC, b.id DESC
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Budget budget = new Budget();

                    budget.setId(rs.getInt("id"));
                    budget.setUserId(rs.getInt("user_id"));

                    int categoryId = rs.getInt("category_id");

                    if (rs.wasNull()) {
                        budget.setCategoryId(null);
                    } else {
                        budget.setCategoryId(categoryId);
                    }

                    budget.setCategoryName(
                            rs.getString("category_name")
                    );

                    budget.setAmount(
                            rs.getDouble("amount")
                    );

                    budget.setMonth(
                            rs.getInt("month")
                    );

                    budget.setYear(
                            rs.getInt("year")
                    );

                    budget.setSpent(
                            rs.getDouble("spent")
                    );

                    budgets.add(budget);
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Get budgets error: " + e.getMessage()
            );
        }

        return budgets;
    }

    public boolean deleteBudget(int budgetId, int userId) {
        String sql = """
                DELETE FROM budgets
                WHERE id = ? AND user_id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, budgetId);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Delete budget error: " + e.getMessage()
            );
            return false;
        }
    }
}
