package com.lyn.codeLearing.mode.decorator;


/**
 * 装饰者实现
 */
public class ConcreteDecorater1 extends Decorater {


    public ConcreteDecorater1(Component component) {
        super(component);
    }

    @Override
    public void doSomeThing() {
        super.doSomeThing();

        doAnotherThing();
    }


    /**
     * 子类多加的功能
     */
    private void doAnotherThing(){

        System.out.println("功能1");

    }
}
