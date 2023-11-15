# Extended View Distance

---

## Description

This plugin enables the dynamic adjustment of player view distances on your server.
It accomplishes this by modifying the server's view distance to match the largest of either the server's default setting or the individual player's configured view distance.
This flexibility ensures an optimal viewing experience for all players on your server.

- The effective view distance of the client will not exceed this value<br>
  -    Client < Server = Server<br>
  -    Client < Extend = Client<br>
  -    Client > Extend = Extend

## Compiling

```bash
# Clone the repository
  git clone https://github.com/TropicalShadow/ExtendedViewDistance.git

# Enter the directory
cd ExtendedViewDistance

# Build the plugin
./gradlew shadowJar

# Output: ./build/libs/ExtendedViewDistance-<version>.jar
```

## Contribution

If you are looking to contribute towards this repository, please fork the repository and submit a pull request.

> **Note:**<br>If you are looking to use a later version than 1.20.2 (current latest) please submit a Issue on GitHub, or reach out to me directly on Discord: RealName_123#2570

## Credits

> Contributors: ``xuancat0208``, ``EuSouVoce``, ``TropicalShadow``<br>

> Upstream forks: ``SpigotPlugins-xuancat0208/FartherViewDistance``, ``EuSouVoce/FartherViewDistance_1.20.1``







