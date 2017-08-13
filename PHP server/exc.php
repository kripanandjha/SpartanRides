<?php
$data1 = file_get_contents('php://input');
$data = json_decode($data1, true);
// This is the data you want to pass to Python
//$data = array();
// Execute the python script with the JSON data
$result = shell_exec('python /var/www/spartanrides.me/fb_get_location.py ');

// Decode the result
// This will contain: array('status' => 'Yes!')
$connect = mysqli_connect("localhost", "user", "password", "database");
$res = "SELECT username, email, ph_url, FBID, source, destination, date, time  FROM post1 where FBID IN ('$result')";
$res = mysqli_query($connect, $res);
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
}
//echo json_encode($data);
$output = json_encode(array('post_data' => $data));
echo ($output);
mysqli_close($connect);
?>
