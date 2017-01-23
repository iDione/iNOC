package com.idione.inoc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.idione.inoc.services.InocProcessor;

@RequestMapping(value = "/admin")
@Controller
public class AdministratorController extends ApplicationController {

    private InocProcessor inocProcessor;

    @Autowired
    public void setInocProcessor(InocProcessor inocProcessor) {
        this.inocProcessor = inocProcessor;
    }

    @RequestMapping(value = "/run", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void runInoc() {
        inocProcessor.run();
    }
}
