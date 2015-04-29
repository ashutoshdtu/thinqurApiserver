/**
 * 
 */
package utils;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

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
	
	 /*
     * Register MongoJack's Date deserializer w/ Jackson so that you can deserialize ISODate() dates from Mongo.
     *
     * @param mapper The Jackson object mapper which will use your deserializer.
     */
    /*public static void register(ObjectMapper mapper) {
        SimpleModule mongoDateModule =
                new SimpleModule("MongoDateDeserializer", new Version(1, 0, 0, "", "", ""));
        mongoDateModule.addDeserializer(Date.class, new DateDeserializer());

        mapper.registerModule(mongoDateModule);
    }*/

}
