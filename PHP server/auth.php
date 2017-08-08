<?php
$data1 = file_get_contents('php://input');
$data = json_decode($data1, true);


$response = array();
$response["success"] = true;
echo json_encode($response);


?>
