# EML
A programming language designed for mathematical modelling, parsed and interpreted in pure Java.

Documentation on the [project's wiki](https://github.com/MaxSW/EML/wiki)

Note that this project is in early development, and so there will be bugs and incomplete features.

Features:
* Inbuilt support for numbers, vectors and matrices
* Graph rendering
* Standard flow control, string and boolean features
* Classes and objects in a JS style
* APIs for maths and input

Language example:
```
//import the graph API
obj graph = import("graph")

//create a numeric variable x
var x = 0

//create an equation y that is a function of x
//whenever x changes y will automatically update
var :y = x^2 -2x - 4

//graph y as x changes from -5 to 5
graph.graph(:x, :y, -5, 5)
```
