package br.com.session.springsession.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.http.HttpSession;

@RestController
public class Controller {
	
	private final String pageLoadsAttribute = "load";
	
	@GetMapping("/")
	public String getMethodName(Principal principal, HttpSession session) {
		increment(session);
		session.setAttribute("user", principal);
		session.setAttribute("testAttribute", "testing persistence of this attribute");
		
		return "Hello, defined attributes, persisted session on Redis. Now go to the /testing endpoint";
	}
	
	@GetMapping("/testing")
	public ObjectNode getCount(Principal principal, HttpSession session) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode json = mapper.createObjectNode();
		
		String testAttribute = (String) session.getAttribute("testAttribute");
		Integer timesThePageWasReloaded = (Integer) session.getAttribute(this.pageLoadsAttribute);
		Principal user = (Principal) session.getAttribute("user");
		JsonNode userNode = mapper.valueToTree(user);
		
		json.set("user", userNode);
		json.put("testAttribute", testAttribute);
		json.put("timesThePageWasReloaded", timesThePageWasReloaded);
		
		return json;
	}
	
	private void increment(HttpSession session) {
		int loadsValue = session.getAttribute(this.pageLoadsAttribute) != null ? (Integer) session.getAttribute(this.pageLoadsAttribute)+1 : 0;
		session.setAttribute(this.pageLoadsAttribute, loadsValue);
	}
}
