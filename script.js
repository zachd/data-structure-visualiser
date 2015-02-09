var data;

d3.json("test.json", function(error, json) {
  if (error) return console.warn(error);
  data = json;
  visualize();
});

function visualize(){
  console.log(data);
}
