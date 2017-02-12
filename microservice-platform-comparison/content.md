


## The Analysis

include::platform-analysis.csv[]

[format="csv", options="header"]
|===
Artist,Track,Genre
Baauer,Harlem Shake,Hip Hop
The Lumineers,Ho Hey,Folk Rock
|===


## Intro

Recently, we ran a London Microservices User Group meeting - "Microservices Platform Smackdown".
This was intended to be somewhat more fiery than the ordinary meetup, directly pitching tech
against tech. It mostly worked, and we'll run the model again.

One thing that came off the back of it was that many dev teams don't even know their options when
 putting a microservice architecture in place. You need many pieces to successfully deploy
 microservices, and one of the biggest is 'the platform'.

This article is meant to be a starting point for teams to analyse. It does not make a single
recommendation, instead presenting enough broad sweeps to get you started with a short list.

If you'd like a more comprehensive analysis, get in touch for some consultancy and we'll happily
give that to you.

## The Contenders

This list is not fully comprehensive. While we believe we've got all the platforms that have significant
 traction or interest, we may have missed some that exist in certain niches. If you know of such a platform
 get in touch and we'll evaluate it for inclusion.

Cloud Foundry
Lattice
Kubernetes
Mesos/ Marathon
Mesosphere DCOS
Apollo
Logam
NetKernel
OSGI one, Fabric8
OpenShift
Docker ones
Deis
Heroku
GAE
Azure XXX
AWS EBS
Dokku
Flynn
AWS Lambda
Google Cloud Functions
Iron Forge Lambdaesque

## Evaluation Criteria

Community (number of participants, companies, individuals, percentage of commits? community addons)

License type
 - Fully open source
 - Commercial Extensions
 - Proprietary

Platform Deploy Model
 - Fully Hosted
 - Hosted and self hosted
 - Only self hosted

Application Deploy Method
 - API
 - Git push
 - Container image

Buildpack Support

Docker Support
Rocket Support

Virt platform support
 -

Platform Services?

Application Management

Service Discovery

Admin user interface

Developer user interface

Deployment Method
 - BOSH
 - AWS proprietary

Minimal Platform size
 - 1 VM
 - X VM.


## Simplicity Itself Recommends

When evaluating platforms of this nature, it feels natural to see what your processing requirements are and
attempt to match them above.  Ultimately though, your data architecture and where you want it to run will be the deciding factors
in which platforms are even available to you. The role of data, how it moves and how you work
upon it cannot be overstated.

"Microservice" is, by definition, an ill defined term, and so all of the above are able to host
something that can legitimately be called a microservice.  WIth that though, they cannot all host
all of the same styles of microservices, and this is where your decision needs to be made.

Broadly, your data architecture will likely be one of :-

* Entity oriented. Inter service RPC + Cache, standard DB, with http/ other synchonous comms.
* Event/ Message oriented. Standard DB, async comms.
* Stream oriented. Event DB, distributed processing, async comms.

The platforms data processing model is somewhat linked to this, and so influences what your
 decision will be.

Broadly, the platforms above have one of the three models :-

* Cloud Function
* Stateless
* Stateful

Each model can be used to also build the one above, if in a less optimised way.

As an example, Mesos is a stateful scheduler for managing processing resources. Each resource can maintain
state, volumes can be managed and attached as needed. It is certainly possible, and indeed common
to implement _stateless_ applications within a Mesos managed setup. As compared with a dedicated
stateless system, the dev UX is generally less optimised and some support infrastructure is missing, but it
is possible to build a platform with similar capability, given the resources to do so.

Similarly, it is possible on any stateful or stateless platform to construct an analogue of the
cloud function systems, although initially with a less optimised dev UX, and with some investment
to do to get the system running.

The upshot of this, we recommend the following analysis of your requirements :-

* Where is your data, and what should it do.
* How optimised do you want the dev process to be.
* How much effort do you want into implementing the platform.

## PLatform and Development Process (aka Continuous Delivery)

TODO, stuff here.

### The Sales Bit

If you need any help evaluating or implementing a Microservices application rollout, including
the underlying platform, we can help!  Please get in touch.