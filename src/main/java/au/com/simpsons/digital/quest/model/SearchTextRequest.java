package au.com.simpsons.digital.quest.model;

import java.util.ArrayList;

import lombok.Data;


/**
 * Class for the Search Text request API . 
 * @author Ramesh
 */
@Data
public class SearchTextRequest {
	private ArrayList<String> searchText;
}
