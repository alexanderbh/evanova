[![Build Status](https://travis-ci.org/evanova/evanova.svg?branch=master)](https://travis-ci.org/evanova/evanova)

## Evanova for Eve Online - 2.0
**Redesigned, Revamped, Opened and Much Better**

This repository contains all the code needed to build Evanova 2.0.

###What is this?
[Evanova](https://play.google.com/store/apps/details?id=com.tlabs.android.evanova) is the Android go-to application for Eve Online, which used to be closed-source.

This repository is a re-write and re-design with clean architecture principles. The intention is to backport features from Evanova and provide the same set of functionalities at first release, with enhanced UI.

####Motivation
Evanova was started in 2011 with support for Android 1.6. With the growing number of features, provinding fixes and updates was getting too difficult and the application had to be re-designed to be more maintainable, follow a better architecture and latest Android releases.

In addition, with the advent of CREST, many new features will be easy to add to the application with little effort and impact on the overall functionalities.

###Current State

You can import characters and corporations using either CREST or an API key and view the list of accounts/chars/corps.

While the back-end code is quite stable and usable, the UI has to be re-assembled. This includes building Android views (a.k.a widgets) and wiring them up in the application as well as enhancing UI feedback and interactions.

###Getting Started

####1 - Clone this repository

`git clone https://github.com/evanova/evanova --recursive`

####2 - Build the dependencies
Before you can build the application, you will need to make sure the dependencies are built and installed in your local Gradle cache. In the root project, do a

`gradlew build install -x test`

`-x test` is currently required as some unit tests need API keys or custom configurations that only you can provide and will fail without them.

####3 - Build the application

Once the above steps have been successfully performed, you can build the Android APK from the **app** module or from Android Studio.

```
cd app
gradlew build
```

*By now, you should go grab a drink and come back in 10mn - or more depending on your computer*

Please ignore the Lint stacktraces you see on the console, they are bogus from Lombok and have no impact.

####Using Android Studio
You can avoid the previous build step and import the **app** project directly in Android Studio.
Do NOT import the root project, it will not work and will confuse the IDE.

####Additional Configuration
The application requires a valid CREST application id and key for CREST access to work. You will need to [create your own application](https://developers.eveonline.com/applications) and customize the **crest.xml** file which you can find in *app/service/src/main/res/values*. Remember that **your settings should stay secret** so make sure to never commit this file If you happen to mistakenly do so, you should immediately delete your application keys from Eve Online.

####4 - Publishing to the Play Store

**You cannot do this under any circumstances.**

For a lot of good reasons, starting by preserving the application' users security, only the maintainer can deploy Evanova on the Play Store. If you have a good case for publishing an application based on Evanova, please contact evanova.mobile@gmail.com before trying to do so.

You are free however to create any application using the shared code, as long as it follows the license terms and does not use the 'Evanova' name.

###Evanova is recruiting!

This project needs your help. If you wish to contribute in any capacity, please feel free to let yourself known.

In no particular order (and not exclusively):

A **Java developer** with a gnack for Android. There is a lot of backporting to be done and design solutions to be found. That's actually where most of the work needs to be done. Also, as of writing this, the application code has zero unit testing, so if testing is your thing, you're welcome to break things.

An **Android UI designer** to provide UI suggestions, layouts and enhance the application theming. Note that you may have to implement your crazy ideas as XML layouts in the application. This shouldn't require much java skills but you should be familiar enough with Android developement to run your ideas and take screenshots.

A **Graphic designer** to provide custom icons and all graphics related to the [Google Play Store](https://play.google.com/store/apps/details?id=com.tlabs.android.evanova).

A **Technical writer** (anyone with good english skills and a lot of patience) who can take care of writing READMEs, help pages, maintaining a blog and ideally correcting text or internationalization issues in the Android application.

A **Project manager** (anyone with good organizational and communication skills) - Yup really! - Every team of more than one person needs someone not crazy to keep things organized, watch for release and Eve patch dates vs the project's state, communicate with the real world and prioritizing issues. The author of these lines is not that person.

Application testing will be required later but the current state doesn't require yet (we know it's full of bugs!).


###Contacts

evanova.mobile@gmail.com

@evanova on #tweetfleet/devfleet slack.

[Get invited on tweetfleet](https://www.fuzzwork.co.uk/tweetfleet-slack-invites/)
