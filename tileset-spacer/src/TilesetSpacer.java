import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * TilesetSpacer.java
 * @author TJ Couch
 * @date 23 Nov 2017
 *
 */

/**
 * TilesetSpacer
 * 
 * @author catsu
 * 
 *         Runs the program to separate tiles
 */
public class TilesetSpacer
{
	public static int tileWidth = 16;
	public static int tileHeight = 16;
	public static int borderSize = 2;
	
	public static void main(String[] args)
	{
		// run program for every file input
		for (int f = 0; f < args.length; f++)
		{
			BufferedImage image = null;
			
			try
			{
				image = ImageIO.read(new File(args[f]));
				System.out.println("Opened " + args[f]);
			}
			catch (IOException e)
			{
				System.out.println(e);
			}
			
			//the image exists
			if (image != null)
			{
				//edit image
				
				//check if image is proper multiple of tileWidth and tileHeight
				if (image.getWidth() % tileWidth != 0 || image.getHeight() % tileHeight != 0)
				{
					System.out.println("Image not a multiple of " + tileWidth + " or " + tileHeight);
					continue;
				}
				
				//get how many tiles there are
				int tilesX = (int) Math.ceil((double) image.getWidth() / tileWidth);
				int tilesY = (int) Math.ceil((double) image.getHeight() / tileHeight);
				
				BufferedImage newImage = new BufferedImage(tilesX * (tileWidth + borderSize * 2), tilesY * (tileHeight + borderSize * 2), BufferedImage.TYPE_INT_ARGB);
				
				Graphics2D newImCanvas = newImage.createGraphics();
				
				//space out and border tiles
				for (int i = tilesX; i >= 0; i--)
					for (int j = tilesY - 1; j >= 0; j--)
					{
						int shiftX = (i + 1) * borderSize;
						int shiftY = (j + 1) * borderSize;
						
						int imageX = i * tileWidth;
						int imageY = j * tileHeight;
						
						int newX = imageX + shiftX;
						int newY = imageY + shiftY;
						
						//copy pixels over to new position
						newImCanvas.drawImage(image, newX, newY, newX + tileWidth, newY + tileHeight, imageX, imageY, imageX + tileWidth, imageY + tileHeight, null);
						
						//smear borders
						for (int k = 0; k < tileWidth; k++)
						{
							//smear left all along height and corners
							if (k == 0 || k == tileWidth - 1)
							{
								for (int l = 0; l < tileHeight; l++)
								{
									
								}
							}
							//smear right all along height and corners
							else if (k == tileWidth - 1)
							{
								for (int l = 0; l < tileHeight; l++)
								{
									
								}
							}
							else //smear at top and bottom
							{
								
							}
						}
					}
				
				//free up the graphics
				newImCanvas.dispose();
				
				//save new image dir/name-spaced.ext
				try
				{
					String fileName = args[f];
					int divider = fileName.lastIndexOf('.');
					String fileType = fileName.substring(divider + 1);
					fileName = fileName.substring(0, divider) + "-spaced." + fileName.substring(divider + 1);
					ImageIO.write(newImage, fileType, new File(fileName));
					System.out.println("Saved " + fileName);
				}
				catch (IOException e)
				{
					System.out.println(e);
				}
			}
		}
	}
	
	public static void copyPixel(BufferedImage destImg, int destX, int destY, BufferedImage srcImg, int srcX, int srcY)
	{
		destImg.setRGB(destX, destY, srcImg.getRGB(srcX, srcY));
	}

}
