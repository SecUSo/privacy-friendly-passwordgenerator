## Privacy Friendly Password Generator

Privacy Friendly Password Generator is an Android application that generates passwords based on pre-chosen parameters.
This app belongs to the group of Privacy Friendly Apps developed by the Technische Universität Darmstadt. <br />

Users can save the following parameters: domain, username (optional), character set (uppercase, lowercase, special, numbers), length and iteration. The iteration is used to create different passwords if an update with the same parameters is intended.  <br />

### Password Generation

The password generation is based on the algorithm PBKDF2 and can be executed with three different hash algorithms (SHA256, SHA384, SHA512). <br />
Domain, username, master password, device ID (optional) and iternation is concatenated to a string which serves as a seed for the hashing algorithm. <br />

The passwords as well as the master password is never stored in the device. The master password has to be entered by the user and password is always created on the fly out of the parameters. 

## Motivation

This application has been developed to be used as a basis for a Privacy Friendly App. Privacy Friendly Apps are group of Android applications which are optimized regarding privacy. Further information can be found on secuso.org/pfa

## Download and more Information

Further development requires Android Studio, we recommend to use at least version 2.2.2
 
### API Reference

Mininum SDK: 17
Target SDK: 25 

## License

Privacy Friendly Password Generator is licensed under the GPLv3.
Copyright (C) 2016  Karola Marky

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.

The icons used in the nagivation drawer are licensed under the [CC BY 2.5] (http://creativecommons.org/licenses/by/2.5/). In addition to them the app uses icons from [Google Design Material Icons](https://design.google.com/icons/index.html) licensed under Apache License Version 2.0. All other images (the logo of Privacy Friendly Apps, the SECUSO logo, the icon in the splash screen and the header in the navigation drawer) copyright [Technische Universtität Darmstadt] (www.tu-darmstadt.de) (2016).

## Contributors

App-Icon: <br />
Markus Hau<br />

Github-Users: <br />
Yonjuni





