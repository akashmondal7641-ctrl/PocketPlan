<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.pocketplan.model.Transaction" %>
<%@ page import="com.pocketplan.model.Category" %>
<%@ page import="com.pocketplan.dao.CategoryDAO" %>

<%
    List<Transaction> transactions =
            (List<Transaction>) request.getAttribute("transactions");

    CategoryDAO categoryDAO = new CategoryDAO();
    List<Category> categories = categoryDAO.getAllCategories();

    String error = request.getParameter("error");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>PocketPlan - Transactions</title>
    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/style.css">
</head>

<body>

<div class="container">

    <h1>Transactions</h1>

    <p>
        <a href="<%= request.getContextPath() %>/dashboard">
            Dashboard
        </a>
        |
        <a href="<%= request.getContextPath() %>/budgets">
            Budgets
        </a>
        |
        <a href="<%= request.getContextPath() %>/savings">
            Savings
        </a>
        |
        <a href="<%= request.getContextPath() %>/reports">
            Reports
        </a>
    </p>

    <hr>

    <% if (error != null) { %>

        <p style="color:red;">
            Could not process the transaction.
            Please check the entered values.
        </p>

    <% } %>

    <h2>Add Transaction</h2>

    <form action="<%= request.getContextPath() %>/transactions"
          method="post">

        <label>Type:</label>

        <select name="type" required>
            <option value="EXPENSE">Expense</option>
            <option value="INCOME">Income</option>
        </select>

        <br><br>

        <label>Amount:</label>

        <input type="number"
               name="amount"
               step="0.01"
               min="0.01"
               required>

        <br><br>

        <label>Category:</label>

        <select name="categoryId">

            <option value="">No Category</option>

            <% if (categories != null) {
                for (Category category : categories) {
            %>

                <option value="<%= category.getId() %>">
                    <%= category.getName() %>
                </option>

            <% }
            } %>

        </select>

        <br><br>

        <label>Description:</label>

        <input type="text"
               name="description"
               maxlength="255">

        <br><br>

        <label>Date:</label>

        <input type="date"
               name="transactionDate"
               value="<%= java.time.LocalDate.now() %>"
               required>

        <br><br>

        <button type="submit">
            Add Transaction
        </button>

    </form>

    <hr>

    <h2>Transaction History</h2>

    <% if (transactions == null || transactions.isEmpty()) { %>

        <p>No transactions found.</p>

    <% } else { %>

        <table border="1" cellpadding="10">

            <tr>
                <th>Date</th>
                <th>Type</th>
                <th>Category</th>
                <th>Description</th>
                <th>Amount</th>
                <th>Action</th>
            </tr>

            <% for (Transaction transaction : transactions) { %>

            <tr>

                <td>
                    <%= transaction.getTransactionDate() %>
                </td>

                <td>
                    <%= transaction.getType() %>
                </td>

                <td>
                    <%= transaction.getCategoryName() == null
                            ? "Uncategorized"
                            : transaction.getCategoryName() %>
                </td>

                <td>
                    <%= transaction.getDescription() == null
                            ? ""
                            : transaction.getDescription() %>
                </td>

                <td>
                    ₹<%= String.format("%.2f",
                            transaction.getAmount()) %>
                </td>

                <td>

                    <form action="<%= request.getContextPath() %>/transactions"
                          method="post">

                        <input type="hidden"
                               name="action"
                               value="delete">

                        <input type="hidden"
                               name="id"
                               value="<%= transaction.getId() %>">

                        <button type="submit">
                            Delete
                        </button>

                    </form>

                </td>

            </tr>

            <% } %>

        </table>

    <% } %>

</div>

</body>
</html>
