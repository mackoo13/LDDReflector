# LDDReflector
A tool for transforming Lego Digital Designer models into their mirror reflection.

## Usage
If you have SBT installed, you can run the application simply typing the command:
`sbt run`

To load the LDD model, you need the file in LXFML format. The easiest way to save it is using Export option in LDD (Ctrl+E).
The program will create two files:
* [name]_reflected.lxfml contains the reflected model
* [name]_remaining.lxfml keeps all the bricks from the original model, which could not be reflected.

At the moment, 136 common types of bricks are supported. Further 400 are possible to reflect, but so far not included to the application.
Each part has to be added manually along with its exact dimensions, so at this stage I concentrated only on the more common ones.
If you need a particular part support, feel free to contact me.

List of supported bricks is always updated on startup, so there is no need to download it again manually.

## Troubleshooting
Should you encounter *any* error during using the application, please let me know as soon as possible (see Contact section below).
In particular, some bricks might be correctly rotated, but anchored in a wrong place - this probably means I made a mistake in introducing parts dimensions.
The sooner I correct it, the better for the other users.

## Licence
The licence is Brickware. It's the same as Beerware, but with bricks instead of beer.
So if you ever meet me next to a PaB wall, you know what to do. ;)

# Contact
toltomeja [at] gmail.com
Github: https://github.com/mackoo13/
Flickr, Eurobricks and other AFOL networks: Toltomeja (https://www.flickr.com/photos/toltomeja/)
