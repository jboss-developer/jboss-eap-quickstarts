<?php
require_once __DIR__ . '/includes/config.php';
require_once __DIR__ . '/includes/api_client.php';

$productId = (int)($_GET['id'] ?? 0);
if ($productId <= 0) {
    header('Location: /frontend/catalog.php');
    exit;
}

$quantity  = max(1, (int)($_GET['qty'] ?? 1));
$addedMsg  = '';
$addError  = '';

// Handle add-to-cart form
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['add_to_cart'])) {
    $memberId = get_current_member_id();
    if (!$memberId) {
        $addError = 'Please log in to add items to your cart.';
    } else {
        $qty    = max(1, (int)($_POST['quantity'] ?? 1));
        // API call: POST /api/orders/cart/{memberId}
        // Body: { "productId": N, "quantity": N }
        $result = api_post("/orders/cart/{$memberId}", [
            'productId' => $productId,
            'quantity'  => $qty,
        ]);
        if ($result['code'] === 201) {
            $addedMsg = 'Item added to cart successfully.';
        } else {
            $addError = 'Could not add item to cart. Please try again.';
        }
    }
}

// API call: GET /api/products/{id}
$product = api_get("/products/{$productId}");
if (!$product) {
    header('Location: /frontend/catalog.php');
    exit;
}

// API call: GET /api/products/{id}/vendors?quantity={qty}
// Returns VendorPriceResult list sorted by unit price ascending
$vendors = api_get("/products/{$productId}/vendors?quantity={$quantity}") ?? [];

// API call (if logged in): GET /api/products/{id}/price?vendorId={v}&quantity={qty}
// Used to show personalised pricing — we show it per vendor row instead
$memberId = get_current_member_id();

require_once __DIR__ . '/includes/header.php';
?>

<?php if ($addedMsg): ?><div class="alert alert-success"><?php echo htmlspecialchars($addedMsg); ?></div><?php endif; ?>
<?php if ($addError):  ?><div class="alert alert-error"><?php echo htmlspecialchars($addError); ?></div><?php endif; ?>

<div style="display:grid; grid-template-columns: 1fr 320px; gap:24px; align-items:start;">
    <!-- Product details -->
    <div class="card">
        <div style="font-size:0.8rem; color:#888; text-transform:uppercase; margin-bottom:6px;">
            <?php echo htmlspecialchars($product['category'] ?? 'General'); ?>
        </div>
        <h2 style="margin-bottom:8px;"><?php echo htmlspecialchars($product['name']); ?></h2>
        <p style="margin-bottom:6px; color:#555;">SKU: <strong><?php echo htmlspecialchars($product['sku']); ?></strong></p>
        <p style="margin-bottom:6px; color:#555;">Weight: <strong><?php echo htmlspecialchars($product['weightLbs'] ?? '—'); ?> lbs</strong></p>
        <p class="price" style="font-size:1.3rem; margin-top:12px;">
            Base price: <?php echo format_currency($product['basePrice']); ?>
        </p>
    </div>

    <!-- Add to cart -->
    <div class="card">
        <h3 style="margin-bottom:16px;">Add to Cart</h3>
        <form method="POST">
            <label for="quantity">Quantity</label>
            <input type="number" id="quantity" name="quantity" min="1" value="<?php echo $quantity; ?>"
                   oninput="this.form.submit()" style="margin-bottom:8px;">
            <input type="hidden" name="qty_nav" value="1">
            <?php if ($memberId): ?>
                <button type="submit" name="add_to_cart" class="btn btn-success" style="width:100%;">
                    Add to Cart
                </button>
            <?php else: ?>
                <p style="color:#888; font-size:0.9rem;">Log in to add to cart.</p>
            <?php endif; ?>
        </form>
    </div>
</div>

<!-- Vendor pricing table -->
<div class="card" style="margin-top:24px;">
    <h3 style="margin-bottom:16px;">
        Vendor Pricing for Qty <?php echo $quantity; ?>
        <!-- API call: GET /api/products/{id}/vendors?quantity={qty} -->
        <!-- Returns vendors ranked by price (cheapest first) from VendorSelectionService -->
    </h3>
    <?php if (empty($vendors)): ?>
        <p style="color:#888;">No vendor pricing available.</p>
    <?php else: ?>
    <table>
        <thead>
            <tr>
                <th>Vendor</th>
                <th>Unit Price</th>
                <th>Fulfillment Rating</th>
                <th>Avg Ship Days</th>
            </tr>
        </thead>
        <tbody>
            <?php foreach ($vendors as $i => $v): ?>
            <tr <?php echo $i === 0 ? 'style="background:#eaf5ea;"' : ''; ?>>
                <td>
                    <?php echo htmlspecialchars($v['vendorName']); ?>
                    <?php if ($i === 0): ?>
                        <span style="background:#27ae60; color:#fff; font-size:0.7rem; padding:2px 7px; border-radius:10px; margin-left:6px;">Best</span>
                    <?php endif; ?>
                </td>
                <td class="price"><?php echo format_currency($v['unitPrice']); ?></td>
                <td><?php echo htmlspecialchars($v['fulfillmentRating']); ?> / 5.0</td>
                <td><?php echo htmlspecialchars($v['avgShippingDays']); ?> days</td>
            </tr>
            <?php endforeach; ?>
        </tbody>
    </table>
    <?php endif; ?>
</div>

<?php if ($memberId): ?>
<!-- Discount preview for logged-in members -->
<!-- API call chain: PricingService.calculateLineTotal() + DiscountService.calculateDiscount() -->
<!-- Shown as informational; actual discount applied at checkout via apply_customer_discount() stored proc -->
<div class="card" style="margin-top:16px; border-left: 4px solid #ffd700;">
    <p style="color:#555; font-size:0.9rem;">
        <strong>Your <?php echo htmlspecialchars($_SESSION['member_tier']); ?> member discount</strong>
        will be applied automatically at checkout. Use the cart preview to see exact savings.
    </p>
</div>
<?php endif; ?>

<?php require_once __DIR__ . '/includes/footer.php'; ?>
