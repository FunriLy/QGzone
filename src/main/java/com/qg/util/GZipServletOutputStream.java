package com.qg.util;

import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class GZipServletOutputStream extends ServletOutputStream {

	private GZIPOutputStream gzipOutputStream;
	
	public  GZipServletOutputStream(ServletOutputStream servletOutputStream) throws IOException{
		this.gzipOutputStream = new GZIPOutputStream(servletOutputStream);
	}
	
	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setWriteListener(WriteListener arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void write(int b) throws IOException {
		gzipOutputStream.write(b);

	}
	
	public GZIPOutputStream getGzipOutputStream(){
		return gzipOutputStream;
	}

}
