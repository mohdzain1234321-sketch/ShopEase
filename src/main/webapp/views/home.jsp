<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>

<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>ShopEase | Online Shopping</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">

    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>

    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700;800&family=Manrope:wght@400;500;600;700;800&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css?v=account-menu-3">

</head>

<body>

    <!-- HEADER -->

    <header class="main-header">

        <a class="logo" href="${pageContext.request.contextPath}/views/home.jsp">

            <span class="logo-mark">▣</span> ShopEase<span class="logo-dot">.</span>

        </a>

        <div class="search-container">
            <form class="search-box" action="${pageContext.request.contextPath}/products" method="get">
                <input type="search" id="productSearch" name="q"
                       placeholder="Search for products, brands and more..."
                       aria-label="Search products" autocomplete="off">
                <button type="submit" aria-label="Search">⌕</button>
            </form>
            <div id="searchSuggestions" class="search-suggestions"></div>
        </div>

        <%

    boolean isLoggedIn = session.getAttribute("userId") != null;

%>

<div class="header-actions">

    <a href="${pageContext.request.contextPath}/products?sort=deals">

        <span>♢</span> Deals

    </a>

    <a href="${pageContext.request.contextPath}/wishlist">

        <span>♡</span> Wishlist

    </a>

    <a href="${pageContext.request.contextPath}/cart">

        <span>🛒</span> Cart

    </a>

    <% if (isLoggedIn) { %>

        <div class="account-dropdown">

            <button class="account-btn" type="button">

                ♙ My Account ▾

            </button>

            <div class="account-menu">

                <a href="${pageContext.request.contextPath}/account">

                    ♙ My Profile

                </a>

                <a href="${pageContext.request.contextPath}/addresses">

                    ⌖ My Addresses

                </a>

                <a href="${pageContext.request.contextPath}/orders">

                    📦 My Orders

                </a>

                <a href="${pageContext.request.contextPath}/logout">

                    ↪ Logout

                </a>

            </div>

        </div>

    <% } else { %>

        <a class="login-btn"

           href="${pageContext.request.contextPath}/login">

            Login / Register

        </a>

    <% } %>

