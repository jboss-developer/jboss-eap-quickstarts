<?php
// header.php — included on every page
$currentMemberId   = get_current_member_id();
$currentMemberName = $_SESSION['member_name'] ?? null;
$currentMemberTier = $_SESSION['member_tier'] ?? null;

$tierBadgeColors = [
    'BRONZE'   => '#cd7f32',
    'SILVER'   => '#a8a9ad',
    'GOLD'     => '#ffd700',
    'PLATINUM' => '#e5e4e2',
];
$tierColor = $tierBadgeColors[$currentMemberTier] ?? '#cd7f32';
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo htmlspecialchars(APP_NAME); ?></title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f4f6f9; color: #333; }
        header { background: #1a3a5c; color: #fff; padding: 0 24px; display: flex; align-items: center; justify-content: space-between; height: 60px; }
        header .brand { font-size: 1.4rem; font-weight: 700; letter-spacing: 0.5px; }
        nav a { color: #cce0f5; text-decoration: none; margin-left: 20px; font-size: 0.95rem; }
        nav a:hover { color: #fff; text-decoration: underline; }
        .tier-badge { display: inline-block; padding: 3px 10px; border-radius: 12px; font-size: 0.75rem; font-weight: 700; color: #333; }
        .member-info { display: flex; align-items: center; gap: 10px; font-size: 0.9rem; }
        main { max-width: 1200px; margin: 30px auto; padding: 0 20px; }
        .alert { padding: 12px 18px; border-radius: 6px; margin-bottom: 18px; }
        .alert-success { background: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .alert-error   { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        table { width: 100%; border-collapse: collapse; background: #fff; border-radius: 8px; overflow: hidden; box-shadow: 0 1px 4px rgba(0,0,0,.08); }
        th, td { padding: 12px 16px; text-align: left; }
        thead { background: #1a3a5c; color: #fff; }
        tr:nth-child(even) { background: #f9fafc; }
        .btn { display: inline-block; padding: 8px 18px; border-radius: 5px; border: none; cursor: pointer; font-size: 0.9rem; text-decoration: none; }
        .btn-primary { background: #1a3a5c; color: #fff; }
        .btn-primary:hover { background: #254d7a; }
        .btn-danger  { background: #c0392b; color: #fff; }
        .btn-danger:hover  { background: #a93226; }
        .btn-success { background: #27ae60; color: #fff; }
        .btn-success:hover { background: #219a52; }
        .card { background: #fff; border-radius: 8px; box-shadow: 0 1px 4px rgba(0,0,0,.08); padding: 24px; margin-bottom: 20px; }
        label { display: block; margin-bottom: 5px; font-weight: 600; font-size: 0.9rem; }
        input, select { width: 100%; padding: 8px 12px; border: 1px solid #ccc; border-radius: 5px; font-size: 0.95rem; margin-bottom: 14px; }
        .price { font-weight: 700; color: #1a3a5c; }
    </style>
</head>
<body>
<header>
    <div class="brand"><?php echo htmlspecialchars(APP_NAME); ?></div>
    <nav>
        <a href="/frontend/index.php">Home</a>
        <a href="/frontend/catalog.php">Catalog</a>
        <a href="/frontend/cart.php">Cart</a>
        <?php if ($currentMemberId): ?>
            <a href="/frontend/checkout.php">Checkout</a>
        <?php endif; ?>
    </nav>
    <div class="member-info">
        <?php if ($currentMemberId && $currentMemberName): ?>
            <span><?php echo htmlspecialchars($currentMemberName); ?></span>
            <span class="tier-badge" style="background: <?php echo $tierColor; ?>">
                <?php echo htmlspecialchars($currentMemberTier); ?>
            </span>
        <?php else: ?>
            <a href="/frontend/index.php" style="color:#cce0f5;">Login</a>
        <?php endif; ?>
    </div>
</header>
<main>
