import json
import os

class Visualiser:

  # Initialise snapshots dictionary
  def __init__(self, directory, filename):
    self.json_data = {"snapshots" : []};
    self.directory = directory;
    self.filename = filename;
    self.default_index = -1;

  # 2D Arrays Visualiser
  def visualise2d(self, array, *highlighted):
    data = {
      "type" : "2D-ARRAY",
      "data" : []
    };

    for i in range(0, len(array)):
      highlighted_row = [x[0] for x in filter(lambda t: t[0] == i, highlighted)];
      data.append(listToJSON(array[i], highlighted_row));

    self.json_data["snapshots"].append(data);

  # 1D Arrays Visualiser
  def visualise(self, array, *highlighted):
    self.json_data["snapshots"].append({
      "type" : "ARRAY", 
      "data" : self.listToJSON(array, highlighted)
    });

  # Binary Tree Visualiser
  def visualiseTree(self, tree, *highlighted):
    self.json_data["snapshots"].append({
      "type" : "BINARY-TREE",
      "data" : self.treeToJSON(array, 1, highlighted)
    });

  # Helper function to convert tree to JSON
  def treeToJSON(self, array, k, highlighted):
    if k >= len(array): return None;

    node = {
      "type" : "NODE", 
      "value" : array[k], 
      "highlighted" : (k in highlighted),
      "children" : []
    };

    leftChild = self.treeToJSON(array, 2 * k, highlighted);
    rightChild = self.treeToJSON(array, (2 * k) - 1, highlighted);

    if(leftChild != None): node["children"].append(leftChild);
    if(rightChild != None): node["children"].append(rightChild);

    return node;

  # Helper function for Array Visualisers
  def listToJSON(self, array, highlighted):
    data = [];

    for i in range(0, len(array)):
      data.append({
        "type" : "ELEMENT",
        "value" : array[i],
        "highlighted" : (i in highlighted)
      });

    return data;

  def end(self):
    with open(os.path.join(self.directory, self.filename), 'w') as outfile:
      json.dump(self.json_data, outfile);
