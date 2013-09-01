$("#GoogleMapPage").on("pageshow", function (e) {
    var directionsView;
    var directionsService = new google.maps.DirectionsService();
    var map;
    directionsView = new google.maps.DirectionsRenderer();
    var center = new google.maps.LatLng(0, 0);
    var mapOptions = {
        zoom: 8,
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        center: center
    };
    map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
    directionsView.setMap(map);
    var start = "Kansas City";
    var end = "Yellowstone";
    var request = {
        origin: start,
        destination: end,
        travelMode: google.maps.DirectionsTravelMode.DRIVING
    };
    directionsService.route(request, function (response, status) {
        if (status == google.maps.DirectionsStatus.OK) {
            directionsView.setDirections(response);
        }
    });
});


$("#WeatherPage").on("pageshow", function (e) {
    $.ajax({url: 'http://api.wunderground.com/api/36b799dc821d5836/conditions/q/WY/Yellowstone.json',
        dataType: "jsonp",
        jsonpCallback: 'successCallback',
        async: true,
        beforeSend: function() {
            $.mobile.showPageLoadingMsg(true);
        },
        complete: function() {
            $.mobile.hidePageLoadingMsg();
        },
        success: function (result) {
            var temp = result.current_observation.temp_f;
            var icon = result.current_observation.icon_url;
            var weather = result.current_observation.weather;
            var curCond = "Currently in Yellowstone " + temp + "&deg; F and " + weather;
            var curIcon = "<img src='" + icon + "'>";
            $("#currentConditions").html(curCond);
            $("#currentIcon").html(curIcon);
        },
        error: function (request,error) {
            alert('Network error has occurred please try again!');
        }
    }); 
});
