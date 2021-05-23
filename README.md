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