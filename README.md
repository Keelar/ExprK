# ExprK
A simple mathematical expression evaluator for Kotlin and Java, written in Kotlin.

### Features:
* Uses BigDecimal for calculations and results
* Allows you to define variables using values or expressions
* Variable definition expressions can reference previously defined variables
* Configurable precision and rounding mode
* Functions and the ability to define new ones

### Supported operators
#### Arithmetic operators
<table>
    <tr>
        <th>Name</th>
        <th>Operator</th>
    </tr>
    <tr>
        <td>Plus</td>
        <td>+</td>
    </tr>
    <tr>
        <td>Minus</td>
        <td>-</td>
    </tr>
    <tr>
        <td>Multiply</td>
        <td>*</td>
    </tr>
    <tr>
        <td>Divide</td>
        <td>/</td>
    </tr>
    <tr>
        <td>Modulus</td>
        <td>%</td>
    </tr>
    <tr>
        <td>Exponent</td>
        <td>^</td>
    </tr>
</table>

#### Logical operators
<table>
    <tr>
        <th>Name</th>
        <th>Operator</th>
    </tr>
    <tr>
        <td>And</td>
        <td>&&</td>
    </tr>
    <tr>
        <td>Or</td>
        <td>||</td>
    </tr>
</table>

### Pre-defined variables
<table>
    <tr>
        <th>Variable</th>
        <th>Value</th>
    </tr>
    <tr>
        <td>pi</td>
        <td>3.141592653589793</td>
    </tr>
    <tr>
        <td>e</td>
        <td>2.718281828459045</td>
    </tr>
</table>

### Pre-defined functions
<table>
    <tr>
        <th>Function</th>
        <th>Description</th>
    </tr>
    <tr>
        <td>abs(<i>expression</i>)</td>
        <td>Returns the absolute value of the expression</td>
    </tr>
    <tr>
        <td>sum(<i>expression, ...</i>)</td>
        <td>Returns the sum of all arguments</td>
    </tr>
    <tr>
        <td>floor(<i>expression</i>)</td>
        <td>Rounds the value of the expression down to the nearest integer</td>
    </tr>
    <tr>
        <td>ceil(<i>expression</i>)</td>
        <td>Rounds the value of the expression up to the nearest integer</td>
    </tr>
    <tr>
        <td>round(<i>expression</i>)</td>
        <td>Rounds the value of the expression to the nearest integer in the direction decided by the configured rounding mode</td>
    </tr>
    <tr>
        <td>min(<i>expression, ...</i>)</td>
        <td>Returns the value of the smallest argument</td>
    </tr>
    <tr>
        <td>max(<i>expression, ...</i>)</td>
        <td>Returns the value or the largest argument</td>
    </tr>
    <tr>
        <td>if(<i>condition, trueValue, falseValue</i>)</td>
        <td>Returns <i>trueValue</i> if <i>condition</i> is true(<i>condition</i> != 0), otherwise it returns <i>falseValue</i></td>
    </tr>
</table>

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
You can add new functions with the `addFunction` method.
````kotlin
val results = Expressions()
    .addFunction("min", object : Function() {
        override fun call(arguments: List<BigDecimal>): BigDecimal {
            if (arguments.isEmpty()) throw ExpressionException(
                    "min requires at least one argument")

            return arguments.min()!!
        }
    })
    .eval("min(4, 8, 16)") // returns 4
````
You can set the precision and rounding mode with `setPrecision` and `setRoundingMode`.
````Kotlin
val result = Expressions()
    .setPrecision(128)
    .setRoundingMode(RoundingMode.UP)
    .eval("222^3/5.5") 
````
