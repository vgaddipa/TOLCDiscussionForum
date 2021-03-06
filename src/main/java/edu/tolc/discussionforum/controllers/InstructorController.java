package edu.tolc.discussionforum.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.tolc.discussionforum.dto.GetCoursesDTO;
import edu.tolc.discussionforum.service.UsersService;

@Controller
public class InstructorController {
	@Autowired
	UsersService userService;
	
	// TODO: Get all the list of courses as soon as the instructor 
	// logs in the system
	@RequestMapping(value="/welcomeInstructor", method=RequestMethod.GET)
	public String welcomeInstructorGET() {
		return "welcomeInstructor";
	}
	
	@RequestMapping(value="/addCourse", method=RequestMethod.GET)
	public String addCourseGET() {
		return "addCourse";
	}
	
	// Adding a new course by the instructor
	@RequestMapping(value="/addCourse", method=RequestMethod.POST)
	public ModelAndView addCoursePOST(@RequestParam("courseName") String courseName,
			@RequestParam("courseDescription") String courseDescription) {
		ModelAndView modelAndView = new ModelAndView();
		
		// Get the instructor name via session attribute
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			String instructorsName = userDetail.getUsername();
			
		// Call the service layer method
		String successMsg = userService.addCourse(courseName, courseDescription,
				instructorsName);
		modelAndView.addObject("addCourseConfirmation", successMsg);
		} else {
				// permission-denied page because
				// user is not logged in
		}
		
		modelAndView.setViewName("addCourse");
		return modelAndView;
	}
	
	// Get courses for instructor
	@RequestMapping(value="/getMyCourses", method=RequestMethod.GET)
	public ModelAndView getMyCourses() {
		ModelAndView modelAndView = new ModelAndView();
		
		// Get the instructor name via session attribute
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			String instructorsName = userDetail.getUsername();
			
			// Get list of courses
			List<GetCoursesDTO> allCourses = new ArrayList<GetCoursesDTO>();
			allCourses = userService.getCourseList(instructorsName);
			
			// Add the object to model
			modelAndView.addObject("courseInformationForInstructor", allCourses);
			
		} else {
			// permission-denied page
			// user is not logged in
		}
		modelAndView.setViewName("getMyCourses");
		return modelAndView;
	}
	
	// Get the discussion board page based on the GET parameter
	@RequestMapping(value="/getMyCourses/discussionBoard/{courseid}", method=RequestMethod.GET)
	public ModelAndView discussionBoardGET(@PathVariable int courseid) {
		ModelAndView modelAndView = new ModelAndView();
		System.out.println(courseid);
		modelAndView.setViewName("welcomeInstructor");
		return modelAndView;
		
	}
}
