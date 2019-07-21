## Example plugin for TactView

Sample plugin, should be turned to an archetype

### Build

    mvn clean package

Plugin is generated in target/testplugin-{version}.zip

This plugin can be installed into TactView.

If you have any transitive native libraries, you have to add it to the zip into a folder named `native`.
Note that native libraries that are loaded by JNA should be added `src/main/resource/{platform}`.
