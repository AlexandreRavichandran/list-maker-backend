Media list maker
================

The goal of this project is to be able to se a list of movie to watch, and music to listen. So you can search a movie or album/songs, get their details, and add them in your list. You can even get a random element in your list if you don't know what to watch/listen !

How to run
----------

To run this project, you have a docker compose file that you can run so everything will be setup automatically and you can use this project in a flash.

Command to do:

```properties

git clone https://github.com/AlexandreRavichandran/media-list-maker-backend.git

cd media-list-maker-backend/service-parent

docker compose up
```  



### Warning:

The project is using 2 external apis: Deezer and OMDB. While Deezer doesn't need any apikey, OMDB is using one. So you have to generate it. To generate one, you can go directly in [OMDB official website](https://www.omdbapi.com/). After getting your api key, replace it in the docker compose file (instead of 'OMDB_API_KEY' in movie service section of the docker compose file).

Few screens
-----------

#### Home page

![Illustration of home page](/service-parent/pictures/home.jpg?raw=true "Home page")

#### Movie search

![Illustration of movie search](/service-parent/pictures/movie_search.jpg?raw=true "Movie search")

#### Movie details

![Illustration of movie details](/service-parent/pictures/movie_details.jpg?raw=true "Movie details")

#### Album details

![Illustration of album details](/service-parent/pictures/album_details.jpg?raw=true "Album details")

