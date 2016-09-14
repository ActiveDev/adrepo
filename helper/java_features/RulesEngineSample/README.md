# Rules Engine Sample
Sample application that demonstrates some of the "new" features of Java:

* Lambda Expressions
* Functional Interfaces
* Predicates

## Concept
This project is intended to demonstrate Java features, so some implementation details may seem odd. The whole idea of this sample is to have some kind of business object such as an Order. The business object contains the data, but the business logic to validate this data is kept in a Manager class. The manager class defines the rules and registers them with the Rules Engine.

## Running the Project   
Seems kind of strange that it's a web project doesn't it. I have plans to add a REST interface to create orders that will then be validated. Until then, have a look at the ValidatableTest JUnit class to see how to run the code.

## IValidatable
Is an interface that identifies a class as having the ability to validate against a set of rules. 

## AbstractValidatable
Is an implementation of the IValidatable interface. It's really not necessary, but provides some helper functionality to business objects to simply define some predicates and not worry about registering them or firing them off.

## RuleEngine
This class holds the rules and fires them off. It also stores the results and returns them back to the caller. Of course, this is a sample, but has a lot of room for added functionality. Feel free to play around with it and get used to Java's new features.

## Rule
This class is a wrapper around the Predicate that would be run. I added some extra fields to show what kind of functionality could be accomplished with it.

