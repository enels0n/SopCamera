# SopCamera

`SopCamera` adds a custom camera item that creates and manages map-based photos.

## What it does

- registers a custom camera item
- stores generated and missed photo state
- manages a local `maps` folder and a `missed.yml` data file
- includes camera rendering and click handling logic

## Files

- `config.yml` - camera settings, item settings, locale strings, and reward commands
- `missed.yml` - saved missed photo entries

## Command

- `/sopcamera`

## Notes

- built against `1.20.4`-style NMS imports
- produces a single final `SopCamera.jar` without `original-*` release clutter
