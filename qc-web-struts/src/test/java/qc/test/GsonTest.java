package qc.test;


import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.DigestUtils;

import qc.core.Page;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class GsonTest {

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void test(){
//		Gson gson = new Gson();
//		Example e = new Example("dragon");
//		System.out.println(gson.toJson(e));
//		
//		List<Example> list = new ArrayList<Example>();
//		list.add(e);
//		Page<Example> page = new Page<Example>(0,20,100,list);
//		System.out.println(gson.toJson(page,new TypeToken<Page<Example>>() {}.getType()));
//
//		gson = new GsonBuilder().serializeNulls().create();
//		System.out.println(gson.toJson(e));
		
		System.out.println(DigestUtils.md5DigestAsHex("password".getBytes()));
	}
}
