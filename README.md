# Purpose?
Use Java 21 to create a browser automation script with less boiler plate code.  One you might want to write and then throw away a few sprints later.

The idea was to use the [introduction of the simplified main method, and introduction of unnamed classes](https://openjdk.org/jeps/445) to create not only a single, unnamed class but also have all the script inside the sole main method. In the end I ended up using one helper method. 

# What does it actually do?
1) Use the playwright browser automation api to obtain a list of hugo winning books from wikipedia. 
2) Search for available books at local library resource to see if any are currently available

# How to run it?
Checkout the project and then run './gradlew run' from the terminal
