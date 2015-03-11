var dataset;
var current;

// Load JSON from file
d3.json("test.json", function(error, json) {
  if (error) return console.warn(error);
  dataset = json
  current = -1;
  next(true);
});

// Dimension variables
var box_size = {w: 110, h:50};
var array_padding = 10;
var text_padding = {x: 10, y:10};
var width = 500;
var height =250;
var myInterval;
var highlightedColor = d3.rgb("rgb(255,247,0 )")
var defaultColor = d3.rgb("rgb(135,206,235)")
var textColor = d3.rgb("rgb(0,0,0")

//Create SVG container
var svg = d3.select("#canvas")
      .append("svg")
      .attr("width", width)
      .attr("height", height);

// Visualise array function
function visualise(num){
    
    // Remove all SVG elements from container
    svg.selectAll("*").remove();
    
    // Check if the snapshot is of type ARRAY
    switch (dataset.snapshots[num].type) {
      case "ARRAY":
        // Create SVG grouping element for each data item
        var elem = svg.selectAll("g")
            .data(dataset.snapshots[num].data)
            .enter().append("g");
        
        // Add rectangular box to group
        elem.append("rect")
            .attr("width", box_size.w)
            .attr("height", box_size.h)
            .attr("fill", function(d) { return (d.highlighted) ? highlightedColor : defaultColor;})
            .attr("x", function(d, i){
                return (i * box_size.w + 
                    (i > 0 ? i * array_padding : 0));})
        
        // Add text to group
        elem.append("text")
            .attr("x", function(d) { return 100; })
            .attr("y", 10 / 2)
            .attr("dy", ".35em")
            .attr("fill", textColor)
            .attr("x", function(d, i){
                return (i * box_size.w + text_padding.x + 
                    (i > 0 ? i * array_padding : 0));})
            .attr("y", text_padding.y)
            .text(function(d) { return d.value; });
      break;
      case "BINARY-TREE":
        
        // Initialise tree variables
        var tree = d3.layout.tree()
          .size([height, width - 160]);
        var diagonal = d3.svg.diagonal()
          .projection(function(d) { return [d.y, d.x]; });
        var treecontainer = svg.append("g")
          .attr("transform", "translate(40,0)");
        
        // Initialise tree nodes and links from data set
        var nodes = tree.nodes(dataset.snapshots[num].data),
          links = tree.links(nodes);

        // Create link elements from link array
        var link = treecontainer.selectAll("path.link")
          .data(links)
          .enter().append("path")
          .attr("class", "link")
          .attr("d", diagonal);
        
        // Create node elements from nodes array
        var node = treecontainer.selectAll("g.node")
          .data(nodes)
          .enter().append("g")
          .attr("class", "node")
          .attr("transform", function(d) { 
            return "translate(" + d.y + "," + d.x + ")"; 
          })

        // Add circle with radius 4.5 to node elements
        node.append("circle")
          .attr("r", 4.5);

        // Add text to node elements
        node.append("text")
          .attr("dx", function(d) { return d.children ? -8 : 8; })
          .attr("dy", 3)
          .attr("text-anchor", function(d) { return d.children ? "end" : "start"; })
          .text(function(d) { return d.value; });
        
        d3.select(self.frameElement).style("height", height + "px");
      break;
    }
}

function next(forward){
  // Change current counter to desired number
  current = current + (forward ? 1 : -1);
  
  // Disable buttons if at the end of the snapshots array
  d3.selectAll(".pagination").attr("disabled", null);
  if(current == dataset.snapshots.length - 1){
    d3.select("#next").attr("disabled", "disabled");
    d3.select("#play").attr("disabled", "disabled");
    stopInterval();
  }
  else if(current == 0)
    d3.select("#previous").attr("disabled", "disabled");
  
  // Visualise desired number
  visualise(current);
}

function play(){
  //get the speed from the input slider
  speed = d3.select("#speed").property("value");
  //call next with the desired interval
  myInterval = setInterval(function () {next(true)}, 100*speed);
}

function reset(){
  //reset current to the first snapshot
  current = 0;
  
  //enable/disable relevant buttons
  d3.selectAll(".pagination").attr("disabled", null);
  d3.select("#previous").attr("disabled", "disabled");
  
  //visualise first snapshot
  visualise(current);
}

function stopInterval(){
  clearInterval(myInterval);
}

