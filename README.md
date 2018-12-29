# WeatherApp

Grio code project 

This app uses a weather API (DarkSky) to get current weather details from a provided latitude and longitude, and also 
provides a forecast of 7 days. I stuck to a MVC architecure because the complexity of the task was low, and the data
being manipulated won't be changing very frequently. I also opted to leave out persistence libraries, again, due to the
low complexity of the project.

All of the conditions from the Grio test requirements were met, with exception of
STORY-4, which asked to implement a small map view of the selected location. Fulfilling this condition would have been
simple enough, but I elected to leave it out for one main reason : it didn't seem to flow with the design of the app. 
The screens themselves were adhering to a minimalist style, and I personally felt that including a GoogleMaps fragment 
in the middle of it would stick out like a sore thumb. Part of me wants to implement the map fragment to achieve the bonus and show that I can design around an obstacle, but the other part just wants to stick to the minimalist design! If requested, I 
can throw in the fragment and push another commit.

Progress of the development of this app was tracked in a Github project (https://github.com/ajhakeem/WeatherApp/projects/1).
