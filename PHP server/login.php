<?php
require_once __DIR__ . '/vendor/autoload.php';
$fb = new Facebook\Facebook([
  'app_id' => '1950897215194122', // Replace {app-id} with your app id
  'app_secret' => 'abca3a8bae8c67b147eb89295304267a',
  'default_graph_version' => 'v2.10',
  ]);

$helper = $fb->getRedirectLoginHelper();

$permissions = ['email']; // Optional permissions
$loginUrl = $helper->getLoginUrl('https://spartanrides.me/fb-callback.php', $permissions);

//<li><?php echo '<a href="' . htmlspecialchars($loginUrl) . '" target="_blank" >Log in with Facebook!</a>'; </li>
echo '<a href="' . htmlspecialchars($loginUrl) . '">Log in with Facebook!</a>';
?>
