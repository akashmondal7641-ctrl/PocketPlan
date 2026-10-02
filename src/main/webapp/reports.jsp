<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.pocketplan.model.Transaction" %>

<%
    Double income = (Double) request.getAttribute("income");
    Double expense = (Double) request.getAttribute("expense");
    Double balance = (Double) request.getAttribute("balance");

    List<Transaction> transactions =
            (List<Transaction>) request.getAttribute("transactions");

    if (income == null) income = 0.0;
    if (expense == null) expense = 0.0;
    if (balance == null) balance = 0.0;
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>PocketPlan - Reports</title>
    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/style.css">
</head>

<body>

<div class="container">

    <h1>Spending Reports</h1>

    <p>
        <a href="<%= request.getContextPath() %>/dashboard">
            Dashboard
        </a>
        |
        <a href="<%= request.getContextPath() %>/transactions">
            Transactions
        </a>
        |
        <a href="<%= request.getContextPath() %>/budgets">
            Budgets
        </a>
        |
        <a href="<%= request.getContextPath() %>/savings">
            Savings
        </a>
    </p>

    <hr>

    <h2>Financial Summary</h2>

    <table border="1" cellpadding="12">

        <tr>
            <th>Total Income</th>
            <th>Total Expense</th>
            <th>Balance</th>
        </tr>

        <tr>
            <td>
                ₹<%= String.format("%.2f", income) %>
            </td>

            <td>
                ₹<%= String.format("%.2f", expense) %>
            </td>

            <td>
                ₹<%= String.format("%.2f", balance) %>
            </td>
        </tr>

    </table>

    <hr>

    <h2>All Transactions</h2>

    <% if (transactions == null || transactions.isEmpty()) { %>

        <p>No transaction data available.</p>

    <% } else { %>

        <table border="1" cellpadding="10">

            <tr>
                <th>Date</th>
                <th>Type</th>
                <th>Category</th>
                <th>Description</th>
                <th>Amount</th>
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

            </tr>

            <% } %>

        </table>

    <% } %>

</div>

</body>
</html>
