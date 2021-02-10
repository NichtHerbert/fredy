# Workflow-Net Editor
The present program is a workflow-net editor, a program for creating, editing, preserving and reusing workflow-nets.
It was created in 2018 for the "Grundpraktikum Programmierung" of the computer science course at the FernUniversität Hagen (Germany's State Distance-Learning University).

## What is a Workflow Net?
A workflow-net is a [Petri net](https://en.wikipedia.org/wiki/Petri_net), so it consists of 3 different possible elements: places, transitions and, connecting these two element types, directed edges called 'arcs'. Places are depicted as circles and transitions are depicted as rectangles.
The conditions for a workflow-net are:
* Arcs can only take course between elements of different types.
* A workflow-net has exactly one place with no incoming arcs, the starting place.
* A workflow-net has exactly one place with no outgoing arcs, the end place.
* All places and transitions are on a directed path from the starting place to the end place.
* Places can have two states, marked or unmarked. (The state of a place can be changed by switching a transition, see below).
* Transitions have 3 possible states: non-activated, activated, and activated-but-contact.
* The state of the transitions depends on the places to which they are connected by arcs:

    * If all places from which an arc leads to the transition have a marking, but none of the places to which an arc leads away from the transition, then the transition is activated.
    * If all places from which an arc leads to the transition have a marking, but if also places to which an arc leads away from the transition are marked (except for places to which an arc leads to as well as away from the transition), then the transition is activated-but-contact.
    * In all other cases, the transition is non-activated.
* An activated transition can be fired. This means that all places from which an arc leads to the transition will be unmarked, and all places where an arc leads away from the transition are marked.

###Icons
As things are now all icons in this program are from [flaticon.com](https://www.flaticon.com/) and are made by

* [Dave Gandy](https://www.flaticon.com/authors/dave-gandy)
* [Freepik](https://www.flaticon.com/authors/freepik)
* [Darius Dan](https://www.flaticon.com/authors/darius-dan)
* [Pixel perfect](https://www.flaticon.com/authors/pixel-perfect)
* [Vitaly Gorbachev](https://www.flaticon.com/authors/vitaly-gorbachev)