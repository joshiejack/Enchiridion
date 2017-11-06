# Enchiridion
Books mod for minecraft

Download Enchiridion on [CurseForge](https://minecraft.curseforge.com/projects/enchiridion)

How to get Enchiridion through maven
---
Add to your build.gradle:
```gradle
repositories {
  maven {
    // url of the maven that hosts Enchiridions files
    url "http://girafi.dk/maven/"
  }
}

dependencies {
  // compile against Enchiridion
  deobfCompile "uk.joshie.enchiridion:enchiridion${mc_version}:${mc_version}-${enchiridion_version}"
}
```

`${mc_version}` & `${enchiridion_version}` can be found [here](http://girafi.dk/maven/uk/joshie/enchiridion/), check the file name of the version you want.