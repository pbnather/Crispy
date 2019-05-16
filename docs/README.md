# About

Crispy is an Android application created during ITSMAP course at Aarhus University, Denmark.  
It won Community Award in the ITSMAP E2018 student contest (December 2018).

As one can read in the [project's report](https://github.com/pbnather/Crispy/blob/master/docs/Crispy_Project_Report.pdf):

> The mission of our app is to make storing recipes,  planning meals and doing
groceries, for the whole family, as easy as possible.  An intuitive UI will allow
users  to  quickly  pick  up  a  day,  choose  a  recipe,  and  decide  who  is  going  to
prepare  it  with  just  a  few  clicks.   This  app  will  make  deciding  about  which
meal should the family eat the next day – as well as who should take the actual
responsibility  of  preparing  it  –  smooth  and  efficient.   Moreover  it  will  enable
users to have all of their families tasty recipes always in their pocket.

To get more familiar with Crispy watch the video!

<center><iframe width="100%" height="425" src="https://www.youtube.com/embed/WvC2NzosAfE" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe></center>

Crispy was developed by:
* Paweł Belczak – <https://github.com/pbnather>
* Alicja Kowalewska – <https://github.com/alako>
* Vojtěch Rychnovský – <https://github.com/rychnovsky>

# Architecture 

Crispy was developed using Android SDK (Java) and Architecture Components.  
There are only two activities, one of which is FirebaseUI activity for user authentication.  
All other views are made with fragments. The app also relies on some third-party libraries.  
One can read more in the [project's report](https://github.com/pbnather/Crispy/blob/master/docs/Crispy_Project_Report.pdf).

## Android 

* Navigation Component
* LiveData
* ViewModels
* Fragments

## Other

* Glide
* Retrofit
* FoodAPI
* FirebaseUI
* Firebase SDK
