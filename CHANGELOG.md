# Elytra Trims 3.0 - The Doomsday Update

## Update notes
- In 1.20.5, due to ItemStack changes, converted elytras **__will lose their glow__**, all other features will remain. This will be looked into in a future release, but until then you can afford an extra glow ink sac, right?
- Trimming and dyeing currently can't be disabled on the server side, as it is now controlled by tags.

## Elytra preview
Elytra trims now renders elytra entity model for its item - just like shields, banners and beds do:  
![Item preview 1](https://i.imgur.com/aRNGIhk.png)
![Item preview 2](https://i.imgur.com/1r9WOHE.png)  
If you don't like it - it can be disabled in the mod config. To access it you need [YACL](https://modrinth.com/mod/yacl) and on Fabric [Mod Menu](https://modrinth.com/mod/modmenu). On Forge it is accessible via default mods menu.

## Kotlin migration - who asked?
First of all, I asked. 1.20.5 brought an enormous amount of changes, 
which was especially bad for Elytra Trims, because it is developed for multiple Minecraft versions.
Kotlin provides several ways (such as type aliases, extension and inline functions)
that simplify code versioning. So here it is, cope with it.

## Texture processing rewrite
Elytra Trims generates textures dynamically to adjust for dozens of trims and now unlimited amount of banner patterns.
Since I got more skill in modding and had to look through the entire codebase, this process has been optimised.  
Notably:
- Elytra model mask is no longer duplicated for each texture pass.
- Armor trims are now queried from atlas definitions.
- Texture rendering now extensively uses caching and reports missing textures.

## Other changes
- `bolt` and `flow` trims have been added, available with 1.21 datapack (@masik16u)
- [DashLoader](https://modrinth.com/mod/dashloader) is no longer incompatible

![1.21 trims](https://i.imgur.com/74ObJX4.png)