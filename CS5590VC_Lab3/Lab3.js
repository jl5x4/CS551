$(document).ready(function() {
  $("#button").click(function() 
  {
      var input = $("#input").val();
      $("#log_search").val("");
      $.ajax
      ({
          type: "POST",
          url: "http://vhost1264.dc1.on.ca.compute.ihost.com:8080/VC_Lab5_Web/jaxrs/mypath/greeting/" + input,
          data: 'url',
          contentType: "application/json; charset=utf-8",
          dataType: "jsonp",
          success: function (data)
          {
            $("#log").val("Country:"+data.Country+","+"State:"+data.State+","+"City:"+data.city+","+ "Name:"+data.name);
            $.ajax
            ({
                type: "GET",
                url: "https://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=" + input,
                data: 'url',
                contentType: "application/json; charset=utf-8",
                dataType: "jsonp",
                success: function (data)
                {
                   $("#log_search").val(function(index, val) 
                   {
                      $.each(data.responseData.results,function(i,rows)
                      {
                         val = val + rows.url;
                         return false;                         
                      });
                       updateTwitterValues(val, "Tweet URL ");
                      return val;
                   });
                    
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
                    var end = ";
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
                }
            });              
          }
      });
  });

  function updateTwitterValues(share_url, title) 
  {
    $("#twitter-share-section").html('&nbsp;'); 
    $("#twitter-share-section").html('<a href="https://twitter.com/share" class="twitter-share-button" data-url="' +    share_url +'" data-size="large" data-text="' + title + '" data-count="none">Tweet</a>');
  twttr.widgets.load();
  }
});
