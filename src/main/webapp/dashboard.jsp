<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.pocketplan.model.Transaction" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>PocketPlan - Dashboard</title>

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/style.css">

</head>

<body>

<div class="app">

    <!-- SIDEBAR -->

    <aside class="sidebar">

        <div class="logo">
            PocketPlan
        </div>

        <nav>

            <a href="<%= request.getContextPath() %>/dashboard"
               class="active">
                Dashboard
            </a>

            <a href="<%= request.getContextPath() %>/transactions">
                Transactions
            </a>

            <a href="<%= request.getContextPath() %>/budgets">
                Budgets
            </a>

            <a href="<%= request.getContextPath() %>/savings">
                Savings Goals
            </a>

            <a href="<%= request.getContextPath() %>/reports">
                Reports
            </a>

        </nav>

        <div class="sidebar-bottom">

            <a href="<%= request.getContextPath() %>/logout">
                Logout
            </a>

        </div>

    </aside>


    <!-- MAIN -->

    <main class="main">

        <header class="topbar">

            <div>

                <h1>Dashboard</h1>

                <p>
                    Welcome back to PocketPlan
                </p>

            </div>

            <a class="button"
               href="<%= request.getContextPath() %>/transactions">
                + Add Transaction
            </a>

        </header>


        <!-- SUMMARY CARDS -->

        <section class="cards">

            <div class="card balance-card">

                <span class="card-title">
                    Current Balance
                </span>

                <h2>
                    ₹<%= String.format(
                        "%.2f",
                        (Double)request.getAttribute("balance")
                    ) %>
                </h2>

            </div>


            <div class="card">

                <span class="card-title">
                    Total Income
                </span>

                <h2 class="income">

                    ₹<%= String.format(
                        "%.2f",
                        (Double)request.getAttribute("income")
                    ) %>

                </h2>

            </div>


            <div class="card">

                <span class="card-title">
                    Total Expenses
                </span>

                <h2 class="expense">

                    ₹<%= String.format(
                        "%.2f",
                        (Double)request.getAttribute("expense")
                    ) %>

                </h2>

            </div>

        </section>


        <!-- RECENT TRANSACTIONS -->

        <section class="panel">

            <div class="panel-header">

                <div>

                    <h2>
                        Recent Transactions
                    </h2>

                    <p>
                        Your latest income and expenses
                    </p>

                </div>

                <a href="<%= request.getContextPath() %>/transactions">
                    View All
                </a>

            </div>


            <div class="table-container">

                <table>

                    <thead>

                    <tr>

                        <th>Description</th>

                        <th>Category</th>

                        <th>Date</th>

                        <th>Type</th>

                        <th>Amount</th>

                    </tr>

                    </thead>

                    <tbody>

                    <%

                        List<Transaction> transactions =
                                (List<Transaction>)
                                request.getAttribute(
                                        "recentTransactions"
                                );

                        if (transactions == null ||
                                transactions.isEmpty()) {

                    %>

                    <tr>

                        <td colspan="5"
                            class="empty">

                            No transactions yet.

                        </td>

                    </tr>

                    <%

                        } else {

                            for (Transaction t : transactions) {

                    %>

                    <tr>

                        <td>
                            <%= t.getDescription() == null
                                    ? "No description"
                                    : t.getDescription() %>
                        </td>

                        <td>
                            <%= t.getCategoryName() == null
                                    ? "Uncategorized"
                                    : t.getCategoryName() %>
                        </td>

                        <td>
                            <%= t.getTransactionDate() %>
                        </td>

                        <td>

                            <% if ("INCOME".equals(t.getType())) { %>

                                <span class="badge income-badge">
                                    Income
                                </span>

                            <% } else { %>

                                <span class="badge expense-badge">
                                    Expense
                                </span>

                            <% } %>

                        </td>

                        <td class="<%= "INCOME".equals(t.getType())
                                ? "income"
                                : "expense" %>">

                            <%= "INCOME".equals(t.getType())
                                    ? "+" : "-" %>

                            ₹<%= String.format(
                                    "%.2f",
                                    t.getAmount()
                            ) %>

                        </td>

                    </tr>

                    <%

                            }

                        }

                    %>

                    </tbody>

                </table>

            </div>

        </section>


        <!-- QUICK ACTIONS -->

        <section class="quick-actions">

            <h2>Quick Actions</h2>

            <div class="quick-grid">

                <a href="<%= request.getContextPath() %>/transactions"
                   class="quick-card">

                    <strong>
                        Add Expense
                    </strong>

                    <span>
                        Record your daily spending
                    </span>

                </a>


                <a href="<%= request.getContextPath() %>/transactions"
                   class="quick-card">

                    <strong>
                        Add Income
                    </strong>

                    <span>
                        Record pocket money or income
                    </span>

                </a>


                <a href="<%= request.getContextPath() %>/budgets"
                   class="quick-card">

                    <strong>
                        Create Budget
                    </strong>

                    <span>
                        Control your monthly spending
                    </span>

                </a>


                <a href="<%= request.getContextPath() %>/savings"
                   class="quick-card">

                    <strong>
                        Savings Goal
                    </strong>

                    <span>
                        Plan for something you want
                    </span>

                </a>

            </div>

        </section>

    </main>

</div>

</body>

</html>
