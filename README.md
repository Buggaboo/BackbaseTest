# README

## Backbase technical test

Summary: create an android app to filter on a list of locations,
then show the selected location on the map.

## Approach

* Setup the project
* Parse the data with GSON's JsonReader
* Write as many tests for small methods asap
* Then gradually increase coverage, as more components starts to work

## General Algorithms / Datastructures / Optimizations used

* Implement Radix sort / Trie
* Dispatched heavy lifting with worker thread, so the app loads quickly
* Applied MVVM (observer actually), because I thought LiveData or RxAndroid would be overkill