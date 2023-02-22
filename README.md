# CBC News & Sports

A basic news app showing the list of up-to-date news and sports articles with brief details.

## Features

* Fetch news content from API and display the articles list.
* See all news article types by default, but can filter story and video article types via the Bottom navigation view.
* Communicates loss of connectivity to the user.
* Offline persistence of data (cached data from DB gets cleared on app startup if network connectivity is there and latest data is stored in DB. On the other hand, if app starts in offline mode, then user can browse offline news content stored previously.
* Pagination feature for unlimited scrolling (has a couple known issues related to lagging and some sudden movement when new list is updated).
* Error handling: If an error occurs while fetching data from API, user is communicated and has the ability to retry.

## Framework and library choices

* Architecture Pattern
  * Model View ViewModel (MVVM) architecture

* Networking and Offline persistence of data
  * Used Retrofit2 with OkHttp3 and Gson to fetch the JSON response from API.
  * Used Room database to persist data and use as a single source of truth.
  * Used Coroutines for doing the long running tasks such as fetching data from API and reading/writing data to DB in background so as to not block the main thread.
	
* Dependency Injection
  * Used Koin framework to inject dependencies in order to separate the code to construct dependencies and make them easily available.

* Unit Tests
  * Used JUnit4 test framework to write some tests to verify the logic implemented for database operations and the queries used.

* Misc libraries used
  * Glide to load images in the article list items.


## Screenshots

* News articles list

![News list](https://user-images.githubusercontent.com/31713053/220617979-0f28e952-0ecd-4bdb-9436-03b96c8d082a.png)


* App is offline

![Offline](https://user-images.githubusercontent.com/31713053/220618631-18d029d0-b660-426c-9652-7d986f2ee5ec.png)


* Error occured while fetching data

![Error handling](https://user-images.githubusercontent.com/31713053/220618830-de6264e5-d22d-4c72-8dcb-89cac0bf03b1.png)
