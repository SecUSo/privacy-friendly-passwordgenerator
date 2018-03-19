# Privacy Friendly Password Generator

[<img src="https://f-droid.org/badge/get-it-on.png" alt="Get it on F-Droid" height="60">](https://f-droid.org/app/org.secuso.privacyfriendlypasswordgenerator)<a href="https://play.google.com/store/apps/details?id=org.secuso.privacyfriendlypasswordgenerator"><img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" height="60"></a>

Privacy Friendly Password Generator is an Android application that generates passwords based on previously saved accounts and a master password. The generated passwords can than be copied into the password or PIN field by the user.
This app belongs to the Privacy Friendly Apps group developed by the SECUSO research group at the Technische Universität Darmstadt, Germany. Further information can be found on [secuso.org/pfa](https://secuso.org/pfa)<br />

Users can save the following properties of a password: <br />
* Account name: e.g. a website or account name 
* Username: the username in case a user has multiple accounts for one service or website
* Character set: at least one of uppercase, lowercase, special, numbers
* Password length
* Password version: used to create different passwords if an update without changing of the master password and account data is intended.  <br />

### Password Generation

The password generation is based on the combination of two algorithms: the key deviation function PBKDF2 and the hash algorithm BCrypt. PBKDF2 and can be executed with three different hash algorithms (SHA256, SHA384, SHA512). <br />
* The master password serves as a secret for the PBKDF2 algorithm.
* Password counter, account name, username and device ID (optional) are concatenated to a string and form the salt of PBKDF2. 
* The result of the PBKDF2 hashing is encoded into a special version of Base64 which is compatible with BCrypt and not longer than 22 characters.
* The master password serves as a secret for the BCrypt algorithm.
* Result of the PBKDF2 hashing combined with the string "$2a$10$" the beginning forms the salt for BCrypt.
* The prefix and the salt is cut from the resulting byte-array.
* The byte-array is used to choose characters out of the character set the user has chosen. 
<br />

The passwords as well as the master password are never stored in the device. The master password has to be entered by the user and password is always created on the fly. 

## Motivation

Nowadays users need many different passwords for all kinds of services and also websites. Remembering and generating strong passwords can be a tough task.  <br />
Privacy Friendly Password Generator should support users in creating strong passwords without having to trust a program to store them securely and safely for them. The complexity of remembering the passwords is reduced to a single master password. 

## Building

Further development requires Android Studio, we recommend to use at least version 2.3<br />
If you wish to contribute to this project, have a look at the contribution policy. 
 
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

The icons used in the nagivation drawer are licensed under the [CC BY 2.5](http://creativecommons.org/licenses/by/2.5/). In addition to them the app uses icons from [Google Design Material Icons](https://design.google.com/icons/index.html) licensed under Apache License Version 2.0. All other images (the logo of Privacy Friendly Apps, the SECUSO logo, the icon in the splash screen and the app icon) copyright [Technische Universtität Darmstadt](https://www.tu-darmstadt.de/) (2016).

## Contributors

App-Icon: <br />
Markus Hau<br />

Developer: <br />
Yonjuni (Karola Marky) <br />

Github-Users: <br />
Naofum <br />
mohammadnaseri <br />
Henne1191






