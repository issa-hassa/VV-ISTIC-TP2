# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer
Tight Class Cohesion (TCC) and Loose Class Cohesion (LCC) are metrics used to evaluate the degree of cohesion within a class in object-oriented programming.

- Tight Class Cohesion (TCC): TCC measures the percentage of method pairs that access common instance variables. In other words, it quantifies the interdependence among methods in a class.


- Loose Class Cohesion (LCC):LCC, on the other hand, measures the percentage of method pairs that do not share any instance variables. It assesses how independent methods are from each other in terms of data access.



In certain scenarios, TCC and LCC can produce the same value for a given Java class. This happens when all methods within the class either access the same set of instance variables (high TCC) or do not share any instance variables (high LCC). Let's consider an example where both metrics yield the same result:

```java
public class CohesionExample {

    private int variableA;
    private String variableB;

    public void method1() {
        // Accessing variableA
        System.out.println(variableA);
    }

    public void method2() {
        // Accessing variableA and variableB
        System.out.println(variableA + " " + variableB);
    }

    public void method3() {
        // Accessing variableA
        System.out.println(variableA);
    }

    public void method4() {
        // Accessing variableB
        System.out.println(variableB);
    }
}

```
