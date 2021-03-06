package com.crabgore.moviesDB

object Const {
    class Addresses {
        /**
         * все адреса
         */
        companion object {
            const val API_HOST = "https://api.themoviedb.org/3/"
            const val IMAGES_API_HOST = "https://image.tmdb.org/t/p/w500"
            const val ORIGINAL_IMAGES_API_HOST = "https://image.tmdb.org/t/p/original"
            const val GRAVATAR_IMAGES_HOST = "https://www.gravatar.com/avatar/"
            const val IMDB_TITLE = "https://www.imdb.com/title/"
            const val IMDB_NAME = "https://www.imdb.com/name/"
        }
    }

    class Keys {
        companion object {
            const val API_KEY = "238c61f9b37aad2b98af577deb79f32d"
        }
    }

    class Constants {
        companion object {
            const val DECORATION = 15
        }
    }

    class MediaTypes {
        companion object {
            const val MOVIE = "movie"
            const val TV = "tv"
        }
    }

    class MyPreferences {
        companion object {
            const val SESSION_ID = "session_id"
            const val ACCOUNT_ID = "account_id"
            const val SEARCH_MOVIE = "search_movie"
            const val SEARCH_TV = "search_tv"
            const val SEARCH_PEOPLE = "search_people"
        }
    }
}