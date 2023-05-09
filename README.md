# Oxylabs | Supply Apps Team Lead / Android Developer Candidate Task

Hello, dear applicant!

Our development team has been working hard on building the cliché weather app, and needs your help.

The MVP of this app cannot even be compiled for some reasons, even though the developer said it works on his machine.

You’ve been tasked to help with app’s development by fixing current issues and implementing some small new features.

## What does the app do?

This app displays current temperature, wind and wind direction that is periodically taken from [open-meteo API](https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&current_weather=true&hourly=temperature_2m,relativehumidity_2m,windspeed_10m). Based on temperature, weather icon is displayed:
```
<0°C - snowflake
0-5°C - sun with cloud
>5°C - sun
```

## Things to be fixed

### Task #1: we have few problems
For some reason error occurs when attempting to build the app. But even when app compiling was working, the data wasn't always fetched and "???" were displayed instead of actual weather. These errors should be investigated and fixed. We also noticed that some UI elements are overlapping which should not be the case. In addition to that, the code doesn’t feel like it follows good conventions or practices. Maybe some structural and style changes could be applied?

### Task #2: we do some expanding
We don’t want the app to remain in MVP state anymore. We want more features. The most innovative additional feature that we came up with is to show notification when temperature, wind or wind direction changes. How should we proceed with that?

### Task #3: we test things
Is there anything worth testing? If so, then some very simple unit tests would be nice.

## Some tips to get started
* Using Android Studio is highly recommended
* Imports are missing for some reason? Reload Gradle project - might help
* Manifest file should contain only necessary permissions
* Few android emulators with different versions should help with testing
* Minimum supported android version should be Android Lollipop

## What we find extra value in
* You verbally explain approaches you take and what you are looking for.
* Using some common architectural patterns for restructuring the code (MVP, MVVM, MVPC, etc.)
