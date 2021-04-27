package au.com.simpsons.digital.quest.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * Class for the Search Text response API . 
 * @author Ramesh
 */
@Data
public class SearchTextResponse {
	List<Map<String, Long>> counts;
}
