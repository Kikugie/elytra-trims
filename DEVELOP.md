# Elytra Trims development notes
> Welcome, fair traveller to the Deep Dark, where no sunlight shines upon us and danger lurks in every corner!

If you happen to `git clone` this repo, this may help you work with the codebase.

## Building from source
Elytra Trims uses [Stonecutter](https://github.com/kikugie/stonecutter-kt) to manage multiple Minecraft versions. Familiarize yourself with it if you want to work on the code.

**TL;DR**: use task `stonecutter/Set active version to X` before running Minecraft on `X`. 
You can use `project/buildActive` to build that version specifically.  
**Do not** run `build` task, `project/chiseledBuild` is the correct one.

## Editing the code
**Do not** run reformat on random files or worse the whole project. It will remove important versioned comments and break the build.