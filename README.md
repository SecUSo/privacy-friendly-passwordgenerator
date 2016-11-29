## Privacy Friendly Password Generator

Privacy Friendly Password Generator is an Android application that generates passwords based on pre-chosen parameters and a master password.
This app belongs to the Privacy Friendly Apps group developed by the SECUSO research group at the Technische Universität Darmstadt, Germany. Further information can be found on secuso.org/pfa<br />

Users can save the following parameters: <br />
* Domain: e.g. a website or account name 
* Username (optional) 
* Character set: at least one of uppercase, lowercase, special, numbers
* Password length
* Iteration: used to create different passwords if an update with the same parameters and no change of master password is intended.  <br />

### Password Generation

The password generation is based on the combination of two password hashing algorithms: PBKDF2 and BCrypt. PBKDF2 and can be executed with three different hash algorithms (SHA256, SHA384, SHA512). <br />
* The master password serves as a seed for the PBKDF2 algorithm.
* Iteration, domain, username and device ID (optional) are concatenated to a string and form the salt of PBKDF2. 
* The result of the PBKDF2 hashing is encoded into a special version of Base64 which is compatible with BCrypt and not longer than 22 characters.
* The master password serves as a seed for the BCrypt algorithm.
* Result of the PBKDF2 hashing combined with the string "$2a$10$" the beginning forms the salt for BCrypt.
* The prefix and the salt is cut from the resulting byte-array.
* The byte-array is used to choose characters out of the character set from the user's parameters. 
<br />

The passwords as well as the master password are never stored in the device. The master password has to be entered by the user and password is always created on the fly out of the parameters. 

## Motivation

Nowadays users need many different passwords for all kinds of services and also websites. Remembering strong passwords can be a tough task.  <br />
Privacy Friendly Password Generator should support users to create strong passwords without having to trust a program to store them securely and safely for them. The complexity of remembering the passwords is reduced to a single master password. 

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