</div>

    </header>

    <!-- MAIN NAVIGATION -->

    <nav class="category-nav" aria-label="Main navigation">

        <a class="nav-item nav-home" href="${pageContext.request.contextPath}/views/home.jsp" aria-label="Discover">

            <span class="nav-icon" aria-hidden="true">⌂</span><span>Discover</span>

        </a>

        <a class="nav-item" href="${pageContext.request.contextPath}/products" aria-label="Shop All">

            <span class="nav-icon" aria-hidden="true">▦</span><span>Shop All</span>

        </a>

        <a class="nav-item" href="${pageContext.request.contextPath}/products?sort=deals" aria-label="Hot Deals">

            <span class="nav-icon" aria-hidden="true">♢</span><span>Hot Deals</span>

        </a>

        <a class="nav-item" href="${pageContext.request.contextPath}/products?sort=newest" aria-label="Just Arrived">

            <span class="nav-icon" aria-hidden="true">✦</span><span>Just Arrived</span>

        </a>

        <a class="nav-item" href="${pageContext.request.contextPath}/orders" aria-label="My Orders">

            <span class="nav-icon" aria-hidden="true">▣</span><span>My Orders</span>

        </a>

    </nav>

    <main>

        <!-- HERO: keep the approved sequential podium animation -->

        <section class="hero-section">

            <div class="hero-content">

                <div class="hero-promo-pill">✦ Mega Sale · Up to 70% Off</div>

                <p class="hero-tagline">THE SMART WAY TO SHOP</p>

                <h1>Shop the Latest<br>Trends at <span>ShopEase</span></h1>

                <p class="hero-description">

                    Discover top brands, exciting deals and everyday essentials —

                    all in one place.

                </p>

                <div class="hero-buttons">

                    <a href="${pageContext.request.contextPath}/products" class="shop-now-btn">Shop Now <span>→</span></a>

                    <a href="${pageContext.request.contextPath}/products?sort=deals" class="hero-secondary-btn">Explore Deals</a>

                </div>

                <div class="hero-trust">

                    <span>♧ Free Delivery*</span>

                    <span>↻ Easy Returns</span>

                    <span>◇ Secure Payments</span>

                </div>

            </div>

            <div class="floating-showcase">

                <span class="showcase-spark spark-one"></span>

                <span class="showcase-spark spark-two"></span>

                <span class="showcase-spark spark-three"></span>

                <div class="product-podium">

                    <div class="podium-top"></div>

                    <div class="podium-base"></div>

                </div>

                <div class="floating-product product-three">

                    <img src="${pageContext.request.contextPath}/images/shoes.png" alt="Premium sneakers">

                </div>

                <div class="floating-product product-five">

                    <img src="${pageContext.request.contextPath}/images/handbag.png" alt="Handbag">

                </div>

                <div class="floating-product product-one">

                    <img src="${pageContext.request.contextPath}/images/headphones.png" alt="Wireless headphones">

                </div>

                <div class="floating-product product-two">

                    <img src="${pageContext.request.contextPath}/images/watch.png" alt="Smartwatch">

                </div>

                <div class="floating-product product-four">

                    <img src="${pageContext.request.contextPath}/images/perfume.png" alt="Perfume">

                </div>

                <div class="floating-product product-six">

                    <img src="${pageContext.request.contextPath}/images/camera.png" alt="Digital camera">

                </div>

            </div>

        </section>

        <!-- SHOP BY CATEGORY -->

        <section class="content-section category-section">

            <div class="section-heading compact-heading">

                <h2>Shop by Category</h2>

                <a href="${pageContext.request.contextPath}/products">View All Categories <span>→</span></a>

            </div>

            <div class="category-grid">

                <a class="category-card" href="${pageContext.request.contextPath}/products?categoryId=1">

                    <div class="category-image">

                        <img src="${pageContext.request.contextPath}/images/headphones.png" alt="Electronics">

                    </div>

                    <span>Electronics</span>

                </a>

                <a class="category-card" href="${pageContext.request.contextPath}/products?categoryId=2">

                    <div class="category-emoji">📱</div>

                    <span>Mobiles</span>

                </a>

                <a class="category-card" href="${pageContext.request.contextPath}/products?categoryId=3">

                    <div class="category-image">

                        <img src="${pageContext.request.contextPath}/images/mens-shirt.png" alt="Fashion">

                    </div>

                    <span>Fashion</span>

                </a>

                <a class="category-card" href="${pageContext.request.contextPath}/products?categoryId=4">

                    <div class="category-image">

                        <img src="${pageContext.request.contextPath}/images/shoes.png" alt="Footwear">

                    </div>

                    <span>Footwear</span>

                </a>

                <a class="category-card" href="${pageContext.request.contextPath}/products?categoryId=5">

                    <div class="category-emoji">🏠</div>

                    <span>Home Appliances</span>

                </a>

                <a class="category-card" href="${pageContext.request.contextPath}/products?categoryId=6">

                    <div class="category-image">

                        <img src="${pageContext.request.contextPath}/images/perfume.png" alt="Beauty">

                    </div>

                    <span>Beauty</span>

                </a>

                <a class="category-card" href="${pageContext.request.contextPath}/products?categoryId=7">

                    <div class="category-emoji">📚</div>

                    <span>Books</span>

                </a>

                <a class="category-card" href="${pageContext.request.contextPath}/products?categoryId=8">

                    <div class="category-image">

                        <img src="${pageContext.request.contextPath}/images/handbag.png" alt="Accessories">

                    </div>

                    <span>Accessories</span>

                </a>

            </div>

        </section>

        <!-- PROMOTIONAL BANNERS -->

        <section class="promo-grid">

            <article class="promo-banner deal-banner">

                <div class="promo-copy">

                    <span class="promo-kicker">⚡ LIMITED TIME OFFER</span>

                    <h2>Deal of the Day</h2>

                    <p>Save more on your everyday favourites.</p>

                    <strong>UP TO <b>60% OFF</b></strong>

                    <a href="${pageContext.request.contextPath}/products?sort=deals">Shop Deals <span>→</span></a>

                </div>

                <div class="promo-art promo-art-laptop">💻</div>

            </article>

            <article class="promo-banner arrivals-banner">

                <div class="promo-copy">

                    <span class="promo-kicker">FRESH FINDS, JUST IN</span>

                    <h2>New Arrivals</h2>

                    <p>Fresh styles for every season.</p>

                    <a href="${pageContext.request.contextPath}/products?sort=newest">Explore Now <span>→</span></a>

                </div>

                <div class="arrival-images">

                    <img src="${pageContext.request.contextPath}/images/handbag.png" alt="New handbag">

                    <img src="${pageContext.request.contextPath}/images/shoes.png" alt="New sneakers">

                </div>

            </article>

        </section>

    	<!-- TRENDING PRODUCTS -->

    	<section class="content-section trending-section">

    		<div class="section-heading">

    			<div>

    				<p class="section-label">POPULAR RIGHT NOW</p>

    				<h2>Trending Products</h2>

    			</div>

    			<a href="${pageContext.request.contextPath}/products"> View All

    				<span>→</span>

    			</a>

    		</div>

    		<div class="trending-grid">

    			<c:choose>

    				<c:when test="${not empty trendingProducts}">

    					<c:forEach var="product" items="${trendingProducts}">

    						<article class="shop-product-card">

    							<form class="wishlist-form"

    								  action="${pageContext.request.contextPath}/wishlist"

    								  method="post">

    								<input type="hidden" name="action" value="add">

    								<input type="hidden" name="productId" value="${product.productId}">

    								<button class="wishlist-button" type="submit"

    								        aria-label="Add* ${product.productName} *to wishlist">♡</button>

    							</form>

    							<a class="shop-product-image"

    								href="${pageContext.request.contextPath}/product-details?id=${product.productId}">

    								<img src="${pageContext.request.contextPath}/${product.image}"

    								alt="${product.productName}">

    							</a>

    							<div class="shop-product-info">

    								<h3><a href="${pageContext.request.contextPath}/product-details?id=${product.productId}">${product.productName}</a></h3>

    								<div class="price-line">

    									<strong>₹${product.price}</strong>

    									<c:if test="${product.discount > 0}">

    										<em>${product.discount}% OFF</em>

    									</c:if>

    								</div>

    								<p class="product-stock">${product.stock} items available</p>

    							</div>

    						</article>

    					</c:forEach>

    				</c:when>

    				<c:otherwise>

    					<p class="no-products">No products are available at the

    						moment.</p>

    				</c:otherwise>

    			</c:choose>

    		</div>

    	</section>

    	<!-- SHOPPING BENEFITS -->

        <section class="benefits-section">

            <div class="benefit"><span class="benefit-icon">♧</span><div><h3>Free Delivery</h3><p>On orders above ₹499</p></div></div>

            <div class="benefit"><span class="benefit-icon">◇</span><div><h3>Secure Payments</h3><p>100% safe &amp; encrypted</p></div></div>

            <div class="benefit"><span class="benefit-icon">↻</span><div><h3>Easy Returns</h3><p>Hassle-free 7-day returns</p></div></div>

            <div class="benefit"><span class="benefit-icon">☏</span><div><h3>24/7 Support</h3><p>We're here to help you</p></div></div>

        </section>

    </main>

    <!-- FOOTER -->

    <footer class="main-footer">

        <div class="footer-brand">

            <a class="footer-logo" href="${pageContext.request.contextPath}/views/home.jsp">♧ ShopEase</a>

            <p>Your one-stop online shopping destination.<br>Find what you love. Shop what you need.</p>

            <div class="social-links"><a href="#" aria-label="Instagram">◎</a><a href="#" aria-label="YouTube">▶</a><a href="#" aria-label="Facebook">f</a><a href="#" aria-label="LinkedIn">in</a></div>

        </div>

        <div class="footer-column">

            <h3>About Us</h3>

            <a href="#">Our Story</a><a href="#">Careers</a><a href="#">Contact Us</a>

        </div>

        <div class="footer-column">

            <h3>Customer Service</h3>

            <a href="#">Help Center</a><a href="#">Track Order</a><a href="#">Returns &amp; Refunds</a>

        </div>

        <div class="footer-column">

            <h3>Policies</h3>

            <a href="#">Privacy Policy</a><a href="#">Terms &amp; Conditions</a><a href="#">Shipping Policy</a>

        </div>

        <div class="footer-column app-column">

            <h3>Get the App</h3>

            <span class="app-badge">▶ Google Play</span>

            <span class="app-badge"> App Store</span>

        </div>

        <div class="footer-bottom">© 2026 ShopEase. All Rights Reserved.</div>

    </footer>


    <script>
    document.addEventListener("DOMContentLoaded", function () {
        const searchInput = document.getElementById("productSearch");
        const suggestionsBox = document.getElementById("searchSuggestions");
        let debounceTimer;

        searchInput.addEventListener("input", function () {
            const keyword = this.value.trim();
            clearTimeout(debounceTimer);

            if (keyword.length < 1) {
                suggestionsBox.style.display = "none";
                suggestionsBox.innerHTML = "";
                return;
            }

            debounceTimer = setTimeout(function () {
                fetch("${pageContext.request.contextPath}/search-suggestions?q=" + encodeURIComponent(keyword))
                    .then(response => {
                        if (!response.ok) throw new Error("Search request failed");
                        return response.json();
                    })
                    .then(products => {
                        suggestionsBox.innerHTML = "";
                        if (products.length === 0) {
                            suggestionsBox.style.display = "none";
                            return;
                        }
                        products.forEach(product => {
                            const suggestion = document.createElement("a");
                            suggestion.className = "suggestion-item";
                            suggestion.href = "${pageContext.request.contextPath}/product-details?id="
                                + encodeURIComponent(product.productId);
                            suggestion.textContent = product.productName;
                            suggestionsBox.appendChild(suggestion);
                        });
                        suggestionsBox.style.display = "block";
                    })
                    .catch(error => {
                        console.error("Autocomplete error:", error);
                        suggestionsBox.style.display = "none";
                    });
            }, 300);
        });

        document.addEventListener("click", function (event) {
            if (!event.target.closest(".search-container")) {
                suggestionsBox.style.display = "none";
            }
        });
    });
    </script>

</body>

</html>