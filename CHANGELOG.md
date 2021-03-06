# Mapxus Map SDK Change Log

## 4.0.0

### Features
* Migrate to AndroidX.
* Use mapxus positioning SDK 2.0.0.
* Simplify Mapxus SDK dependencies.


## 3.2.6

### Bugs
* Fix can not use pointsearch.
* Fix Buildingsearch parsing exception.

### Features
* Add multi-language tag named "accessibilityDetail" of POI.

* Now you can get multi-language title and description of POI category search API in SDK.


## 3.2.5

### Bugs
* Fix multi-language data return.

### Features
* Add Korean and Japanese support.


## 3.2.4

### Bugs
* Fix can not show some building data.(Add multiple layouts to search building data)


## 3.2.3

### Bugs
* Fix floor filtering rules.

### Features
* Upgrade to mims2.


## 3.2.2

### Features
* Add buildingName tag in pointinfo class.


## 3.2.1

### Features
* Add init route method in WalkRouteOverlay class.
* Highlight instruction when selected.
* Update wayfinding connector icon.
* Users can use pointsearch to search buildings and pois at the same time.


## 3.2.0

### Bugs
* Fix wayfinding instructions.(the first point of instruction not match it's instruction)
* Fix map TalkBack.(do not read the logo)

### Features
* User can config the map element language and not dependence on system language.
* When we trigger the even of buildingChange,we can get the properties like 'bbox' and 'mult language name' from building.
* When we click the poi.We can get the buildingId、floor、type and location.
* Wayfinding add vehicle param.Support foot and wheelchair.
