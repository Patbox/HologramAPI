# This library is deprecated!
With next release, it likely won't get an update. You should switch to Polymer Virtual Entity API instead,
as it allows for better and more powerful client side entities, while also being compatible with more mods (for example Immersive Portals).

## Links:

* Github: https://github.com/Patbox/polymer
* Docs: https://polymer.pb4.eu/0.5.x/polymer-virtual-entity/getting-started/


# Hologram API
It's a small, jij-able library for creating server side holograms 
showing text, items or even entities. They can be fully interactive with 
different actions depending on where players click.

## Usage:
Add it to your dependencies like this:

```
repositories {
	maven { url 'https://maven.nucleoid.xyz' }
}

dependencies {
	modImplementation include("eu.pb4:hologram-api:[TAG]")
}
```

Next you just need to create instance of hologram (WorldHologram/EntityHologram),
set every element and call `hologram.show()`. For example code see [Test Mod](https://github.com/Patbox/HologramAPI/blob/master/src/testmod/java/eu/pb4/holograms/TestMod.java)