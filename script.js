var dataset;
var current;

// Load JSON from file
function load(filename){
  d3.json(filename, function(error, json) {
    if (error) return console.warn(error);
    dataset = json
    d3.select("#snapshot-slider").attr("max",
      dataset["snapshots"].length - 1);
    current = -1;
    next(true);
  });
}

/* Max func: http://ejohn.org/blog/fast-javascript-maxmin/ */
Array.max = function( array ){
    return Math.max.apply( Math, array );
};

// Dimension variables
var box_size = {w: 70, h:50};
var array_padding = 8;
var text_padding = {x: 8, y:8};
var width = 500;
var height = 250;
var playing = false;
var myInterval;

// Color variables
var defaultColor = d3.rgb("rgb(135,206,235)");
var orange = d3.rgb("rgb(250,198,85)");
var textColor = d3.rgb("rgb(255,255,255)");
var weightedColors = false;

//Create SVG container
var svg = d3.select("#canvas")
      .append("svg")
      .attr("width", width)
      .attr("height", height);


// Visualise array function
function visualise(num){
    
    // Remove all SVG elements from container
    svg.selectAll("*").remove();
    
    // Snapshot variable
    var snapshot = dataset.snapshots[num];
  
    // Show snapshot number in corner
    d3.select("#snapshot-counter")
      .html("type: " + snapshot.type + "<Br />snapshot: " + num);
    
    // Update snapshot progress slider
    d3.select("#snapshot-slider").property("value", num);
  
    // Check if the snapshot is of type ARRAY
    switch (snapshot.type) {
      case "ARRAY":
      case "2D-ARRAY":
        
        // Get max array size
        var firstd = snapshot.data.length;
        var twoddata = snapshot.data.slice();
        var secondd = (snapshot.type == "2D-ARRAY" ? twoddata.map(function(s){return s.length;}) : null);
        
        
        // Set width of canvas dependent on size
        d3.select("#canvas svg")
          .attr("width", (array_padding + 
              ((secondd ? Array.max(secondd) : firstd) * (box_size.w +array_padding))));
                
        // Set height of canvas dependent on 2D size
        var twodheight = (array_padding + (firstd * (box_size.h + array_padding)));
        d3.select("#canvas svg")
          .attr("height", (secondd && twodheight > height ? twodheight : height));

        for(var k = 0; k < (secondd ? firstd : 1); k++){
          var data = (secondd ? snapshot.data[k] : snapshot.data);
          
          // Create SVG grouping element for each data item
          var elem = svg.selectAll(".array-node" + k)
              .data(data)
              .enter().append("g")
              .attr("class", "array-node" + k + " array-node")
              .attr("transform", function(d, i) { 
                return "translate(" + (array_padding + (i * (box_size.w +array_padding)))
                  + "," + (array_padding + (k * (box_size.h +array_padding))) + ")"; 
              });

          // Set range for weighted colors
          if(weightedColors)
            range = getRange(data);
          else
            range = [0,0]

          // Add main box to group
          elem.append("rect")
              .attr("width", box_size.w)
              .attr("height", box_size.h)
              .attr("class", function(d){
                return d.highlighted ? "highlighted" : null;
              })
              .attr("fill", function(d) { 
                return getColor(d, range);
              });

          // Add text to group
          elem.append("text")
              .attr("dy", box_size.h / 2 + array_padding)
              .attr("dx", box_size.w / 2)
              .attr("style", function(d){
                return d.highlighted ? "font-weight: bold" : null;
              })
              .attr("fill", textColor)
              .attr("text-anchor", "middle")
              .text(function(d) { return d.value; });
        }
        
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
        var nodes = tree.nodes(snapshot.data),
          links = tree.links(nodes);
        
        // Set width of canvas dependent on size
        d3.select("#canvas svg")
          .attr("width", (array_padding + 
              (nodes.length * (box_size.w + array_padding))));
        tree.size([height, (array_padding + 
              (nodes.length * (box_size.w + array_padding)))]);
        
        // Create link elements from link array
        var link = treecontainer.selectAll("path.link")
          .data(links)
          .enter().append("path")
          .attr("class", "link")
          .attr("d", diagonal);
         
        // Create node elements from nodes array
        var node = treecontainer.selectAll("g.tree-node")
          .data(nodes)
          .enter().append("g")
          .attr("class", "tree-node")
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
        
        break;
    }
}

function next(forward){
  // Change current counter to desired number
  current = current + (forward ? 1 : -1);
  
  // Disable buttons if at the end of the snapshots array
  d3.selectAll(".direction").attr("disabled", null);
  if(current >= dataset.snapshots.length - 1){
    d3.select("#next").attr("disabled", "disabled");
    d3.select("#play").html("<b>&#8634;</b>");
    playing = false;
    stopInterval();
  }
  else if(current == 0)
    d3.select("#previous").attr("disabled", "disabled");
  
  // Visualise desired number
  visualise(current);
}

function play(){
  if(current >= dataset.snapshots.length - 1)
    reset();
  
  // set playing button value
  d3.select("#play").html(!playing ? "&#9646;&#9646;" : "&#9654;");
  
  //get the speed from the input slider
  speed = d3.select("#speed").property("value");
  
  //call next with the desired interval
  if(!playing && current != dataset.snapshots.length - 1)
    myInterval = setInterval(function () {next(true)}, speed);
  else
    stopInterval();
  
  playing = !playing;
}

function changeSpeed(speed){
  stopInterval();
  if(playing && current != dataset.snapshots.length - 1)
    myInterval = setInterval(function () {next(true)}, speed);
  d3.select("#speed-value").html(speed + "ms");
}

function reset(){
  //reset current to the first snapshot
  current = 0;
  playing = false;
  stopInterval();
  
  //enable/disable relevant buttons
  d3.select("#play").html("&#9654;");
  d3.selectAll(".direction").attr("disabled", null);
  d3.select("#previous").attr("disabled", "disabled");
  
  //visualise first snapshot
  visualise(current);
}

function stopInterval(){
  clearInterval(myInterval);
}

function getColor(d, range){
  if (!weightedColors)
    return defaultColor;
  else {
    c = d3.scale.linear().domain([range[0],range[1]]).range([defaultColor, orange]);
    return c(d.value);
  }
}
function getRange(data) {
  min = Infinity;
  max = -Infinity;
  for(i = 0; i < data.length; i++){
    if(data[i].value > max)
      max = data[i].value;
    if(data[i].value < min)
      min = data[i].value;
  }  
  return [min, max]
}

function toggleWeightedColors(){
  weightedColors = !weightedColors;
  if(weightedColors)
    d3.select("#colors").text("Disable Weighted Colors");
  else
    d3.select("#colors").text("Enable Weighted Colors");
  visualise(current);
}

function sliderMove(slider){
  current = +slider.value;
  visualise(slider.value);
}
