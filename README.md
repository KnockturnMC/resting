# Rest

A plugin that allows players to rest for spigot.

Installation
=

The plugin is based on the maven build tools, so you can easily install it using  
```bash
mvn package
```
The final output jar will be located in the `target` folder.

Commands
===

`sit`: Basic command to sit.
##
`lay`: Basic command to lay and stand up.
##
`rest {cleanse/sit/lay} [player]`: The admin command which allows players with `rest.admin` to clean all bugged armor
stands and force players to sit, lay and stand up.