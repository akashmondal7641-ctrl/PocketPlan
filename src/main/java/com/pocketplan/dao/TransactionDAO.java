package com.pocketplan.dao;

import com.pocketplan.model.Transaction;
import com.pocketplan.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public boolean addTransaction(Transaction transaction) {

        String sql = """
                INSERT INTO transactions
                (user_id, category_id, type, amount, description, transaction_date)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, transaction.getUserId());

            if (transaction.getCategoryId() != null) {
                ps.setInt(2, transaction.getCategoryId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            ps.setString(3, transaction.getType());
            ps.setDouble(4, transaction.getAmount());
            ps.setString(5, transaction.getDescription());
            ps.setDate(
                    6,
                    Date.valueOf(transaction.getTransactionDate())
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Add transaction error: " + e.getMessage());
            return false;
        }
    }

    public List<Transaction> getTransactionsByUser(int userId) {

        List<Transaction> transactions = new ArrayList<>();

        String sql = """
                SELECT
                    t.id,
                    t.user_id,
                    t.category_id,
                    c.name AS category_name,
                    t.type,
                    t.amount,
                    t.description,
                    t.transaction_date
                FROM transactions t
                LEFT JOIN categories c
                    ON t.category_id = c.id
                WHERE t.user_id = ?
                ORDER BY t.transaction_date DESC, t.id DESC
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Transaction transaction = new Transaction();

                    transaction.setId(rs.getInt("id"));
                    transaction.setUserId(rs.getInt("user_id"));

                    int categoryId = rs.getInt("category_id");

                    if (rs.wasNull()) {
                        transaction.setCategoryId(null);
                    } else {
                        transaction.setCategoryId(categoryId);
                    }

                    transaction.setCategoryName(
                            rs.getString("category_name")
                    );

                    transaction.setType(
                            rs.getString("type")
                    );

                    transaction.setAmount(
                            rs.getDouble("amount")
                    );

                    transaction.setDescription(
                            rs.getString("description")
                    );

                    transaction.setTransactionDate(
                            rs.getDate("transaction_date")
                                    .toLocalDate()
                    );

                    transactions.add(transaction);
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Get transactions error: " + e.getMessage()
            );
        }

        return transactions;
    }

    public boolean deleteTransaction(int transactionId, int userId) {

        String sql = """
                DELETE FROM transactions
                WHERE id = ? AND user_id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, transactionId);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Delete transaction error: " + e.getMessage()
            );

            return false;
        }
    }

    public double getTotalIncome(int userId) {

        return getTotalByType(userId, "INCOME");
    }

    public double getTotalExpense(int userId) {

        return getTotalByType(userId, "EXPENSE");
    }

    private double getTotalByType(int userId, String type) {

        String sql = """
                SELECT COALESCE(SUM(amount), 0)
                FROM transactions
                WHERE user_id = ? AND type = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, type);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Calculate total error: " + e.getMessage()
            );
        }

        return 0;
    }

    public double getBalance(int userId) {

        return getTotalIncome(userId)
                - getTotalExpense(userId);
    }

    public List<Transaction> getRecentTransactions(
            int userId,
            int limit) {

        List<Transaction> transactions = new ArrayList<>();

        String sql = """
                SELECT
                    t.id,
                    t.user_id,
                    t.category_id,
                    c.name AS category_name,
                    t.type,
                    t.amount,
                    t.description,
                    t.transaction_date
                FROM transactions t
                LEFT JOIN categories c
                    ON t.category_id = c.id
                WHERE t.user_id = ?
                ORDER BY t.transaction_date DESC, t.id DESC
                LIMIT ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, limit);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Transaction transaction = new Transaction();

                    transaction.setId(rs.getInt("id"));
                    transaction.setUserId(rs.getInt("user_id"));

                    int categoryId = rs.getInt("category_id");

                    if (rs.wasNull()) {
                        transaction.setCategoryId(null);
                    } else {
                        transaction.setCategoryId(categoryId);
                    }

                    transaction.setCategoryName(
                            rs.getString("category_name")
                    );

                    transaction.setType(
                            rs.getString("type")
                    );

                    transaction.setAmount(
                            rs.getDouble("amount")
                    );

                    transaction.setDescription(
                            rs.getString("description")
                    );

                    transaction.setTransactionDate(
                            rs.getDate("transaction_date")
                                    .toLocalDate()
                    );

                    transactions.add(transaction);
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Get recent transactions error: "
                            + e.getMessage()
            );
        }

        return transactions;
    }
}
