$(document).ready(function() {
  $("#buttonIn").click(function() 
  {
      var input = $("#input").val();
      $("#log").val("");
      $.ajax
      ({
          type: "POST",
          url: "http://vhost0844.dc1.on.ca.compute.ihost.com:18080/HDFRestWS/jaxrs/generic/hadoopPut/inputDir" + input,
          data: 'url',
          contentType: "application/json; charset=utf-8",
          dataType: "jsonp",
          success: function (data)
          {
            $("#log").val("Called Hadoop successfully!");
          }
      });
  });

  $("#buttonOut").click(function() 
  {
      var output = $("#output").val();
      $("#log").val("");
      $.ajax
      ({
          type: "POST",
          url: "http://vhost0844.dc1.on.ca.compute.ihost.com:18080/HDFRestWS/jaxrs/generic/hadoopPut/outputDir" + output,
          data: 'url',
          contentType: "application/json; charset=utf-8",
          dataType: "jsonp",
          success: function (data)
          {
            $("#log").val("Hadoop output: " + data);
          }
      });
  });
});
