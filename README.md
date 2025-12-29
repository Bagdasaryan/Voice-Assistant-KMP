# **Voice Assistant App**

Voice Assistant is a KMP application that allows medical staff to add a patient's medical readings using voice input.

# Features
On the `main screen`, a list of patients is displayed, retrieved from the server.

For quick finding of a specific patient, a `dynamic search bar` is supported.

Tapping on a specific patient takes the user to the `patient details screen`.
Here, the patient's history of vitals is displayed.
Tapping the `voice input button` allows dictating the vitals of the patient and adding them to the history.

On the `patient details screen`, the patient's history of illness is also displayed, in the **_Notes_** section.


# Screenshots
<img src="screenshots/splash_light.png" width="180"/> <img src="screenshots/splash_night.png" width="180"/>
<img src="screenshots/main_light.png" width="180"/> <img src="screenshots/main_night.png" width="180"/>

<img src="screenshots/main_search_light.png" width="180"/> <img src="screenshots/main_search_night.png" width="180"/>
<img src="screenshots/details_vitals_light.png" width="180"/> <img src="screenshots/details_vitals_night.png" width="180"/>

<img src="screenshots/details_recognizer_light.png" width="180"/> <img src="screenshots/details_recognizer_night.png" width="180"/>
<img src="screenshots/details_notes_light.png" width="180"/> <img src="screenshots/details_notes_night.png" width="180"/>

# Tech Stack
- **Kotlin Multiplatform**

- **MVVM**

- **Clean Architecture**

- **Jetpack Compose**

- **Coroutines**

- **Ktor** for client-server interaction

- **Dependency Injection** (Koin)

- **Room DB**

- **JUnit tests**









