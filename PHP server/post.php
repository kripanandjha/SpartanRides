<?php

$data1 = file_get_contents('php://input');
$data = json_decode($data1, true);
$Source = $data["Source"];
$Destination = $data["Destination"];
$Date = $data["Date"];
$Time = $data["Time"];
$Username = $data['Username'];



$connect = mysqli_connect("localhost", "user", "password", "database");
$statement = mysqli_prepare($connect, "INSERT INTO post (username, source, destination, date, time) VALUES (?, ?, ?, ?, ?)");
mysqli_stmt_bind_param($statement, "sssss", $Username, $Source, $Destination, $Date, $Time);
mysqli_stmt_execute($statement);
mysqli_stmt_close($statement);     
$response = array();
$response["success"] = true;
echo json_encode($response);
?>
