<?php
require_once __DIR__ . '/includes/config.php';
require_once __DIR__ . '/includes/api_client.php';

$memberId = get_current_member_id();
if (!$memberId) {
    header('Location: /frontend/index.php');
    exit;
}

$zip      = trim($_GET['zip'] ?? $_POST['zip'] ?? '');
$expedite = (bool)($_GET['expedite'] ?? $_POST['expedite'] ?? false);
$success  = '';
$error    = '';
$orderId  = null;

// POST: submit the order
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['confirm_order'])) {
    $zip      = trim($_POST['zip'] ?? '');
    $expedite = (bool)($_POST['expedite'] ?? false);

    if (empty($zip)) {
        $error = 'ZIP code is required.';
    } else {
        // API call: POST /api/orders/submit/{memberId}?zip={zip}&expedite={bool}
        // Calls process_order() stored procedure which atomically:
        //   -> select_best_vendor() per item
        //   -> calculate_price() for final pricing
        //   -> apply_customer_discount() (writes discount_audit row)
        //   -> calculate_shipping()
        //   Clears draft cart and updates member total_spend
        $expediteStr = $expedite ? 'true' : 'false';
        $result = api_post("/orders/submit/{$memberId}?zip=" . urlencode($zip) . "&expedite={$expediteStr}");

        if ($result['code'] === 201 && isset($result['body']['orderId'])) {
            $orderId = (int)$result['body']['orderId'];
            $success = "Order #{$orderId} placed successfully! Thank you for your order.";
        } else {
            $errorBody = $result['body'] ?? [];
            $error = 'Order submission failed. ' . (is_string($errorBody) ? $errorBody : json_encode($errorBody));
        }
    }
}

// GET: show preview summary before confirmation
$preview = null;
if (!$orderId && $zip !== '') {
    // API call: GET /api/orders/cart/{memberId}/preview?zip={zip}&expedite={bool}
    // Mirrors process_order() logic without committing
    $expediteStr = $expedite ? 'true' : 'false';
    $preview = api_get("/orders/cart/{$memberId}/preview?zip=" . urlencode($zip) . "&expedite={$expediteStr}");
}

require_once __DIR__ . '/includes/header.php';
?>

<h2 style="margin-bottom:20px;">Checkout</h2>

<?php if ($success): ?>
    <div class="alert alert-success">
        <?php echo htmlspecialchars($success); ?>
        <br><a href="/frontend/index.php">Return to home</a>
    </div>
<?php elseif ($error): ?>
    <div class="alert alert-error"><?php echo htmlspecialchars($error); ?></div>
<?php endif; ?>

<?php if (!$orderId): ?>

<?php if ($preview && !empty($preview['items'])): ?>
<div class="card" style="margin-bottom:24px;">
    <h3 style="margin-bottom:14px;">Order Summary</h3>
    <table style="box-shadow:none; background:transparent; margin-bottom:16px;">
        <?php foreach ($preview['items'] as $item): ?>
        <tr>
            <td>Product #<?php echo (int)$item['productId']; ?> &times; <?php echo (int)$item['quantity']; ?></td>
            <td class="price"><?php echo format_currency($item['lineTotal']); ?></td>
        </tr>
        <?php endforeach; ?>
        <tr style="border-top:1px solid #ddd;"><td>Subtotal</td><td class="price"><?php echo format_currency($preview['subtotal']); ?></td></tr>
        <tr><td>Discount (<?php echo htmlspecialchars($_SESSION['member_tier'] ?? ''); ?> tier)</td><td class="price" style="color:#27ae60;">- <?php echo format_currency($preview['discountAmount']); ?></td></tr>
        <tr><td>Shipping to <?php echo htmlspecialchars($zip); ?><?php echo $expedite ? ' (Expedited)' : ''; ?></td><td class="price"><?php echo format_currency($preview['shippingCost']); ?></td></tr>
        <tr style="border-top:2px solid #1a3a5c;">
            <td><strong>Total</strong></td>
            <td class="price" style="font-size:1.1rem;"><strong><?php echo format_currency($preview['total']); ?></strong></td>
        </tr>
    </table>

    <form method="POST">
        <input type="hidden" name="zip"      value="<?php echo htmlspecialchars($zip); ?>">
        <input type="hidden" name="expedite" value="<?php echo $expedite ? '1' : '0'; ?>">
        <button type="submit" name="confirm_order" class="btn btn-success" style="font-size:1rem; padding:12px 28px;">
            Confirm &amp; Place Order
        </button>
        <a href="/frontend/cart.php" class="btn" style="margin-left:12px; background:#e0e7ef; color:#1a3a5c;">Back to Cart</a>
    </form>
</div>
<?php elseif ($zip): ?>
    <div class="alert alert-error">Could not load order preview. Your cart may be empty.</div>
    <a href="/frontend/cart.php" class="btn btn-primary">Back to Cart</a>
<?php else: ?>
    <div class="alert alert-error">No ZIP code provided.</div>
    <a href="/frontend/cart.php" class="btn btn-primary">Back to Cart</a>
<?php endif; ?>

<?php endif; // !$orderId ?>

<?php require_once __DIR__ . '/includes/footer.php'; ?>
