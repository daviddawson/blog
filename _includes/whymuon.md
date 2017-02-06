Muon is a **polyglot microservices communication toolkit** that enables you to easily build microservices in **many languages** that have richer, 
more performant and **fully reactive** communication semantics. You can easily gain access to Muon compatible services written 
in other languages without having to sacrifice the richnes of communication or performance.

With built in **service discovery**, **remote service introspection** and the ability to create new ways of communicating
as your application needs. 

It is designed to be the epitome of **intelligent endpoints** and **dump pipes**, building autonomous services with rich communication options.
 
## Protocols and Communication

Muon focuses on your **data**, how it needs to move, and what it means to you. Your microservices will need to talk to each 
other in a variety of ways to effectively process this data in a performant way. 

These different ways of communicating, streaming, request/ response, back pressure, transient streams, persistent streams ... 
The list goes on, yet all of these are methods for services to interact with each other in a structured way.  These are
known as **protocols**.

Muon is not a generic toolkit for Microservices, it is a **communication toolkit**, a way to build protocols and use them easily in microservice development 

By taking this approach, adding new ways of communicating is natural, and so Muon has, out of the box, the **richest communication
functionality** of existing microservices technologies, and does so across multiple languages.

Notable built in are protocols are **reactive streaming**, **event emit**, **event sourcing** and **distributed event projection**.

Some of these are designed for broad use, like reactive streaming, others give easy and richer access to particular services, such as Photon, an event store and Collider, an event projection service.

## Polyglot

Polyglot means multi language. For many, that means on the same platform or VM. 

We mean truly multi platform. Muon is specified and externally tested at multiple levels to ensure compatibility 
is maintained.

Currently supported are **Java**, **Clojure**, **Node.JS** and experimental support for in browser, **Muon.JS**

See <a href="/#gettingstarted">Getting Started</a> to get to the coding

## Communicating Sequential Processes (CSP)

Internally, Muon is implemented as a restricted **form of CSP**, with well specified ways to distribute over a network.

As an application designer, you do not need to be overly concerned with this model, although if you so choose, you can.

This model gives excellent characteristics for distribution, availability and fault tolerance. 
