<aside class="sidebar">

    <div class="sidebar-header">
        <h2>ShopEase</h2>
        <p>Admin Panel</p>
    </div>

    <nav class="sidebar-nav">

        <a href="${pageContext.request.contextPath}/admin/dashboard">
            Dashboard
        </a>

        <a href="${pageContext.request.contextPath}/admin/products">
            Products
        </a>

        <a href="${pageContext.request.contextPath}/admin/categories">
            Categories
        </a>

        <a href="${pageContext.request.contextPath}/admin/orders">
            Orders
        </a>

        <a href="${pageContext.request.contextPath}/admin/customers">
            Customers
        </a>

        <a href="${pageContext.request.contextPath}/admin/inventory">
            Inventory
        </a>

        <a href="${pageContext.request.contextPath}/admin/reviews">
            Reviews
        </a>

    </nav>

    <form action="${pageContext.request.contextPath}/admin/logout"
          method="post"
          class="sidebar-logout">
          <input type="hidden"
           name="adminCsrfToken"
           value="${adminCsrfToken}">

        <button type="submit" class="logout-btn">
            Logout
        </button>

    </form>

</aside>