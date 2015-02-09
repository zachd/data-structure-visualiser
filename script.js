var dataset;

// Load JSON from file
d3.json("test.json", function(error, json) {
  if (error) return console.warn(error);
  dataset = json
  visualise(1);
});

// Dimension variables
var box_size = {w: 110, h:50};
var array_padding = 10;
var text_padding = {x: 10, y:10};

//Create SVG container
var svg = d3.select("body")
      .append("svg")
      .attr("width", 500)
      .attr("height", 500);

// Visualise array function
function visualise(num){
    
    // Remove all SVG elements from container
    svg.selectAll("*").remove();
    
    // Check if the snapshot is of type ARRAY
    if(dataset.snapshots[num].type === "ARRAY"){
        
        // Create SVG grouping element for each data item
        var elem = svg.selectAll("g")
            .data(dataset.snapshots[num].data)
            .enter().append("g");
        
        // Add rectangular box to group
        elem.append("rect")
            .attr("width", box_size.w)
            .attr("height", box_size.h)
            .attr("x", function(d, i){
                return (i * box_size.w + 
                    (i > 0 ? i * array_padding : 0));})
        
        // Add text to group
        elem.append("text")
            .attr("x", function(d) { return 100; })
            .attr("y", 10 / 2)
            .attr("dy", ".35em")
            .attr("fill", "white")
            .attr("x", function(d, i){
                return (i * box_size.w + text_padding.x + 
                    (i > 0 ? i * array_padding : 0));})
            .attr("y", text_padding.y)
            .text(function(d) { return d.value; });
    }
}
