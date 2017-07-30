<!DOCTYPE html>
<html>

<body>

<div id="map" style="width:100%;height:900px"></div>
<style>.fullscreen {
    width: 100%;
    height: 100%;
}
</style>
<script>
function myMap() {
  var mapCanvas = document.getElementById("map");
  var myCenter = new google.maps.LatLng(37.346078, -121.902565); 
  var mapOptions = {center: myCenter, zoom: 5};
  var map = new google.maps.Map(mapCanvas,mapOptions);
  var marker = new google.maps.Marker({
    position: myCenter,
    animation: google.maps.Animation.BOUNCE
  });
  marker.setMap(map);
zoomControl: true;
}
</script>

<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBbsDKDvwwtw1SHFI9lm8dYQkcq6sQHe-I&callback=myMap"></script>
<!--
To use this code on your website, get a free API key from Google.
Read more at: https://www.w3schools.com/graphics/google_maps_basic.asp
-->

</body>
</html>

