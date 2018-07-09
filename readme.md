# LDDReflector
A tool for transforming Lego Digital Designer models into their mirror reflection.

![Example](doc/lddr_example.png?raw=true "Example")

## Usage
If you have SBT installed, simply type the command:
`sbt run`

Alternatively, download the [LDDReflector.jar file](https://github.com/mackoo13/LDDReflector-JAR).

To load the LDD model, you need the file in LXFML format. The easiest way to save it is using Export option in LDD (Ctrl+E).

The program will create two files:

1. xxx_reflected.lxfml contains the reflected model
2. xxx_remaining.lxfml keeps all the bricks which could not be reflected.

To convert it back to .lxf format, create a new .lxf file and use Import (Ctrl+I).

## Supported bricks

At the moment, about 450 types of bricks are supported - for my models, this gives over 90% coverage. Much more are possible to reflect, but so far not included to the application.

Adding new parts' dimensions is quite time-consuming, so at this stage I concentrated only on the more common ones.
If you need a particular part support, I can add it for you, so feel free to contact me.

The list of supported bricks is always updated on startup, so there's no need to download it again manually.

## Troubleshooting
Should you encounter *any* error during using the application, please let me know as soon as possible (see Contact section below).
It'd be best if you attached the LXF/LXFML file that caused the problem.

In particular, some bricks might be correctly rotated, but anchored in a wrong place - this'd probably mean I had made a mistake in introducing parts dimensions.
The sooner I correct it, the better!

## Licence
The licence is Brickware. It's the same as Beerware, but with bricks instead of beer.
So if you ever meet me next to a PaB wall, you know what to do. ;)

## Contact
toltomeja [at] gmail.com

Github: https://github.com/mackoo13/

Flickr, Eurobricks and other AFOL networks: Toltomeja (https://www.flickr.com/photos/toltomeja/)
