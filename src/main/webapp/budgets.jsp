<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.pocketplan.model.Budget" %>
<%@ page import="com.pocketplan.model.Category" %>

<%
    List<Budget> budgets =
            (List<Budget>) request.getAttribute("budgets");

    List<Category> categories =
            (List<Category>) request.getAttribute("categories");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>PocketPlan - Budgets</title>
    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/style.css">
</head>

<body>

<div class="container">

    <h1>Budgets</h1>

    <p>
        <a href="<%= request.getContextPath() %>/dashboard">
            Dashboard
        </a>
        |
        <a href="<%= request.getContextPath() %>/transactions">
            Transactions
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

    <h2>Add Budget</h2>

    <form action="<%= request.getContextPath() %>/budgets"
          method="post">

        <label>Category:</label>

        <select name="categoryId">

            <option value="">All Categories</option>

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

        <label>Amount:</label>

        <input type="number"
               name="amount"
               step="0.01"
               min="0"
               required>

        <br><br>

        <label>Month:</label>

        <select name="month">

            <% for (int month = 1; month <= 12; month++) { %>

                <option value="<%= month %>"
                    <%= month == java.time.LocalDate.now().getMonthValue()
                        ? "selected" : "" %>>

                    <%= month %>

                </option>

            <% } %>

        </select>

        <br><br>

        <label>Year:</label>

        <input type="number"
               name="year"
               value="<%= java.time.LocalDate.now().getYear() %>"
               required>

        <br><br>

        <button type="submit">
            Add Budget
        </button>

    </form>

    <hr>

    <h2>Your Budgets</h2>

    <% if (budgets == null || budgets.isEmpty()) { %>

        <p>No budgets added yet.</p>

    <% } else { %>

        <table border="1" cellpadding="10">

            <tr>
                <th>Category</th>
                <th>Month</th>
                <th>Year</th>
                <th>Budget</th>
                <th>Spent</th>
                <th>Remaining</th>
                <th>Progress</th>
                <th>Action</th>
            </tr>

            <% for (Budget budget : budgets) { %>

            <tr>

                <td>
                    <%= budget.getCategoryName() == null
                            ? "All Categories"
                            : budget.getCategoryName() %>
                </td>

                <td>
                    <%= budget.getMonth() %>
                </td>

                <td>
                    <%= budget.getYear() %>
                </td>

                <td>
                    ₹<%= String.format("%.2f",
                            budget.getAmount()) %>
                </td>

                <td>
                    ₹<%= String.format("%.2f",
                            budget.getSpent()) %>
                </td>

                <td>
                    ₹<%= String.format("%.2f",
                            budget.getRemaining()) %>
                </td>

                <td>
                    <%= String.format("%.1f",
                            budget.getPercentage()) %>%
                </td>

                <td>

                    <form action="<%= request.getContextPath() %>/budgets"
                          method="post">

                        <input type="hidden"
                               name="action"
                               value="delete">

                        <input type="hidden"
                               name="id"
                               value="<%= budget.getId() %>">

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
