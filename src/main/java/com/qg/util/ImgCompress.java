package com.qg.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.*;  


public class ImgCompress 
{
	private Image img;
	private int width;
	private int height;
	private final static int success = 1;
	private final static int fail = 0;
	
	
	/**
	 * 压缩图片
	 * @param photoPath 文件夹路径
	 * @param photoId 相片名
	 * @return
	 */
    @SuppressWarnings("finally")
	public static int ImageCompress(String photoPath, int photoId){
    	int result = fail;
    	try {
    		ImgCompress imgCompress = new ImgCompress(photoPath+photoId+".jpg");
    		//获取文件名
    		//String imgName = pictiurePath.substring(pictiurePath.lastIndexOf("/"));
    		// 规定相片的长度
    		imgCompress.resize(100, 100, photoPath, photoId);
    		result = success;
		} catch (IOException e) {
			result =fail;
			//如果捕抓到异常，则压缩失败
			e.printStackTrace();
		} finally {
			return result;
		}
    }
    
	@SuppressWarnings("finally")
	public static int ImageCompress(String photoPath, String photoName){
    	int result = fail;
    	try {
    		ImgCompress imgCompress = new ImgCompress(photoPath+photoName);
    		//获取文件名
    		//String imgName = pictiurePath.substring(pictiurePath.lastIndexOf("/"));
    		// 规定相片的长度
    		imgCompress.resize(200, 200, photoPath, photoName);
    		result = success;
		} catch (IOException e) {
			result =fail;
			//如果捕抓到异常，则压缩失败
			e.printStackTrace();
		} finally {
			return result;
		}
    }
    
    
    
    /**
     * 构造器
     */
    public ImgCompress(String fileName) throws IOException{
    	//获取文件
    	File file = new File(fileName);
    	//读入文件
    	img = ImageIO.read(file);
    	width = img.getWidth(null);
    	height = img.getHeight(null);
    }
    /**
     * 按照宽度或者高度进行压缩
     * @param w 最大宽度�
     * @param h 最大高度
     * @throws IOException
     */
    public void resizeFix(int w, int h, String path, int photoId) throws IOException{
    	if(width/height > w/h){
    		resizeByWidth(w,  path,  photoId);
    	} else{
    		resizeByHeight(h,  path,  photoId);
    	}
    }
    
    /**
     * 按照宽度等比例缩放图片
     * @param w 新宽度
     * @throws IOException
     */
    public void resizeByWidth(int w, String path, int photoId) throws IOException{
    	int h = (int) (height * w / width);
    	resize(w, h, path, photoId);
    }
    
    /**
     * 按照高度等比例缩放图片�
     * @param h 新高度
     * @throws IOException
     */
    public void resizeByHeight(int h, String path, int photoId) throws IOException{
    	int w = (int) (width * h / height);
    	resize(w, h,  path,  photoId);
    }
    
    /**
     * 根据规定长度对图片压缩�
     * @param w 宽度
     * @param h 高度
     * @throws IOException
     */
    @SuppressWarnings("restriction")
	public void resize(int w, int h, String path, int photoId) throws IOException{
    	BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_BGR);
    	image.getGraphics().drawImage(img, 0, 0, w, h, null);
    	String savePath = path + "t_" + photoId+".jpg";
    	File destFile = new File(savePath);
    	// 写出文件
    	FileOutputStream out = new FileOutputStream(savePath);   
    	//压缩后输出到文件流
    	JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out); 
    	encoder.encode(image); //  JPEG编码
    	out.flush();
        out.close(); 
    }
    
	public void resize(int w, int h, String path, String photoName) throws IOException{
    	BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_BGR);
    	image.getGraphics().drawImage(img, 0, 0, w, h, null);
    	String savePath = path +"_" +photoName;
    	File destFile = new File(savePath);
    	// 写出文件
    	FileOutputStream out = new FileOutputStream(savePath);   
    	//压缩后输出到文件流
    	JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out); 
    	encoder.encode(image); //  JPEG编码
    	out.flush();
        out.close(); 
    }
}
