import os
from visualiser import Visualiser

# Get main directory of project relative to this file
directory = os.path.dirname(os.path.dirname(os.path.dirname(os.path.realpath(__file__))));

# Create new visualiser object
visualiser = Visualiser(directory, "output.json");

# Two dummy arrays to test String and Int output
stringarray = ["ABC", "DEF", "GHI"];
intarray = [1, 2, 3, 4, 5, 6, 7, 8];

# Visualise both arrays
visualiser.visualise(intarray);
visualiser.visualise(stringarray);

# Finish writing and flush output
visualiser.flush();
