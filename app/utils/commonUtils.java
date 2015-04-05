/**
 * 
 */
package utils;

import java.util.UUID;

import play.Logger;

/**
 * @author ashutosh
 *
 */
public class commonUtils {
	public static String generateUUID() {
		String uuid;
		try{
			uuid = UUID.randomUUID().toString().replaceAll("-", "");
		} catch(Exception e) {
			Logger.error(e.toString());
			return null;
		}
		return uuid;
	}

}
