package com.github.florent37.holyfragment.processor;

import com.squareup.javapoet.ClassName;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Element;

public class Holder {
    public Class annotation;
    public Element element;
    public ClassName classNameComplete;
    public String className;
    public List<Element> methods;

    public Holder(Class annotation, Element element, ClassName classNameComplete, String className) {
        this.annotation = annotation;
        this.element = element;
        this.classNameComplete = classNameComplete;
        this.className = className;
        this.methods = new ArrayList<>();
    }

    public void addMethod(Element child){
        methods.add(child);
    }

}
