<?php
require_once __DIR__ . '/includes/config.php';
require_once __DIR__ . '/includes/api_client.php';

$error   = '';
$success = '';

// Handle login form submission
if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['member_id'])) {
    $memberId = (int)$_POST['member_id'];
    // API call: GET /api/members/{id}  (standard kitchensink endpoint)
    // We use the member from session; for this demo we accept any valid seed member ID
    if ($memberId >= 1 && $memberId <= 3) {
        // Simulate fetching member details — in a full app, call /api/members/{id}
        $memberData = [
            1 => ['name' => 'Jane Smith',    'tier' => 'GOLD'],
            2 => ['name' => 'Robert Torres', 'tier' => 'SILVER'],
            3 => ['name' => 'Emily Chen',    'tier' => 'BRONZE'],
        ];
        set_current_member($memberId, $memberData[$memberId]['name'], $memberData[$memberId]['tier']);
        $success = 'Logged in as ' . $memberData[$memberId]['name'] . ' (' . $memberData[$memberId]['tier'] . ')';
    } else {
        $error = 'Invalid member ID. Use 1, 2, or 3 for demo.';
    }
}

// API call: GET /api/products  — fetch featured products for homepage
// Returns all products; we display first 6 as "featured"
$allProducts = api_get('/products') ?? [];
$featured    = array_slice($allProducts, 0, 6);

require_once __DIR__ . '/includes/header.php';
?>

<?php if ($error): ?>
    <div class="alert alert-error"><?php echo htmlspecialchars($error); ?></div>
<?php endif; ?>
<?php if ($success): ?>
    <div class="alert alert-success"><?php echo htmlspecialchars($success); ?></div>
<?php endif; ?>

<div class="card">
    <h2 style="margin-bottom:16px;">Welcome to <?php echo htmlspecialchars(APP_NAME); ?></h2>
    <p style="margin-bottom:20px; color:#555;">Professional dental supplies, competitively priced. Log in with your member ID to see personalized pricing and loyalty discounts.</p>

    <?php if (!get_current_member_id()): ?>
    <form method="POST" style="max-width:300px;">
        <label for="member_id">Member ID (demo: 1, 2, or 3)</label>
        <input type="number" id="member_id" name="member_id" min="1" placeholder="Enter member ID" required>
        <button type="submit" class="btn btn-primary">Log In</button>
    </form>
    <?php else: ?>
    <p><a href="/frontend/catalog.php" class="btn btn-primary">Browse Full Catalog</a></p>
    <?php endif; ?>
</div>

<h3 style="margin-bottom:16px;">Featured Products</h3>
<?php if (empty($featured)): ?>
    <p style="color:#888;">No products available. Ensure the API is running.</p>
<?php else: ?>
<div style="display:grid; grid-template-columns: repeat(auto-fill, minmax(280px,1fr)); gap:16px;">
    <?php foreach ($featured as $product): ?>
    <div class="card" style="margin-bottom:0;">
        <div style="font-size:0.75rem; color:#888; text-transform:uppercase; margin-bottom:6px;">
            <?php echo htmlspecialchars($product['category'] ?? 'General'); ?>
        </div>
        <h4 style="margin-bottom:8px; font-size:1rem;"><?php echo htmlspecialchars($product['name']); ?></h4>
        <div style="font-size:0.8rem; color:#666; margin-bottom:8px;">SKU: <?php echo htmlspecialchars($product['sku']); ?></div>
        <div class="price" style="font-size:1.1rem; margin-bottom:14px;">
            <?php echo format_currency($product['basePrice']); ?>
        </div>
        <a href="/frontend/product.php?id=<?php echo (int)$product['id']; ?>" class="btn btn-primary">View Details</a>
    </div>
    <?php endforeach; ?>
</div>
<?php endif; ?>

<?php require_once __DIR__ . '/includes/footer.php'; ?>
