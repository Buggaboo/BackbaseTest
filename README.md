# README

## Backbase technical test

I was asked to do a test.

Summary: create an android app to search the (long, lat)-locations of places.

## Approach

1. Setup the project
2. Parse the data with org.json or regexp (see what's faster)
3. Normalize the data for ascii
4. Unit test the data input conversion first
5. Unit test the trie build, and trie search
6. Dispatch heavy lifting with worker thread (i.e. Handler and Runnable)
7. Make the app responsive by using Observables and use a lightweight MVVM architecture.

## Bonus

* Incremental loading the complete list with bound checking in the RecyclerView
* Unit test the user-interface with espresso