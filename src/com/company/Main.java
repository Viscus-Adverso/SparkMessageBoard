package com.company;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        HashMap<String, User> users = new HashMap<>();
        ArrayList<Message> mgs = new ArrayList<>();

        Spark.get(
                "/",
                (request, response) -> {
                    Session session = request.session();
                    HashMap m = new HashMap();
                    String name = session.attribute("Loginname");
                    m.put("name", name);
                    m.put("messages", mgs);
                    return new ModelAndView(m, "home.html");
                }, new MustacheTemplateEngine()
        );



        Spark.post(
                "/login",
                (request, response) -> {
                    String name = request.queryParams("loginName");
                    User user = users.get(name);
                    if (user == null) {
                        user = new User();
                        users.put(name, user);
                    }
                    Session session = request.session();
                    session.attribute("Loginname", name);
                    response.redirect("/");
                    return null;
                }


        );

        Spark.post(
                "/logout",
                (request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return null;
                }
        );

        Spark.post(
                "/create-message",
                (request, response) -> {
                    String message = request.queryParams("createMessage");
                    Message m = new Message(message);
                    mgs.add(m);
                    response.redirect("/");
                    return null;
                }

        );

    }
}
