<?php
require_once __DIR__ . '/includes/config.php';
require_once __DIR__ . '/includes/api_client.php';

$memberId = get_current_member_id();
$message  = '';
$error    = '';

if (!$memberId) {
    header('Location: /frontend/index.php');
    exit;
}

// Handle remove-from-cart
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['remove_product_id'])) {
    $removeId = (int)$_POST['remove_product_id'];
    // API call: DELETE /api/orders/cart/{memberId}/{productId}
    $result = api_delete("/orders/cart/{$memberId}/{$removeId}");
    if ($result['code'] < 300) {
        $message = 'Item removed from cart.';
    } else {
        $error = 'Could not remove item.';
    }
}

// Fetch preview if zip provided
$zip      = trim($_GET['zip'] ?? $_POST['zip'] ?? '');
$expedite = isset($_GET['expedite']) ? (bool)$_GET['expedite'] : false;
$preview  = null;

if ($zip !== '') {
    // API call: GET /api/orders/cart/{memberId}/preview?zip={zip}&expedite={bool}
    // Calls OrderService.previewOrder() which mirrors process_order() stored procedure logic
    // Chain: VendorSelectionService.selectBestVendor() -> PricingService.calculatePrice()
    //        -> DiscountService.calculateDiscount() -> ShippingService.estimateShipping()
    $expediteStr = $expedite ? 'true' : 'false';
    $preview = api_get("/orders/cart/{$memberId}/preview?zip=" . urlencode($zip) . "&expedite={$expediteStr}");
}

require_once __DIR__ . '/includes/header.php';
?>

<?php if ($message): ?><div class="alert alert-success"><?php echo htmlspecialchars($message); ?></div><?php endif; ?>
<?php if ($error):   ?><div class="alert alert-error"><?php echo htmlspecialchars($error); ?></div><?php endif; ?>

<h2 style="margin-bottom:20px;">Your Cart</h2>

<?php if ($preview && !empty($preview['items'])): ?>
<table style="margin-bottom:24px;">
    <thead>
        <tr>
            <th>Product ID</th>
            <th>Vendor ID</th>
            <th>Qty</th>
            <th>Unit Price</th>
            <th>Line Total</th>
            <th>Action</th>
        </tr>
    </thead>
    <tbody>
        <?php foreach ($preview['items'] as $item): ?>
        <tr>
            <td><?php echo (int)$item['productId']; ?></td>
            <td><?php echo (int)$item['vendorId']; ?></td>
            <td><?php echo (int)$item['quantity']; ?></td>
            <td class="price"><?php echo format_currency($item['unitPrice']); ?></td>
            <td class="price"><?php echo format_currency($item['lineTotal']); ?></td>
            <td>
                <form method="POST" style="display:inline;">
                    <input type="hidden" name="remove_product_id" value="<?php echo (int)$item['productId']; ?>">
                    <input type="hidden" name="zip" value="<?php echo htmlspecialchars($zip); ?>">
                    <button type="submit" class="btn btn-danger" style="padding:4px 10px; font-size:0.8rem;">Remove</button>
                </form>
            </td>
        </tr>
        <?php endforeach; ?>
    </tbody>
</table>

<!-- Order summary -->
<div class="card" style="max-width:380px; margin-bottom:24px;">
    <h3 style="margin-bottom:14px;">Order Summary</h3>
    <table style="box-shadow:none; background:transparent;">
        <tr><td>Subtotal</td><td class="price"><?php echo format_currency($preview['subtotal']); ?></td></tr>
        <tr><td>Discount</td><td class="price" style="color:#27ae60;">- <?php echo format_currency($preview['discountAmount']); ?></td></tr>
        <tr><td>Shipping<?php echo $expedite ? ' (Expedited)' : ''; ?></td><td class="price"><?php echo format_currency($preview['shippingCost']); ?></td></tr>
        <tr style="border-top:2px solid #1a3a5c;">
            <td><strong>Total</strong></td>
            <td class="price" style="font-size:1.1rem;"><strong><?php echo format_currency($preview['total']); ?></strong></td>
        </tr>
    </table>
</div>
<?php elseif ($preview): ?>
    <p style="color:#888; margin-bottom:20px;">Your cart is empty or all items have no available vendors.</p>
<?php endif; ?>

<!-- Preview / shipping form -->
<div class="card" style="max-width:480px; margin-bottom:24px;">
    <h3 style="margin-bottom:14px;">Get Shipping Estimate</h3>
    <form method="GET" id="previewForm">
        <label for="zip">Destination ZIP Code</label>
        <input type="text" id="zip" name="zip" maxlength="5" value="<?php echo htmlspecialchars($zip); ?>"
               placeholder="e.g. 27601">
        <label style="display:flex; align-items:center; gap:8px; font-weight:normal; margin-bottom:14px;">
            <input type="checkbox" name="expedite" value="1" <?php echo $expedite ? 'checked' : ''; ?>
                   style="width:auto; margin:0;"> Expedited shipping (2.5x rate)
        </label>
        <button type="submit" class="btn btn-primary">Preview Order</button>
    </form>
</div>

<!-- Live preview refresh via JS fetch -->
<script>
// Live preview: refresh order total when ZIP changes without full page reload
document.getElementById('zip').addEventListener('change', function() {
    const zip = this.value.trim();
    if (zip.length !== 5) return;
    const expedite = document.querySelector('[name="expedite"]').checked ? 'true' : 'false';
    // Fetch: GET /api/orders/cart/{memberId}/preview?zip={zip}&expedite={bool}
    fetch(`<?php echo API_BASE_URL; ?>/orders/cart/<?php echo (int)$memberId; ?>/preview?zip=${encodeURIComponent(zip)}&expedite=${expedite}`, {
        headers: { 'Accept': 'application/json' }
    })
    .then(r => r.ok ? r.json() : null)
    .then(data => {
        if (!data) return;
        // Reload the page to show updated preview
        window.location.href = `/frontend/cart.php?zip=${encodeURIComponent(zip)}&expedite=${expedite}`;
    })
    .catch(() => {});
});
</script>

<?php if ($preview && !empty($preview['items'])): ?>
<a href="/frontend/checkout.php?zip=<?php echo urlencode($zip); ?>&expedite=<?php echo $expedite ? '1' : '0'; ?>"
   class="btn btn-success">Proceed to Checkout</a>
<?php endif; ?>

<?php require_once __DIR__ . '/includes/footer.php'; ?>
