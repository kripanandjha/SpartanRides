<?php
$data1 = file_get_contents('php://input');
$data = json_decode($data1, true);
error_log(print_r($data, TRUE));

$Rating = $data["Rating"];
$Suggestion = $data["Suggestion"];
$Username = $data["Username"];

$connect = mysqli_connect("localhost", "user", "password", "database");
$statement = mysqli_prepare($connect, "INSERT INTO suggest (username, rating, suggestion) VALUES (?, ?, ?)");
mysqli_stmt_bind_param($statement, "sis", $Username, $Rating, $Suggestion);
mysqli_stmt_execute($statement);
mysqli_stmt_close($statement);
$response = array();
$response["success"] = true;
echo json_encode($response);
?>
