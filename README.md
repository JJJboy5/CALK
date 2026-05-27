# Cat Food Game for Nokia 3110 Classic

## Overview
A simple 2D game for the Nokia 3110 Classic where you play as a cat trying to find and collect food.

## Controls
- **2 (Up Arrow)** - Move Forward
- **4 (Left Arrow)** - Move Left
- **6 (Right Arrow)** - Move Right
- **5 (Center)** - Move Backward
- **\* (Star)** - Quit Game

## Game Features
- Simple 2D gameplay
- Score tracking
- Lives system
- Random food placement
- Collision detection

## Compilation
To compile and create the JAR file:

```bash
ant package
```

This will create `dist/CatGame.jar` which can be deployed to the Nokia 3110 Classic.

## Technical Details
- **Language**: Java (J2ME/MIDP 2.0)
- **Target Device**: Nokia 3110 Classic
- **Screen Resolution**: 176x144
- **JAR Format**: Compatible with MIDP devices

## Gameplay
Navigate your cat to find food pellets scattered around the screen. Each food collected increases your score. The game tracks lives and provides a simple HUD displaying current score and remaining lives.
