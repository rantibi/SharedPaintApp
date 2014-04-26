package com.sharedpaint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;

/*
 * IO utilities
 */
public class IOUtils {

	public static byte[] ObjectToByteArray(Serializable object) throws IOException{
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    ObjectOutput out = null;
	    try {
	      out = new ObjectOutputStream(bos);   
	      out.writeObject(object);
	      return bos.toByteArray();

	    } 
	    finally 
	    {
	      out.close();
	      bos.close();
	    }
	}
	
	@SuppressWarnings("unchecked")
	public static<T> T byteArrayToObject(byte[] data) throws OptionalDataException, ClassNotFoundException, IOException{
		ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
	    return (T) is.readObject();
	}
}
