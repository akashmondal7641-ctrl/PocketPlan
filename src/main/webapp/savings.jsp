<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.pocketplan.model.SavingsGoal" %>

<%
    List<SavingsGoal> goals =
            (List<SavingsGoal>) request.getAttribute("goals");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>PocketPlan - Savings Goals</title>
    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/css/style.css">
</head>

<body>

<div class="container">

    <h1>Savings Goals</h1>

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
        <a href="<%= request.getContextPath() %>/reports">
            Reports
        </a>
    </p>

    <hr>

    <h2>Create Savings Goal</h2>

    <form action="<%= request.getContextPath() %>/savings"
          method="post">

        <label>Goal Name:</label>

        <input type="text"
               name="name"
               required>

        <br><br>

        <label>Target Amount:</label>

        <input type="number"
               name="targetAmount"
               step="0.01"
               min="0"
               required>

        <br><br>

        <label>Already Saved:</label>

        <input type="number"
               name="savedAmount"
               step="0.01"
               min="0"
               value="0">

        <br><br>

        <label>Target Date:</label>

        <input type="date"
               name="targetDate">

        <br><br>

        <button type="submit">
            Create Goal
        </button>

    </form>

    <hr>

    <h2>Your Savings Goals</h2>

    <% if (goals == null || goals.isEmpty()) { %>

        <p>No savings goals added yet.</p>

    <% } else { %>

        <% for (SavingsGoal goal : goals) { %>

            <div style="border:1px solid #ccc;
                        padding:15px;
                        margin:15px 0;">

                <h3>
                    <%= goal.getName() %>
                </h3>

                <p>
                    Target:
                    ₹<%= String.format("%.2f",
                            goal.getTargetAmount()) %>
                </p>

                <p>
                    Saved:
                    ₹<%= String.format("%.2f",
                            goal.getSavedAmount()) %>
                </p>

                <p>
                    Progress:
                    <%= String.format("%.1f",
                            goal.getProgress()) %>%
                </p>

                <%
                    if (goal.getTargetDate() != null) {
                %>

                    <p>
                        Target Date:
                        <%= goal.getTargetDate() %>
                    </p>

                <%
                    }
                %>

                <form action="<%= request.getContextPath() %>/savings"
                      method="post">

                    <input type="hidden"
                           name="action"
                           value="update">

                    <input type="hidden"
                           name="id"
                           value="<%= goal.getId() %>">

                    <input type="number"
                           name="savedAmount"
                           step="0.01"
                           min="0"
                           value="<%= goal.getSavedAmount() %>"
                           required>

                    <button type="submit">
                        Update Saved Amount
                    </button>

                </form>

                <br>

                <form action="<%= request.getContextPath() %>/savings"
                      method="post">

                    <input type="hidden"
                           name="action"
                           value="delete">

                    <input type="hidden"
                           name="id"
                           value="<%= goal.getId() %>">

                    <button type="submit">
                        Delete
                    </button>

                </form>

            </div>

        <% } %>

    <% } %>

</div>

</body>
</html>
