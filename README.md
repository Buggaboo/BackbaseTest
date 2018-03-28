# README

## Backbase technical test

Summary: create an android app to filter on a list of locations,
then show the selected location on the map.

## Approach

* Setup the project
* Parse the data with org.json or regexp (see what's faster)
* Write as many tests for small methods asap
* Then gradually increase coverage, as more components starts to work

## Algorithms / Datastructures / Optimizations used

* Applied what I learned at the uni, a looooooooooooong time ago, I used a trie
* Dispatched heavy lifting with worker thread, so the app loads quickly
* Applied MVVM (observer actually), because I thought LiveData or RxAndroid would be overkill