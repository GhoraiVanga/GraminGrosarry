package com.javamaster.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

import com.javamaster.beans.Product;
import com.javamaster.beans.User;
import com.javamaster.model.DB;


public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ArrayList<Product> list = new ArrayList<>();
	static ArrayList<String> cartlist = new ArrayList<>();
	ArrayList<User> userList = new ArrayList<>();
	HttpSession session;
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String page = request.getParameter("page");
		if(page == null || page.equals("index")) {
			
			DB db = new DB();
			 try {
				list = db.fetch();
				System.out.println("2nd");
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("3nd");
				System.out.println(e);
			}
			
			 session = request.getSession();
			 session.setAttribute("cartlist", cartlist);
			 session.setAttribute("list", list);
			 System.out.println("4");
			 
			 System.out.println(list);
			 System.out.println(cartlist);
			 request.getRequestDispatcher("/WEB-INF/view/index.jsp").forward(request, response);
		}else
		{ 
			System.out.println("5");
			doPost(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String page = request.getParameter("page");
		
		if(page.equals("login")) {
			request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
		}
		
		if(page.equals("sign-up")) {
			request.getRequestDispatcher("/WEB-INF/view/signup.jsp").forward(request, response);
		}
		
		if(page.equals("sign-up-form")) {
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String username = request.getParameter("username");
			String address = request.getParameter("address");
			String password_1 = request.getParameter("password_1");
			String password_2 = request.getParameter("password_2");
			
			if(password_1.equals(password_2)) {
				
				User user = new User();
				user.setAddress(address);
				user.setEmail(email);
				user.setName(name);
				user.setUsername(username);
				user.setPassword(password_1);
				
				DB db = new DB();
				try {
					db.addUser(user);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				request.setAttribute("username", username);
				request.setAttribute("msg", "Account created successfully, Please Login!");
				request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
				
			}else {
				request.setAttribute("msg", "The two passwords do not match");
				request.setAttribute("name", name);
				request.setAttribute("address", address);
				request.setAttribute("email", email);
				request.setAttribute("username", username);
				request.getRequestDispatcher("/WEB-INF/view/signup.jsp").forward(request, response);
			}
			
		}
		
		if(page.equals("login-form")) {
			
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			DB db = new DB();
			User user = new User();
			boolean status = false;
			try {
				status = db.checkUser(username,password);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(status) {
				session = request.getSession();
				session.setAttribute("session", session);
				
				try {
					userList = db.fetchUser();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				session.setAttribute("address", user.fetchadd(userList,username));
				session.setAttribute("email", user.fetchemail(userList,username));
				session.setAttribute("name", user.fetchname(userList,username));
				session.setAttribute("username", username);
				request.getRequestDispatcher("/WEB-INF/view/index.jsp").forward(request, response);
			}else {
				request.setAttribute("msg", "Invalid Crediantials");
				request.setAttribute("username", username);
				request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
			}
			
		}
		
		if(page.equals("logout")) {
			session = request.getSession();
			session.invalidate();
			
			 session = request.getSession();
			 cartlist.clear();
			 session.setAttribute("cartlist", cartlist);
			 session.setAttribute("list", list);
			
			request.getRequestDispatcher("/WEB-INF/view/index.jsp").forward(request, response);
		}
		
		if(page.equals("mobiles") || page.equals("laptops") || page.equals("clothing") || page.equals("home-decor") || page.equals("all-products")) {
			DB db = new DB();
			 try {
				list = db.fetch();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			request.setAttribute("list", list);
			
			if(page.equals("mobiles"))
				request.getRequestDispatcher("/WEB-INF/view/mobiles.jsp").forward(request, response);
			if(page.equals("laptops"))
				request.getRequestDispatcher("/WEB-INF/view/laptops.jsp").forward(request, response);
			if(page.equals("clothing"))
				request.getRequestDispatcher("/WEB-INF/view/clothing.jsp").forward(request, response);
			if(page.equals("home-decor"))
				request.getRequestDispatcher("/WEB-INF/view/home-decor.jsp").forward(request, response);
			if(page.equals("all-products"))
				request.getRequestDispatcher("/WEB-INF/view/all-products.jsp").forward(request, response);
		}
		
		
		
		if(page.equals("showcart")) {
			request.getRequestDispatcher("/WEB-INF/view/cart.jsp").forward(request, response);
		}
		
		if(page.equals("addtocart")) {
			String id = request.getParameter("id");
			String action = request.getParameter("action");
			Product p = new Product();
			boolean check = p.check(cartlist,id);
			
			if(check==true)
			{	
			//JOptionPane.showMessageDialog(null, "Product is already added to Cart", "Info", JOptionPane.INFORMATION_MESSAGE);
			//servlet code
				cartlist.add(id);
				request.setAttribute("loginError","Incorrect password");
			}
			else {
			cartlist.add(id);
			request.setAttribute("loginError","Incorrect password");
		//	JOptionPane.showMessageDialog(null, "Product successfully added to Cart", "Info", JOptionPane.INFORMATION_MESSAGE);
			}
			
			if(action.equals("index"))
				request.getRequestDispatcher("/WEB-INF/view/index.jsp").forward(request, response);
			if(action.equals("allproducts"))
				request.getRequestDispatcher("/WEB-INF/view/all-products.jsp").forward(request, response);
			if(action.equals("clothing"))
				request.getRequestDispatcher("/WEB-INF/view/clothing.jsp").forward(request, response);
			if(action.equals("home-decor"))
				request.getRequestDispatcher("/WEB-INF/view/home-decor.jsp").forward(request, response);
			if(action.equals("laptops"))
				request.getRequestDispatcher("/WEB-INF/view/laptops.jsp").forward(request, response);
			if(action.equals("mobiles"))
				request.getRequestDispatcher("/WEB-INF/view/mobiles.jsp").forward(request, response);
		}
		
		if(page.equals("success")) {
			
			request.getRequestDispatcher("/WEB-INF/view/success.jsp").forward(request, response);
			
			session = request.getSession();
			 cartlist.clear();
			 session.setAttribute("cartlist", cartlist);
		}
		
		if(page.equals("remove")) {
			String id = request.getParameter("id");
			Product p = new Product();
			cartlist = p.remove(cartlist,id);
			
			session = request.getSession();
			session.setAttribute("cartlist", cartlist);
			request.getRequestDispatcher("/WEB-INF/view/cart.jsp").forward(request, response);
		}
		
		if(page.equals("price-sort")) {
			String price = request.getParameter("sort");
			String action = request.getParameter("action");
			Product p = new Product();
			if(price.equals("low-to-high"))
				list = p.lowtohigh(list);
			else
				list = p.hightolow(list);
			
			session.setAttribute("list", list);
			
			if(action.equals("index"))
				request.getRequestDispatcher("/WEB-INF/view/index.jsp").forward(request, response);
			if(action.equals("all-products"))
				request.getRequestDispatcher("/WEB-INF/view/all-products.jsp").forward(request, response);
			if(action.equals("mobiles"))
				request.getRequestDispatcher("/WEB-INF/view/mobiles.jsp").forward(request, response);
			if(action.equals("laptops"))
				request.getRequestDispatcher("/WEB-INF/view/laptops.jsp").forward(request, response);
			if(action.equals("clothing"))
				request.getRequestDispatcher("/WEB-INF/view/clothing.jsp").forward(request, response);
			if(action.equals("home-decor"))
				request.getRequestDispatcher("/WEB-INF/view/home-decor.jsp").forward(request, response);
		}
	}

}

