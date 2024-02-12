package com.user.util;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.springframework.web.multipart.MultipartFile;
public class Stegno {
	public static byte[] addText(MultipartFile imageFile, Integer id) throws IOException {
		BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageFile.getBytes()));
		int width = originalImage.getWidth();
		int pixelIndex = 0;

		// Loop through each pixel of the image
		for (int i = 0; i < 32; i++) {
			int pixel = originalImage.getRGB(pixelIndex % width, pixelIndex / width);
			int red = (pixel >> 16) & 0xFF;
			int green = (pixel >> 8) & 0xFF;
			int blue = pixel & 0xFF;
			int bit = ((id >> (31 - i)) & 1);
			red = (red & 0xFE) | bit;
			int newPixel = (red << 16) | (green << 8) | blue;
			originalImage.setRGB(pixelIndex % width, pixelIndex / width, newPixel);

			pixelIndex++;
		}
System.out.println(id);
		// Save the steganography image
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(originalImage, "png", byteArrayOutputStream);
//		ImageIO.write(originalImage, "png", new File("F:/Practice/just.jpg"));
		
//				ByteArrayMultipartFile	byteArrayMultipartFile=new ByteArrayMultipartFile("file", "example.jpg", byteArrayOutputStream.toByteArray());
		return byteArrayOutputStream.toByteArray();
				
//				File destFile = new File("F:/Practice/example.png");
//				byteArrayMultipartFile.transferTo(destFile);
//				ImageIO.doWrite(byteArrayMultipartFile, "png",destFile);
//return byteArrayMultipartFile;
	}

	public static Integer getText(MultipartFile imageFile) throws IOException {
		try {
			BufferedImage steganographyImage = ImageIO.read(new ByteArrayInputStream(imageFile.getBytes()));
			int width = steganographyImage.getWidth();

			int retrievedNumber = 0;
			int pixelIndex = 0;

			for (int i = 0; i < 32; i++) {
				int pixel = steganographyImage.getRGB(pixelIndex % width, pixelIndex / width);
				int red = (pixel >> 16) & 0xFF;
				int bit = red & 1;
				retrievedNumber = (retrievedNumber << 1) | bit;

				pixelIndex++;
			}

			return retrievedNumber;

		} catch (IOException e) {
			System.out.println("hereing");
			e.printStackTrace();
			return -1;
		}

	}

}
