<?php

$connect = mysqli_connect("localhost", "user", "password", "database");
if ($connect->connect_errno) {
    // The connection failed. What do you want to do? 
    // You could contact yourself (email?), log the error, show a nice page, etc.
    // You do not want to reveal sensitive information

    // Let's try this:
    echo "Sorry, this website is experiencing problems.";

    // Something you should not do on a public site, but this example will show you
    // anyways, is print out MySQL error related information -- you might log this
    echo "Error: Failed to make a MySQL connection, here is why: \n";
    echo "Errno: " . $mysqli->connect_errno . "\n";
    echo "Error: " . $mysqli->connect_error . "\n";
    
    // You might want to show them something nice, but we will simply exit
    exit;
}
$res = mysqli_query($connect, "SELECT username, email, ph_url, FBID, source, destination, date, time  FROM post1");
//$numrows = mysql_num_rows($res);
//while($row = mysql_fetch_assoc($res))
//{
//print_r($row);
//}
if($res) 
{
    while($row = mysqli_fetch_array($res))
    {
	$data[] = $row;
    }
//print(json_encode($data));
//print_r($data);
}
//echo json_encode($data);
$output = json_encode(array('post_data' => $data));
echo ($output);
mysqli_close($connect);
?>
