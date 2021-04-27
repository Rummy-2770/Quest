package au.com.simpsons.digital.quest.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

/**
 * Static Class used to store the parsed file data. 
 * Stored data to be deleted automatially by every 5 seconds.
 * 
 * @author Ramesh
 */

@Slf4j
public class ParsedFileData {

	private static Map<String, HashMap<String, Long>> cacheMappedValue = new ConcurrentHashMap<>();

	//Parsed data to be cached for 5 seconds
	private static Long timeToLive = Long.valueOf(5000);

	public static <T> void set(String key, HashMap<String, Long> value) {
		cacheMappedValue.put(key, value);
	}

	public static HashMap<String, Long>  get(String key) {
		return cacheMappedValue.get(key);
	}

	static {
		log.debug("Timer block set to remove the content after 5 second ");
		Timer t = new Timer();
		t.schedule(new ClearTimerTask(cacheMappedValue), 0, timeToLive);
	}

	private static class ClearTimerTask extends TimerTask {

		Map<String, HashMap<String, Long>> cacheMappedValue;

		public ClearTimerTask(Map<String, HashMap<String, Long>> cacheMappedValue) {
			this.cacheMappedValue = cacheMappedValue;
		}

		@Override
		public void run() {
			Set<String> keys = cacheMappedValue.keySet();
			for(String key : keys) {
				log.debug("Job invoked to remove the stored value , {}", key);
				cacheMappedValue.remove(key);
			}
		}
	}
}