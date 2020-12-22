# CongratsShower
Beautiful falling paper pieces animation in an Android App, built using Kotlin and XML. A highly reusable and customizable module that can be used in games, welcome screens, and whatnot.

### Do you want this library to have more features? Then make a pull request or write me an email at dopedevx@gmail.com

# DEMO:

Download demo app from: https://github.com/DopeDevX/CongratsShower/raw/master/DEMO.apk

[![Watch the video](https://i.stack.imgur.com/ezpUI.gif)](https://youtu.be/vt5fpE0bzSY)

# How to use?

1. Include maven into the repositories block present inside allprojects block in your project level build.gradle file. 

    ```
    allprojects {
       repositories {
          jcenter()
          maven { url "https://jitpack.io" } // add this line
       }
    }
    ```
2. Add the following line inside the dependencies block of your project level build.gradle file.

    ```
    	dependencies {
            ...
	          implementation 'com.github.DopeDevX:CongratsShower:v0.1.69.420' // add this line
            ...
	        }
    ```
3. Initialize CongratsShower with two parameters: 
    A. Reference of the root layout of the activity where you want the shower as a ViewGroup.
    B. Context of the activity.
    
    ```
      val shower = CongratsShower(layoutVariable, context)
    ```
    Then, call the perfromShower() method.
    
    ```
      shower.performShower()
      //~~~~~ OR ~~~~~
      shower.performShower(runtimeInMilliseconds)
    ```
    
    You can also study the code base and make necessary adjusments as per your use case.
