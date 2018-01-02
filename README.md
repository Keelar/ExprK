# ExprK
A simple mathematical expression evaluator for Kotlin and Java, written in Kotlin.

### Features:
* Uses BigDecimal for calculations and results
* Allows you to define variables using values or expressions
* Variable definition expressions can reference previously defined variables
* Configurable precision and rounding mode

### Examples:
````Kotlin
val result = Expressions()
    .eval("(5+5)*10") // returns 100
````
You can define variables with the `define` method.
````Kotlin
val result = Expressions()
    .define("x", 5)
    .eval("x*10") // returns 50
````
The define method returns the expression instance to allow chaining definition method calls together.
````Kotlin
val result = Expressions()
    .define("x", 5)
    .define("y", "5*2")
    .eval("x*y") // returns 50
````
Variable definition expressions can reference previously defined variables.
````Kotlin
val result = Expressions()
    .define("x", 5)
    .define("y", "x^2")
    .eval("y*x") // returns 125
````
You can set the precision and rounding mode with `setPrecision` and `setRoundingMode`.
````Kotlin
val result = Expressions()
    .setPrecision(128)
    .setRoundingMode(RoundingMode.UP)
    .eval("222^3/5.5") 
````
