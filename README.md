# Cafeteria

This was a group project in an Object Oriented Programming class, for managing a college cafeteria. It's an evolution from the [ATM project](https://github.com/helderco/atm) created the previous year in Introduction to Programming.

This is being published for **educational purposes**, mostly for the application's **model** (see [uml.png][UML]), which is in part independent of the business model and can be reused in other contexts.

Most of the project is in English, although data and generally output to the user are few examples that had to be done in Portuguese per teachers' requirements.


## The Project

The purpose of this project is to create an application that allows managing the purchase of tickets by the student body in a University. The application should make this process much better and efficient, while freeing the worker that usually does this, to other tasks.

Each student is given a card which is personal and non-transferable, and is associated to an account. The student buys cafeteria tickets for a chosen meal type (meat, fish, vegetarian) in a chosen day (at lunch or dinner) through this account, as well as pre-paying money to an administrator for increasing his available balance.

The administrator is responsible for managing user records and accounts, physically receiving money and updating the sutdent's balance, as well as managing available meals and making meal menus.

The application uses a mixture of XML files, binary files, text files and a database for data storage for demonstration purposes, and is capable of sending emails.

If you can read portuguese, the full requirements are in the resources folder.


## Design Patterns

This application implements a lot of design patterns, mostly from the [Patterns of Enterprise Application Architecture](http://www.martinfowler.com/books.html#eaa) book by Martin Fowler, although over-simplified because this is a simple application.

### Domain Logic Patterns

* [Domain Model][] (116)

### Data Source Architectural Patterns

* [Data Mapper][] (165)

### Object-Relational Behavioral Patterns

* [Identity Map][] (195)
* [Lazy Load][] (200)

### Object-Relational Structural Patterns

* [Identity Field][] (216)
* [Foreign Key Mapping][] (236)
* [Dependent Mapping][] (262)

### Base Patterns

* [Gateway][] (466)
* [Mapper][] (473)
* [Layer Supertype][] (475)
* [Registry][] (480)
* [Value Object][] (486)
* [Record Set][] (508)

[domain model]: http://www.martinfowler.com/eaaCatalog/domainModel.html
[data mapper]: http://www.martinfowler.com/eaaCatalog/dataMapper.html
[identity map]: http://www.martinfowler.com/eaaCatalog/identityMap.html
[lazy load]: http://www.martinfowler.com/eaaCatalog/lazyLoad.html
[identity field]: http://www.martinfowler.com/eaaCatalog/identityField.html
[foreign key mapping]: http://www.martinfowler.com/eaaCatalog/foreignKeyMapping.html
[dependent mapping]: http://www.martinfowler.com/eaaCatalog/dependentMapping.html
[gateway]: http://www.martinfowler.com/eaaCatalog/gateway.html
[mapper]: http://www.martinfowler.com/eaaCatalog/mapper.html
[layer supertype]: http://www.martinfowler.com/eaaCatalog/layerSupertype.html
[registry]: http://www.martinfowler.com/eaaCatalog/registry.html
[value object]: http://www.martinfowler.com/eaaCatalog/valueObject.html
[record set]: http://www.martinfowler.com/eaaCatalog/recordSet.html

We were also careful to have [separation of concerns](http://en.wikipedia.org/wiki/Separation_of_concerns), as can be seen from the [UML][] (provided in the resources folder). A notable case is a simplified version of MVC (in regards to the controller, not the model or view). The model is completely separated and unaware of the other parts of the application, and the view and controller are implemented with Swing in a separate package.


## TDD and JUnit

The project started out with [TDD](http://en.wikipedia.org/wiki/Test-driven_development), although we were quickly pressed with time and was an overload for my partners who were hearing about it for the first time. So we abandoned writing tests, but I kept mindful of it, trying to make the code at least testable.

Perhaps sometime in the future, for me or others, this project can serve as practice in writing unit tests to prove the application works, find bugs and improve uppon it to make it better.

[uml]: https://github.com/helderco/cafeteria/raw/c3374bb5ff5d3ad2ce1478b8de021230a9292e33/resources/uml.png


## Authors

* Helder Correia (me)
* Nuno Diogo Silva
* Paulo Silva
