<?php
require_once __DIR__ . '/includes/config.php';
require_once __DIR__ . '/includes/api_client.php';

$selectedCategory = $_GET['category'] ?? '';

// API call chain:
// 1. GET /api/products              — fetch all products (for category list)
// 2. GET /api/products/category/{c} — fetch filtered if category selected
$allProducts = api_get('/products') ?? [];

// Build category list from all products
$categories = [];
foreach ($allProducts as $p) {
    if (!empty($p['category']) && !in_array($p['category'], $categories)) {
        $categories[] = $p['category'];
    }
}
sort($categories);

// Fetch filtered products if category selected, otherwise show all
if ($selectedCategory !== '') {
    // API call: GET /api/products/category/{category}
    $products = api_get('/products/category/' . urlencode($selectedCategory)) ?? [];
} else {
    $products = $allProducts;
}

require_once __DIR__ . '/includes/header.php';
?>

<h2 style="margin-bottom:20px;">Product Catalog</h2>

<!-- Category filter bar -->
<div class="card" style="padding:14px 20px; margin-bottom:20px; display:flex; align-items:center; gap:12px; flex-wrap:wrap;">
    <strong>Filter by category:</strong>
    <a href="/frontend/catalog.php"
       class="btn <?php echo $selectedCategory === '' ? 'btn-primary' : ''; ?>"
       style="<?php echo $selectedCategory !== '' ? 'background:#e0e7ef; color:#1a3a5c;' : ''; ?>">
        All
    </a>
    <?php foreach ($categories as $cat): ?>
    <a href="/frontend/catalog.php?category=<?php echo urlencode($cat); ?>"
       class="btn <?php echo $selectedCategory === $cat ? 'btn-primary' : ''; ?>"
       style="<?php echo $selectedCategory !== $cat ? 'background:#e0e7ef; color:#1a3a5c;' : ''; ?>">
        <?php echo htmlspecialchars($cat); ?>
    </a>
    <?php endforeach; ?>
</div>

<?php if (empty($products)): ?>
    <p style="color:#888;">No products found<?php echo $selectedCategory ? ' in category "' . htmlspecialchars($selectedCategory) . '"' : ''; ?>.</p>
<?php else: ?>
<table>
    <thead>
        <tr>
            <th>SKU</th>
            <th>Name</th>
            <th>Category</th>
            <th>Base Price</th>
            <th>Weight (lbs)</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <?php foreach ($products as $product): ?>
        <tr>
            <td><code><?php echo htmlspecialchars($product['sku']); ?></code></td>
            <td><?php echo htmlspecialchars($product['name']); ?></td>
            <td><?php echo htmlspecialchars($product['category'] ?? '—'); ?></td>
            <td class="price"><?php echo format_currency($product['basePrice']); ?></td>
            <td><?php echo htmlspecialchars($product['weightLbs'] ?? '—'); ?></td>
            <td>
                <a href="/frontend/product.php?id=<?php echo (int)$product['id']; ?>"
                   class="btn btn-primary" style="padding:5px 12px; font-size:0.85rem;">
                    Details
                </a>
            </td>
        </tr>
        <?php endforeach; ?>
    </tbody>
</table>
<?php endif; ?>

<?php require_once __DIR__ . '/includes/footer.php'; ?>
