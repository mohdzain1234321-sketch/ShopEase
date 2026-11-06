<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List,java.math.BigDecimal,java.math.RoundingMode,com.shopease.model.Product" %>

<%
    List<Product> relatedProducts =
        (List<Product>) request.getAttribute("relatedProducts");

    String ctx = request.getContextPath();
%>

<link rel="stylesheet"
      href="<%=ctx%>/css/related-products.css">

<section class="related-section">

    <div class="related-heading">

        <div>
            <span class="related-eyebrow">MORE TO EXPLORE</span>

            <h2>You may also like</h2>

            <p>
                Discover more products selected from this category.
            </p>
        </div>

        <a href="<%=ctx%>/products" class="related-view-all">
            View all products →
        </a>

    </div>

    <% if (relatedProducts == null || relatedProducts.isEmpty()) { %>

        <div class="related-empty">
            More products will appear here soon.
        </div>

    <% } else { %>

        <div class="related-grid">

            <% for (Product item : relatedProducts) {

                BigDecimal price = item.getPrice() == null
                        ? BigDecimal.ZERO : item.getPrice();

                BigDecimal discount = item.getDiscount() == null
                        ? BigDecimal.ZERO : item.getDiscount();

                BigDecimal finalPrice = price.subtract(
                    price.multiply(discount)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)
                ).setScale(2, RoundingMode.HALF_UP);

                String image = item.getImage() == null
                        || item.getImage().trim().isEmpty()
                        ? "images/product-placeholder.png"
                        : item.getImage();
            %>

                <article class="related-card">

                    <a class="related-image"
                       href="<%=ctx%>/product-details?id=<%=item.getProductId()%>">

                        <img src="<%=ctx%>/<%=image%>"
                             alt="<%=item.getProductName()%>">

                    </a>

                    <div class="related-info">

                        <h3>
                            <a href="<%=ctx%>/product-details?id=<%=item.getProductId()%>">
                                <%=item.getProductName()%>
                            </a>
                        </h3>

                        <div class="related-price-row">

                            <strong>
                                ₹<%=finalPrice.toPlainString()%>
                            </strong>

                            <% if (discount.compareTo(BigDecimal.ZERO) > 0) { %>

                                <del>
                                    ₹<%=price.setScale(2, RoundingMode.HALF_UP).toPlainString()%>
                                </del>

                                <span>
                                    <%=discount.stripTrailingZeros().toPlainString()%>% OFF
                                </span>

                            <% } %>

                        </div>

                        <p class="related-stock">In stock</p>

                        <a class="related-button"
                           href="<%=ctx%>/product-details?id=<%=item.getProductId()%>">
                            View Product
                        </a>

                    </div>

                </article>

            <% } %>

        </div>

    <% } %>

</section>