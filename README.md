# tileset-spacer
A tool that spaces out tilesets to prevent tearing between tiles. Made to solve rendering issues in RetroBots Online.

![](https://github.com/tjcouch1/tileset-spacer/blob/master/tileset-spacer.gif)

This issue arises because of how tiles are rendered in GameMaker: Studio. Basically, the tiles are approximately thrown in by the renderer, so adjacent pixels show up sometimes. The stretcher pulls the edge of each tile to make the adjacent pixels the same color. To read about this problem from the developers, see [this GameMaker: Studio tech blog post](https://www.yoyogames.com/blog/3/seamless-tile-scaling-in-gamemaker).
