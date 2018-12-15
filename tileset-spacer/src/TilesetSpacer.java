import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

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
 * @author TJ Couch
 * 
 * Created 7/21/18
 * Modified 12/15/18
 * Runs the program to separate tiles
 */
public class TilesetSpacer
{
	public static int tileWidth = 16;
	public static int tileHeight = 16;
	public static int borderSize = 2;

	public static void main(String[] args)
	{
		//get the input scanner
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Tileset Spacer by TJ Couch\n");
		
		//if there are no commandline inputs, ask for a file
		if (args.length <= 0)
		{
			System.out.print("Enter the tileset's filename: ");
			
			args = new String[1];
			args[0] = scan.nextLine();
		}
		
		//get the tile width
		System.out.print("Enter the tile width in pixels (Default " + tileWidth + "): ");
		String in = scan.nextLine();
		if (in != null && !in.isEmpty())
			tileWidth = Integer.parseInt(in);
	
		//get the tile height
		System.out.print("Enter the tile height in pixels (Default " + tileHeight + "): ");
		in = scan.nextLine();
		if (in != null && !in.isEmpty())
			tileHeight = Integer.parseInt(in);
	
		//get the border size
		System.out.print("Enter the border thickness in pixels (Default " + borderSize + "): ");
		in = scan.nextLine();
		if (in != null && !in.isEmpty())
			borderSize = Integer.parseInt(in);
		
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

			boolean succeeded = spaceTileSet(image, args[f]);
			
			if (succeeded)
				System.out.println("Successfully spaced tileset " + args[f]);
			else System.out.println("Did not successfully space tileset " + args[f]);
		}

		System.out.println("Done.");
		
		scan.close();
	}
	
	public static boolean spaceTileSet(BufferedImage image, String fileName)
	{
		// the image exists
		if (image != null)
		{
			// edit image

			// check if image is proper multiple of tileWidth and tileHeight
			if (image.getWidth() % tileWidth != 0 || image.getHeight() % tileHeight != 0)
			{
				System.out.println("Image not a multiple of " + tileWidth + " or " + tileHeight);
				return false;
			}

			// get how many tiles there are
			int tilesX = (int) Math.ceil((double) image.getWidth() / tileWidth);
			int tilesY = (int) Math.ceil((double) image.getHeight() / tileHeight);

			BufferedImage newImage = new BufferedImage(tilesX * (tileWidth + borderSize * 2),
					tilesY * (tileHeight + borderSize * 2), BufferedImage.TYPE_INT_ARGB);

			Graphics2D newImCanvas = newImage.createGraphics();

			// space out and border tiles
			for (int i = tilesX - 1; i >= 0; i--)
				for (int j = tilesY - 1; j >= 0; j--)
				{
					int shiftX = borderSize + i * borderSize * 2;
					int shiftY = borderSize + j * borderSize * 2;

					int imageX = i * tileWidth;
					int imageY = j * tileHeight;

					int newX = imageX + shiftX;
					int newY = imageY + shiftY;

					// copy pixels over to new position
					newImCanvas.drawImage(image, newX, newY, newX + tileWidth, newY + tileHeight, imageX, imageY,
							imageX + tileWidth, imageY + tileHeight, null);

					// smear borders
					for (int k = 0; k < tileWidth; k++)
					{
						// smear left all along height
						if (k == 0)
						{
							for (int l = 0; l < tileHeight; l++)
								smearLeft(newImage, newX + k, newY + l);
						}
						// smear right all along height
						else if (k == tileWidth - 1)
						{
							for (int l = 0; l < tileHeight; l++)
								smearRight(newImage, newX + k, newY + l);
						}

						// smear at top and bottom
						{
							smearUp(newImage, newX + k, newY);
							smearDown(newImage, newX + k, newY + tileHeight - 1);
						}
					}
					// smear corners
					for (int k = 0; k < borderSize; k++)
					{
						// top
						smearLeft(newImage, newX, newY - (k + 1));
						smearRight(newImage, newX + tileWidth - 1, newY - (k + 1));
						// bottom
						smearLeft(newImage, newX, newY + tileHeight - 1 + (k + 1));
						smearRight(newImage, newX + tileWidth - 1, newY + tileHeight - 1 + (k + 1));
					}
				}

			// free up the graphics
			newImCanvas.dispose();

			// save new image as image_spaced.ext
			int divider = fileName.lastIndexOf('.');
			String fileType = fileName.substring(divider + 1);
			fileName = fileName.substring(0, divider) + "_spaced." + fileName.substring(divider + 1);
			
			try
			{
				ImageIO.write(newImage, fileType, new File(fileName));
				System.out.println("Saved " + fileName);
			}
			catch (IOException e)
			{
				System.out.println("Failed to write spaced tileset!");
				System.out.println(e);
				return false;
			}
		}
		else
		{
			System.out.println("Tileset not found!");
			return false;
		}
		
		return true;
	}

	public static void copyPixel(BufferedImage destImg, int destX, int destY, BufferedImage srcImg, int srcX, int srcY)
	{
		destImg.setRGB(destX, destY, srcImg.getRGB(srcX, srcY));
	}

	public static void smearLeft(BufferedImage image, int x, int y)
	{
		for (int i = 0; i < borderSize; i++)
			copyPixel(image, x - (i + 1), y, image, x, y);
	}

	public static void smearRight(BufferedImage image, int x, int y)
	{
		for (int i = 0; i < borderSize; i++)
			copyPixel(image, x + (i + 1), y, image, x, y);
	}

	public static void smearUp(BufferedImage image, int x, int y)
	{
		for (int i = 0; i < borderSize; i++)
			copyPixel(image, x, y - (i + 1), image, x, y);
	}

	public static void smearDown(BufferedImage image, int x, int y)
	{
		for (int i = 0; i < borderSize; i++)
			copyPixel(image, x, y + (i + 1), image, x, y);
	}
}
