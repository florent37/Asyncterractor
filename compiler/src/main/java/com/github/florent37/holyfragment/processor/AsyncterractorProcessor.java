package com.github.florent37.holyfragment.processor;

import com.github.florent37.asyncterractor.annotations.OnThread;
import com.github.florent37.asyncterractor.annotations.OnUiThread;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import static javax.lang.model.element.ElementKind.INTERFACE;
import static javax.lang.model.element.ElementKind.METHOD;

@SupportedAnnotationTypes({
    "com.github.florent37.asyncterractor.annotations.OnThread",
    "com.github.florent37.asyncterractor.annotations.OnUiThread"
})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@AutoService(javax.annotation.processing.Processor.class) public class AsyncterractorProcessor
    extends AbstractProcessor {

    private Map<TypeName, Holder> holders = new HashMap<>();
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        filer = env.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        processAnnotations(env);

        writeHoldersOnJavaFile();

        return true;
    }

    protected boolean isAcceptable(Element element) {
        if (element.getKind() != INTERFACE) {
            throw new IllegalStateException("OnThread/OnUiThread annotation must be on a interface.");
        }

        return true;
    }

    protected void processAnnotations(Element element, Class annotation) {
        //ex: @OnUiThread public class MyView;
        final ClassName classFullName = ClassName.get((TypeElement) element); //com.github.florent37.MyView
        final String className = element.getSimpleName().toString(); //MyView

        Holder holder = new Holder(annotation, classFullName, className);
        final List<? extends Element> enclosedElements = element.getEnclosedElements();
        for(Element child : enclosedElements){
            if(child.getKind() == METHOD ){
                holder.addMethod(child);
            }
        }
        holders.put(classFullName, holder);
    }

    protected void processAnnotations(RoundEnvironment env) {
        for (Element element : env.getElementsAnnotatedWith(OnUiThread.class)) {
            if (isAcceptable(element)) {
                processAnnotations(element, OnUiThread.class);
            }
        }
        for (Element element : env.getElementsAnnotatedWith(OnThread.class)) {
            if (isAcceptable(element)) {
                processAnnotations(element, OnThread.class);
            }
        }
    }

    protected void writeHoldersOnJavaFile() {
        for (Holder holder : holders.values()) {
            construct(holder);
        }
        constructAsyncterractor(holders);
        holders.clear();
    }

    private void constructAsyncterractor(Map<TypeName, Holder> holders) {
        final Collection<MethodSpec> methodSpecs = new ArrayList<>();

        for (Holder holder : holders.values()) {
            final MethodSpec.Builder constructorB = MethodSpec.methodBuilder("of")
                .returns(holder.classNameComplete)
                .addParameter(holder.classNameComplete, "subject")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addStatement("return new Asyncterractor$L(subject)", "$"+holder.className);
            methodSpecs.add(constructorB.build());
        }


        final TypeSpec newClass = TypeSpec.classBuilder("Asyncterractor")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethods(methodSpecs)
            .build();

        final JavaFile javaFile = JavaFile.builder("com.github.florent37.asyncterractor", newClass).build();

        try {
            javaFile.writeTo(System.out);
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void construct(Holder holder) {
        final Collection<MethodSpec> methodSpecs = new ArrayList<>();

        final MethodSpec.Builder constructorB = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(holder.classNameComplete, "subject")
            .addStatement("this.reference = new WeakReference<>(subject)");

        if(holder.annotation == OnThread.class) {
            constructorB.addStatement("this.handler = new Handler()");
        } else {
            constructorB.addStatement("this.handler = new Handler($T.getMainLooper())", ClassName.bestGuess("android.os.Looper"));
        }

        methodSpecs.add(constructorB.build());

        for(Element method : holder.methods) {
            ExecutableElement methodType = (ExecutableElement)method;
            final String methodName = method.getSimpleName().toString();

            final MethodSpec.Builder methodB = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.get(methodType.getReturnType()))
                .addAnnotation(Override.class);

            for(VariableElement variableElement : methodType.getParameters()){
                methodB.addParameter(TypeName.get(variableElement.asType()), variableElement.getSimpleName().toString(), Modifier.FINAL);
            }

            methodB.addCode("handler.post(new $T() {\n", ClassName.get(Runnable.class))
                .addCode("@Override public void run() {\n")
                .addStatement("final $T subject = reference.get()", holder.classNameComplete)
                .beginControlFlow("if (subject != null)");

            final StringBuilder call = new StringBuilder();
            call.append("subject.").append(methodName).append("(");
            boolean first = true;
            for(VariableElement variableElement : methodType.getParameters()){
                //TODO handle Asyncterractor.of here
                call.append(variableElement.getSimpleName().toString());
                if(first) {
                    first = false;
                } else {
                    call.append(", ");
                }
            }
            call.append(")");

            methodB.addStatement(call.toString());
            methodB.endControlFlow();
            methodB.addCode("}});");

            methodSpecs.add(methodB.build());
        }

        final TypeSpec newClass = TypeSpec.classBuilder("Asyncterractor" + "$" + holder.className)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addSuperinterface(holder.classNameComplete)
            .addMethods(methodSpecs)
            .addField(ClassName.bestGuess("android.os.Handler"), "handler", Modifier.PRIVATE, Modifier.FINAL)
            .addField(ParameterizedTypeName.get(ClassName.bestGuess("java.lang.ref.WeakReference"), holder.classNameComplete), "reference", Modifier.PRIVATE, Modifier.FINAL)
            .build();

        final JavaFile javaFile = JavaFile.builder(holder.classNameComplete.packageName(), newClass).build();

        try {
            javaFile.writeTo(System.out);
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
