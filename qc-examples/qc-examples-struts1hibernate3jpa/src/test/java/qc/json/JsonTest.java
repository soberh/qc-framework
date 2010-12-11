package qc.json;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class JsonTest {
	@Test
	public void test() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("success", true);
		json.put("msg", "test");
		Assert.assertEquals("{\"msg\":\"test\",\"success\":true}", json.toString());
		//System.out.println(json.toString());
	}
}
