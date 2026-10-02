package com.pocketplan.dao;

import com.pocketplan.model.SavingsGoal;
import com.pocketplan.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SavingsGoalDAO {

    public boolean addGoal(SavingsGoal goal) {
        String sql = """
                INSERT INTO savings_goals
                (user_id, name, target_amount, saved_amount, target_date)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, goal.getUserId());
            ps.setString(2, goal.getName());
            ps.setDouble(3, goal.getTargetAmount());
            ps.setDouble(4, goal.getSavedAmount());

            if (goal.getTargetDate() != null) {
                ps.setDate(
                        5,
                        Date.valueOf(goal.getTargetDate())
                );
            } else {
                ps.setNull(5, Types.DATE);
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Add savings goal error: " + e.getMessage()
            );
            return false;
        }
    }

    public List<SavingsGoal> getGoalsByUser(int userId) {
        List<SavingsGoal> goals = new ArrayList<>();

        String sql = """
                SELECT
                    id,
                    user_id,
                    name,
                    target_amount,
                    saved_amount,
                    target_date
                FROM savings_goals
                WHERE user_id = ?
                ORDER BY target_date ASC, id DESC
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    SavingsGoal goal = new SavingsGoal();

                    goal.setId(rs.getInt("id"));
                    goal.setUserId(rs.getInt("user_id"));
                    goal.setName(rs.getString("name"));
                    goal.setTargetAmount(
                            rs.getDouble("target_amount")
                    );
                    goal.setSavedAmount(
                            rs.getDouble("saved_amount")
                    );

                    Date date = rs.getDate("target_date");

                    if (date != null) {
                        goal.setTargetDate(
                                date.toLocalDate()
                        );
                    }

                    goals.add(goal);
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Get savings goals error: " + e.getMessage()
            );
        }

        return goals;
    }

    public boolean updateSavedAmount(
            int goalId,
            int userId,
            double savedAmount) {

        String sql = """
                UPDATE savings_goals
                SET saved_amount = ?
                WHERE id = ? AND user_id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, savedAmount);
            ps.setInt(2, goalId);
            ps.setInt(3, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Update savings goal error: "
                            + e.getMessage()
            );
            return false;
        }
    }

    public boolean deleteGoal(int goalId, int userId) {
        String sql = """
                DELETE FROM savings_goals
                WHERE id = ? AND user_id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, goalId);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Delete savings goal error: "
                            + e.getMessage()
            );
            return false;
        }
    }
}
